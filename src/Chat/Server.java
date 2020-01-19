package Chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private static final int PORT = 5002;
	static List<Writer> listwriter =  new ArrayList<Writer>();
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		
		
		try {
			//서버 소켓 생성
			serversocket = new ServerSocket();
			//바인딩
			serversocket.bind(new InetSocketAddress("0.0.0.0", PORT));
			System.out.println("[server] server starts....");
			
			while(true) {
				//연결
				Socket socket = serversocket.accept();
				//쓰레드 부르기
				new ServerThread(socket, listwriter).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(serversocket != null && !serversocket.isClosed()) {
					serversocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
