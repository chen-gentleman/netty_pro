package com.chen.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    //服务器成功接受到请求时 Netty的NIO线程会调用channelRead方法，
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)   throws Exception{
        //以静默方式丢弃接受的数据
        //((ByteBuf)msg).release();
        //ByteBuf类似于JDK中的java.nio.ByteBuffer对象，不过它提供了更加强大和灵活的功能。
        ByteBuf buf=(ByteBuf) msg;
        byte[] req=new byte[buf.readableBytes()];
        //读入缓冲区
        buf.readBytes(req);
        String body=new String(req, "UTF-8");
        System.out.println("The time server receive order : "+body);
        String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body) ? new
                Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        ByteBuf resp= Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
         * ctx.flush();将消息发送队列中的消息写入到SocketChannel中发送给对方。
         * 从性能角度考虑，为了防止频繁地唤醒Selector进行消息发送，Netty的write方法并不直接将消息写入SocketChannel中，
         * 调用write方法只是把待发送的消息放到发送缓冲数组中，再通过调用flush方法，将发送缓冲区中的消息全部写到SocketChannel中。
         */
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      //出现异常时关闭链接
        cause.printStackTrace();
        ctx.close();
    }
}
