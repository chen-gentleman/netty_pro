package com.chen.netty.filechannel.read;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel {
    public static void main(String[] args) throws Exception {

        //文件输入流
        File f = new File("E:\\NioFileChaanel.txt");
        FileInputStream fis = new FileInputStream(f);
        //获取对应的FileChannel
        FileChannel channel = fis.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer =  ByteBuffer.allocate(8);
        //将通道的数据读入到buffer
        //channel.read(byteBuffer);
        byteBuffer.put((byte)97);

        System.out.println(byteBuffer.isReadOnly());;
        channel.write(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fis.close();
    }
}
