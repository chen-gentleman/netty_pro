package com.chen.netty.nettyGroupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.*;

public class GroupChatServerHandel extends SimpleChannelInboundHandler {
    public static List<Channel> channels = new ArrayList<>();

    //使用已hashmap管理
    public static Map<String,Channel> channelMap = new HashMap<>();

    //定义一个channle，管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    //新连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("GroupChatServerHandel处理新连接");
        Channel channel = ctx.channel();
        //将该呵护加入聊天的信息推送给其他在线的客户端'
        //该方法会将channelGroup中所有的channel遍历，并发送信息
        //我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + sdf.format(new Date()) + "\n");
        channelGroup.add(channel);
    }

    //断开连接，将xx客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size " + channelGroup.size());
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~");
    }


    //表示channel处于不活动状态，提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~");
    }


    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final Object msg) throws Exception {
        System.out.println("GroupChatServerHandel处理数据");
            //获取当前channel
        final Channel channel = ctx.channel();
        //这时我们遍历channelGroup，根据不同的情况，回送不同的消息 ch为channelGroup遍历出来的channel
       /* channelGroup.forEach(ch ->{
            if (channel != ch){//不是当前的channel，转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息：" + msg + "\n");
            }else {
                ch.writeAndFlush("[我]发生了消息" + msg + "\n");
            }
        });*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
