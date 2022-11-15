package com.chen.netty.Nio.MultipleChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务器
 */
public class GroupchatServer {
    //定义属性
    //选择器
    private Selector selector;
    //服务器通道
    private ServerSocketChannel listenChannel;
    //端口号
    private static final int PORT = 6667;

    //构造器，初始化
    public GroupchatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));
            //阻塞模式 非阻塞
            listenChannel.configureBlocking(false);
            //将listenChannel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){

        try {
            //循环
            while (true) {
                //一直阻塞，没有超时，只有有连接
                int count = selector.select();

                if(count > 0){
                    //监听到了，有事件处理
                    //得到SelectionKey集合，并遍历
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    //得到迭代器
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey key = selectionKeyIterator.next();

                        //监听到accept（新连接）
                        if(key.isAcceptable()){
                            //注册到选择器
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + "上线啦");
                        }

                        if(key.isReadable()){//通道发送read事件，即通道是可读的状态
                            //处理读（专门写方法实现）
                            //TODO
                            readData(key);
                        }
                        //当前的key删除，防止重复处理
                        selectionKeyIterator.remove();
                    }
                }else {
                    System.out.println("等待中......");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //异常处理
            System.out.println("网络发生异常，请检查");
        }
    }

    //处理读取客户端信息
    private void readData(SelectionKey key) {
        //渠道相关联的channel
        SocketChannel channel = null;

        try {
            //得到channel
            channel = (SocketChannel)key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //将通道的信息读到buffer
            int count = channel.read(buffer);

            if(count > 0){
                String msg = new String(buffer.array());
                System.out.println("from " + channel.getRemoteAddress() + ": " + msg);
                //向其他的客户端转发信息（去掉自己）
                sendInfoToOtherClient(msg,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发信息中...");
        //遍历所有注册到selector上的SocketChannel，并排处自己
        for(SelectionKey key :selector.keys()){
            //通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupchatServer groupchatServer = new GroupchatServer();
        groupchatServer.listen();
    }

}
