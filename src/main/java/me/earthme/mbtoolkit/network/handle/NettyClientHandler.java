package me.earthme.mbtoolkit.network.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.earthme.mbtoolkit.network.packet.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public class NettyClientHandler extends SimpleChannelInboundHandler<Message<NettyClientHandler>> {
    public static final Logger logger = LogManager.getLogger();
    private Channel channel;

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<NettyClientHandler> msg) {
        msg.process(this);
    }
}
