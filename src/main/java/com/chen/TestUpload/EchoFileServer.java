package com.chen.TestUpload;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @Author @Chenxc
 * @Date 2022/2/22 10:08
 */
public class EchoFileServer {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void startServer() throws Exception {
        stopServer();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
                    ch.pipeline().addLast(new EchoFileServerHandler());
                    ch.pipeline().addLast(new ObjectEncoder());
                }
            });
            ChannelFuture f = b.bind(10130).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    //    public static void main(String[] args) {
//        File sourceFileDir = new File(EchoServerHandler.file_dir);
//        if (!sourceFileDir.exists()) {
//            sourceFileDir.mkdirs();
//        }
//        startFileServer();
//    }
    public void stopServer(){
        if(null != bossGroup && workerGroup != null){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }else{
            System.out.println("工作组为空");
        }
    }
    public boolean isWorking(){
        return bossGroup.isShutdown();
    }

    public static void main(String[] args) {
        try {
            new EchoFileServer().startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
