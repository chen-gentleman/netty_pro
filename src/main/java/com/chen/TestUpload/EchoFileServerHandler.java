package com.chen.TestUpload;

import cn.hutool.core.io.FileUtil;
import com.chen.netty.protocol.EchoFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 * @Author @Chenxc
 * @Date 2022/2/22 10:09
 */
public class EchoFileServerHandler extends ChannelInboundHandlerAdapter {
    public static String sysProperties = "E:/" + File.separator + "config.properties";
    public static int serverPort = 19801;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("1111111");
    }




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        if (msg instanceof EchoFile) {
            EchoFile ef = (EchoFile) msg;
            if (ef.isCheck()) {
                String md5 = ef.getFile_md5();//文件md5
//                String md5 = ef.getFileName();//文件md5
                String path = "E:\\" + File.separator + md5;
                File file = new File(path);
                ef.setStarPos(file.length());
                ef.setCheck(false);
                ctx.writeAndFlush(ef);
            } else {
                long start = ef.getStarPos();
                byte[] bytes = ef.getBytes();
                long byteRead = ef.getReadSize();
                String md5 = ef.getFile_md5();//文件md5
//                String md5 = ef.getFileName();//文件md5

                String path = "E:\\" + File.separator + md5;
                File file = new File(path);
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(start);
                if (bytes.length != byteRead) {
                    byte[] remainBytes = new byte[(int) byteRead];
                    System.arraycopy(bytes, 0, remainBytes, 0, (int) byteRead);
                    randomAccessFile.write(remainBytes);
                } else {
                    randomAccessFile.write(bytes);
                }
                start = start + byteRead;
                if (byteRead > 0) {
                    ef.setStarPos(start);
                    ctx.writeAndFlush(ef);
                    randomAccessFile.close();
                    if (start == ef.getFileSize()) {
//                        renameAndSend(ef, file);
                        System.out.println("fileuploadserver.EchoServerHandler.channelRead()-->" + "byteRead>0 but start == ef.getFileSize()");
                    }
                } else {
                    randomAccessFile.close();
                    ctx.close();
//                    renameAndSend(ef, file);
                    System.out.println("fileuploadserver.EchoServerHandler.channelRead()-->" + "byteRead<0 file close");
                }
            }

            msg = null;
        }
    }

    private void renameAndSend(EchoFile echoFile, File file) {
//        String new_name = getTimeName();
//        String prefix = md5.substring(md5.lastIndexOf(".") + 1);
        String newFileName = "E:/" + File.separator + echoFile.getFile_md5();
//        String newFileName = FileUtil.tempDirMeet + File.separator + echoFile.getFileName() ;
        System.out.println(".renameAndSend()-->"+file.getAbsolutePath());
        file.renameTo(new File(newFileName));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
        ctx.close();
    }

    public static String getTimeName() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int dayMonth = ca.get(Calendar.DAY_OF_MONTH);
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        int minute = ca.get(Calendar.MINUTE);
        int second = ca.get(Calendar.SECOND);
        int dayWeek = ca.get(Calendar.DAY_OF_WEEK);
        int date = ca.get(Calendar.DATE);
        System.out.println("year-->" + year + "month-->" + (month) + "dayMonth-->" + dayMonth + "hour-->"
                + hour + "minute-->" + minute + "second-->" + second + "dayWeek-->" + dayWeek + "date-->" + date);
        return year + "_" + (month + 1) + "_" + dayMonth + "_" + hour + "_" + minute + "_" + second;
    }
}
