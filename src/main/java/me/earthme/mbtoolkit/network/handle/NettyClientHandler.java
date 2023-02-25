package me.earthme.mbtoolkit.network.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.earthme.mbtoolkit.backgroundlocking.BackgroundforceBackendThread;
import me.earthme.mbtoolkit.network.packet.s2c.S2CPacket;

public class NettyClientHandler extends SimpleChannelInboundHandler<S2CPacket> {
    private static final BackgroundforceBackendThread backgroundForceThread = new BackgroundforceBackendThread();

    public static void init(){
        backgroundForceThread.start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, S2CPacket msg) throws Exception {

    }
}