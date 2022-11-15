package com.chen.netty.filechannel.write;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFilechannel {
    public static void main(String[] args) throws Exception{
        String str = "Hello,沃天";
        System.out.println("汉".getBytes("Unicode").length);
        System.out.println("汉".getBytes("utf-8").length);
        //输出流
        FileOutputStream fos = new FileOutputStream("E:\\NioFileChaanel.txt");
        //通过FileOutputStream获取对应的FileChannel
        FileChannel fosChannel = fos.getChannel();
        //创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入byteBuffer
        byteBuffer.put(str.getBytes());
        //对byteBuffer进行切换，读写切换
        byteBuffer.flip();
        //将byteBuffer写入fosChannel
        fosChannel.write(byteBuffer);
        fos.close();

    }

}
