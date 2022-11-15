package com.chen.netty.nettyGroupChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupChatClientHandel extends SimpleChannelInboundHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        // Send the first message if this handler is a client-side handler.
        System.out.println("Connected to: " + ctx.channel().remoteAddress());
        //MessageDataDecoder.decoder(ProtocolData.CMD_ONLINE, "Connected to: " + ctx.channel().remoteAddress());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString().trim());
    }
}
