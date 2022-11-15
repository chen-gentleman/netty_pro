package com.chen.netty.filechannel.MapperByteBuffer;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * MapperByteBuffer 可以让文件直接在内存中进行修改，而如何同步到文件由NIO来完成
 *
 * 说明：
 * 1，MapperByteBuffer  可以让文件直接在内存中进行修改，操作系统不需要拷贝一次
 */

public class MapperByteBufferTest {
    public static void main(String[] args) throws Exception{
        //  随机流（RandomAccessFile）不属于IO流，支持对文件的读取和写入随机访问。
        RandomAccessFile accessFile = new RandomAccessFile("E:\\RandomAccessFile.txt","rw");
        //对应通道
        FileChannel accessFileChannel = accessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = accessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put(3,(byte) '6');
       // mappedByteBuffer.put(5,(byte) 'Y');//IndexOutOfBoundsException
        accessFile.close();
        System.out.println("success");

    }
}
