package com.chen.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

//public class TimeClientHandler extends ChannelHandlerAdapter{//已摒弃
public class DiscardClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger=Logger.getLogger(DiscardClientHandler.class.getName());
    private final ByteBuf firstMessage;

    public DiscardClientHandler(){
        byte[] req="QUERY TIME ORDER".getBytes();
        firstMessage= Unpooled.buffer(req.length);
        //写入缓冲区
        firstMessage.writeBytes(req);
    }

    /**
     * 当客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用channelActive方法，
     * 发送查询时间的指令给服务端。
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将请求信息发送给服务端
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端返回应答消息时调用channelRead方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf buf=(ByteBuf) msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req, "UTF-8");
        System.out.println("Now is :"+body);
    }

    /**
     * 发生异常是，打印异常日志，释放客户端资源。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        //释放资源
        logger.warning("Unexpected exception from downstream : "+cause.getMessage());
        ctx.close();
    }
}
