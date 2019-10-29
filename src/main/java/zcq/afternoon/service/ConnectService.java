package zcq.afternoon.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zcq.afternoon.SocketServerInitializer;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengchuqin
 * @version 1.0
 * @since 2019/10/10
 */
@Service
public class ConnectService {

    private final static Logger logger = LoggerFactory.getLogger(ConnectService.class);

    @Autowired
    private SocketServerInitializer initializer;

    private static Bootstrap bootstrap;

    public static Channel channel;

    @Value("8555")
    private int port;
    @Value("10.10.15.233")
    private String host;
    private static final EventLoopGroup group = new NioEventLoopGroup();
    /**
     * 初始化bootstrap
     */
    public void init() {

        bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(initializer);
        //设置分配bytebuf时使用内存池
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }
    /**
     * 建立连接 如果连接服务端失败，每5秒尝试一次
     */
    public void connect() {
        try {
            bootstrap.remoteAddress(new InetSocketAddress(host, port));
            ChannelFuture future = bootstrap.connect().sync();
            //推送连接状态和设备编号
            future.addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (future.isSuccess()) {
                        channel = future.channel();
                    } else {
                        future.cause().printStackTrace();
                    }
//
                }
            });
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (channel == null || !channel.isActive()) {
                //连接失败 每5s重试一次
                ScheduledThreadPoolExecutor schdule = new ScheduledThreadPoolExecutor(1);
                schdule.schedule(new Runnable() {
                    @Override
                    public void run() {
                        // 重新连接
                        connect();
                    }
                }, 5, TimeUnit.SECONDS);
            }
        }
    }
}
