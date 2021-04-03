package be.lvduo.othello.network;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NetworkManager extends SimpleChannelInboundHandler<ByteBuf> {
	
	private Channel channel;
	private final ArrayList<Class<? extends Packet<?>>> packets;
	private final NetworkSystem system;
	private boolean closed;
	private ISPacketHandler handler;
	
	public NetworkManager(NetworkSystem system) {
		this.packets = system.getPackets();
		this.system = system;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Caught exception "+cause.toString());
	//	cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.channel = ctx.channel();
		System.out.println("New Client "+channel.remoteAddress());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("Channel "+handler.getUser().getNickname()+"["+channel.remoteAddress()+"] disconnected");
		if(handler != null) {
			system.getServer().userDisconnect(handler.getUser());
		}
	}
	
	public NetworkSystem getNetworkSystem() {
		return system;
	}
	
	public void setHandler(ISPacketHandler handler) {
		this.handler = handler;
	}
	
	public ISPacketHandler getHandler() {
		return handler;
	}
	
	private ByteBuf writePacket(Packet<?> packet) throws Exception {
		if(closed)
			throw new IllegalStateException("Channel closed.");
		int index = packets.indexOf(packet.getClass());
		if(index == -1)
			throw new IllegalArgumentException("The packet of type "+packet.getClass()+" is not registred.");
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(index);
		packet.write(buf);
		return buf;
	}
	
	public void sendPacket(Packet<?> packet) throws Exception {
		this.channel.writeAndFlush(this.writePacket(packet)).sync().addListener(new GenericFutureListener<Future<? super Void>>() {

			@Override
			public void operationComplete(Future<? super Void> f) throws Exception {
				System.out.println("Packet type "+packet.getClass().getSimpleName()+" sent to "+handler.getUser().getNickname()+" "+f.isSuccess());
			}
			
		});
		System.out.println("Send packet type "+packet.getClass().getSimpleName()+" to "+handler.getUser().getNickname());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		super.channelRead(arg0, arg1);
		System.out.println("Reading something");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		if(closed)
			throw new IllegalStateException("Channel closed.");
		int index = buf.readInt();
	//	if(index == 0) {
	//		this.close(false);
	//	} else {
		Packet<ISPacketHandler> packet = (Packet<ISPacketHandler>) packets.get(index).newInstance();
		System.out.println("Get packet type "+packets.get(index).getSimpleName()+" from "+handler.getUser().getNickname());
		packet.read(buf, handler);
	//	}
	}
	
	public void close() throws Exception {
		this.close(true);
	}
	
	private void close(boolean packet) throws Exception {
		if(closed) 
			throw new IllegalStateException("Channel closed.");
//		if(packet)
//			this.channel.writeAndFlush(this.writePacket(packets.get(0).newInstance()), channel.voidPromise());
		this.channel.closeFuture().syncUninterruptibly();
		closed = true;
	}
}