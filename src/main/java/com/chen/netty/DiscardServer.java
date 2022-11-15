package com.chen.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private int port;

    public DiscardServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        /*
         * 配置服务端的NIO线程组，它包含了一组NIO线程，专门用于网络事件的处理，实际上它们就是Reactor线程组。
         * 这里创建两个的原因：一个用于服务端接受客户端的连接，
         * 另一个用于进行SocketChannel的网络读写。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //ServerBootstrap对象，Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    //使用NioServerSocketChannel类，该类用于实例化新的通道以接受传入连接。
                    .channel(NioServerSocketChannel.class)
                    /*
                     * 绑定I/O事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的handler类，
                     * 主要用于处理网络I/O事件，例如：记录日志、对消息进行编解码等。
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //ChannelInitializer是一个特殊的处理程序，用于帮助用户配置新的通道。
                        // 很可能要通过添加一些处理程序(例如DiscardServerHandler)来配置新通道的ChannelPipeline来实现您的网络应用程序。
                        // 随着应用程序变得复杂，可能会向管道中添加更多处理程序(  ch.pipeline().addLast(new DiscardServerHandler()).addLast(new DiscardServerHandler()))，
                        // 并最终将此匿名类提取到顶级类中。
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    //option()用于接受传入连接的NioServerSocketChannel。
                    // childOption()用于由父ServerChannel接受的通道，在这个示例中为NioServerSocketChannel。
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            /*
             * 绑定端口，同步等待成功（调用它的bind方法绑定监听端口，随后，调用它的同步阻塞方法sync等待绑定操作完成。
             * 完成之后Netty会返回一个ChannelFuture,它的功能类似于JDK的java.util.concurrent.Future，
             * 主要用于异步操作的通知回调。）
             */
            ChannelFuture f = b.bind(port).sync();
            //等待服务端监听端口关闭（使用f.channel().closeFuture().sync()方法进行阻塞，等待服务端链路关闭之后main函数才退出。）
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            //优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args)throws Exception {
        int port;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }else {
            // 采用8080
            port = 8080;
        }
        new DiscardServer(port).run();
    }
}
