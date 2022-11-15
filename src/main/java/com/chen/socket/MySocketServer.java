package com.chen.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author @Chenxc
 * @Date 2022/2/25 15:12
 */
public class MySocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true){
            System.out.println("服务器已启动，等待连接");
            Socket accept = serverSocket.accept();
            System.out.println("服务器收到连接：" + accept.getInetAddress());
            ClientHandle handle = new ClientHandle(accept);
            Thread t = new Thread(handle);
            t.start();
        }
    }
}
