package com.chen.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Author @Chenxc
 * @Date 2022/2/25 15:16
 */
public class ClientHandle implements Runnable{
    private Socket socket;
    public ClientHandle( Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true){
            try {
                InputStream inputStream = socket.getInputStream();
                byte[] b = new byte[1024];
                inputStream.read(b);
                System.out.println("客户端发来：" + new String(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
