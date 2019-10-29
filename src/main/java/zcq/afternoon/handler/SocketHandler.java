package zcq.afternoon.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhengchuqin
 * @version 1.0
 * @since 2019/10/10
 */
@Component
public class SocketHandler extends ChannelInboundHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        LOGGER.info("channel read :{}", obj);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info("exception caught :{}", cause.getMessage());
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        LOGGER.info("userEventTriggered :{}", evt);
    }
}
