package com.chen.netty.filechannel.readAndWrite;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFilechannel {
    public static void main(String[] args) throws Exception{
        //文件输入流、对应的channel
        FileInputStream fis = new FileInputStream("E:\\NioFileChaanel.txt");
        FileChannel fisChannel = fis.getChannel();

        //文件输出流、对应的channel
        FileOutputStream fos = new FileOutputStream("E:\\NioFileChaanel-copy.txt");
        FileChannel fosChannel = fos.getChannel();

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        int i = 1;
        //循环读取
       // while (true){
            byteBuffer.clear();
            //先读取到buffer
            int read = fisChannel.read(byteBuffer);
            System.out.println("read=== "+ read  + "第" + (i++) + "次");

          /*  if (read == -1){
                break;
            }*/
            //读写切换
            byteBuffer.flip();
            //再写入
            fos.write(byteBuffer.array());
       // }
        //关闭流
        fis.close();
        fos.close();

    }
}
