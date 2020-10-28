package io.github.skycat.geekstudy.java.week02;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HttpServer01
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-10-27 23:17:18
 */
public class HttpServer01 {
	/** max request data length: 1MB */
	private static final int MAX_REQUEST_DATA_LENGTH = 1024 * 1024;
	
	/**
	 * main
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8801);
		boolean flag = true;
		while (flag) {
			try {
				Socket socket = serverSocket.accept();
				service(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		serverSocket.close();
	}
	
	/**
	 * service
	 * @param socket
	 */
	private static void service(Socket socket) {
		try {
			System.out.println("request client: " + socket.getRemoteSocketAddress());
			Thread.sleep(20);
			BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
			int available = inputStream.available();
			if (available > 0) {
				if (available > MAX_REQUEST_DATA_LENGTH) {
					invalidRequestResponse(socket.getOutputStream());
					socket.close();
					return;
				}
				byte[] requestData = new byte[available];
				inputStream.read(requestData);
				System.out.println("request data:");
				System.out.println(new String(requestData));
			}
			response(socket.getOutputStream());
			socket.close();
			System.out.println("----- end ----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * response
	 * @param out
	 */
	private static void response(OutputStream out) {
		PrintWriter printWriter = new PrintWriter(out, true);
		printWriter.println("HTTP/1.1 200 OK");
		printWriter.println("Content-Type:text/html;charset=utf-8");
		String body = "hello, nio";
		printWriter.println("Content-Length:" + body.getBytes().length);
		printWriter.println();
		printWriter.println(body);
	}
	
	/**
	 * invalidRequestResponse
	 * @param out
	 */
	private static void invalidRequestResponse(OutputStream out) {
		PrintWriter printWriter = new PrintWriter(out, true);
		printWriter.println("HTTP/1.1 400 Bad Request");
		printWriter.println("Content-Type:text/html;charset=utf-8");
		printWriter.println();
		printWriter.println("400 Bad Request");
	}
}
