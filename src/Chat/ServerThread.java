package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ServerThread extends Thread {
	private Socket socket = null;
	private String nickName;
	List<Writer> listwriter;
	
	public ServerThread(Socket socket, List<Writer> listwriter) {
		this.socket = socket;
		this.listwriter = listwriter;
	}
	
	@Override
	public void run() {
		System.out.println("[thread server] connected");
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			
			
			while(true) {
				String data = br.readLine();
				
				if(data == null) {
					System.out.println("[thread server] closed");
					doQuit(pw);
					break;
				}
				
				//∑Œ±◊¿Œ
				String[] tokens = data.split(":");
				
				if("join".equals(tokens[0])){
					dojoin(tokens[1], pw);
				} else if("message".equals(tokens[0])) {
					domessage(tokens[1]);
				} else if("quit".equals(tokens[0])) {
					doQuit(pw);
				} else {
					System.out.println("«œ¡ˆ∏∂!!");
				}
				
			}
		} catch(SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && !socket.isClosed())
					socket.close();
			}  catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doQuit(Writer writer) {
		removeWriter(writer);
		
		String data = nickName + " ¥‘¿Ã ≈¿Â«œºÃΩ¿¥œ¥Ÿ.";
		broadcast(data);
	}

	private void removeWriter(Writer writer) {
		synchronized (writer) {
			listwriter.remove(writer);
		}
	}

	private void dojoin(String nickname, Writer writer) {
		this.nickName = nickname;
		String data = nickname + " ¥‘¿Ã ¿‘¿Â«œºÃΩ¿¥œ¥Ÿ.";
		broadcast(data);
		addWriter(writer);
	}

	private void addWriter(Writer writer) {
		synchronized (writer) {
			listwriter.add(writer);
		}
	}
	
	public void domessage(String message) {
		String msg = nickName + ":" + message + "\r\n";
		broadcast(msg);
	}
	
	private void broadcast(String data) {
		synchronized ( listwriter ) {
			for(Writer writer : listwriter) {
				PrintWriter printwriter = (PrintWriter)writer;
				printwriter.println(data);
				printwriter.flush();
			}
		}
	}
}
