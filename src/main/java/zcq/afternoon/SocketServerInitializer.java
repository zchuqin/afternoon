package zcq.afternoon;
/**
 * ***************************************************************************
 * Copyright (C) 2017 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 * ****************************************************************************
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zcq.afternoon.handler.SocketHandler;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengchuqin
 * @version 1.0
 * @since 2019/10/10
 */
@Component
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final int coreSize =16;

    @Autowired
    private SocketHandler socketHandler;

    private static EventExecutorGroup group;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 以("\n")为结尾分割的 解码器
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        //添加心跳机制10s没有接受到客户端发送数据,10s没有向客户端发送数据,5s所有类型的超时时间
        pipeline.addLast(new IdleStateHandler(10, 10, 5, TimeUnit.SECONDS));
        //业务处理器
        pipeline.addLast(group, socketHandler);
    }

    @PostConstruct
    void init() {
        group = new DefaultEventExecutorGroup(coreSize, runnable1 -> new Thread("socketHandler"));
    }


}
