package com.chen.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author @Chenxc
 * @Date 2022/2/25 15:19
 */
public class MySocketClient {
    public static void main(String[] args) throws IOException {
        int i = 0;
        Socket socket = new Socket("127.0.0.1",6666);
        while (true){
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(("第" + i++ +"次").getBytes());
            outputStream.flush();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
