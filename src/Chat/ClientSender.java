package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import sun.net.InetAddressCachePolicy;

public class ClientSender {
	private static final int PORT = 5002;
	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(System.in);
			socket = new Socket();
			socket.connect(new InetSocketAddress("0.0.0.0",PORT));
			System.out.println("[client]Client connected....");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			
			System.out.print("닉네임을 입력하세요>>");
			String nickname = scanner.nextLine();
			pw.println("join:" + nickname);
			
			new ClientRecieveThread(br,socket).start();
			
			while(true) {
				System.out.print(">>");
				String line = scanner.nextLine();
				
				if("quit".equals(line)) {
					break;
				}
				else {
					pw.println("message:"+line);
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(scanner!=null) {
					scanner.close();
				}
				if(socket!=null && !socket.isClosed()) {
					socket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
