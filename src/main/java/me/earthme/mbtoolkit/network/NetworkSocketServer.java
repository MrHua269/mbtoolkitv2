package me.earthme.mbtoolkit.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import me.earthme.mbtoolkit.network.handle.NettyServerHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class NetworkSocketServer {
    private static final Logger logger = LogManager.getLogger();
    private final NioEventLoopGroup currentLoopGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public void start(InetSocketAddress address){
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(this.currentLoopGroup,this.eventExecutors)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        logger.info("Connection incoming:{}",ch);
                        ch.pipeline()
                                .addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(new NettyServerHandler());
                    }
                });
        try {
            this.channelFuture = this.serverBootstrap.bind(address).sync();
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.info("Server bind on : {}:{}",address.getHostName(),address.getPort());
    }

    public void shutdown(){
        logger.info("Shutting down server");
        this.channelFuture.channel().close();
        this.currentLoopGroup.shutdownGracefully();
        this.eventExecutors.shutdownGracefully();
    }
}
