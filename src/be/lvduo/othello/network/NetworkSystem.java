package be.lvduo.othello.network;

import java.util.ArrayList;

import be.lvduo.othello.Server;
import be.lvduo.othello.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetworkSystem {

	private final ArrayList<Class<? extends Packet<?>>> packets = new ArrayList<>();
	
	private final Server server;
	
	public NetworkSystem(Server server) {
		this.server = server;
	}
	
	public void start(String ip, int port) {
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		try {
			new ServerBootstrap().group(loopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
					@Override
					public void initChannel(Channel channel) throws Exception {
						NetworkManager manager = new NetworkManager(NetworkSystem.this);
						channel.pipeline().addLast("timeout", new ReadTimeoutHandler(600));
						channel.pipeline().addLast(manager);
					//	networks.add(manager);
						User user = new User(manager);
						manager.setHandler(new ServerPacketHandler(user, server));
						
					}
				}).bind(ip, port).sync().channel().closeFuture().syncUninterruptibly();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			loopGroup.shutdownGracefully();
		}
	}
	
	public void registerPacketType(Class<? extends Packet<?>> packetType) {
		packets.add(packetType);
	}
	
	public ArrayList<Class<? extends Packet<?>>> getPackets() {
		return new ArrayList<>(packets);
	}
	
	public Server getServer() {
		return server;
	}
	
}

