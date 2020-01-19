package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import sun.net.InetAddressCachePolicy;

public class ClientRecieveThread extends Thread {
	private Socket socket = null;
	private BufferedReader br;
	private PrintWriter pw;
	private String line;
	public ClientRecieveThread(BufferedReader br, Socket socket) {
		this.br = br;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true);
			
			while(true) {
				line = br.readLine();
				
				System.out.println(line);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
