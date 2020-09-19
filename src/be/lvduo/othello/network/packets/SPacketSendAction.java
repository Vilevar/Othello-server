package be.lvduo.othello.network.packets;

import java.awt.Point;

import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketSendAction implements Packet<ICPacketHandler> {

	private int x,y;
	public SPacketSendAction() {}
	public SPacketSendAction(Point shot) {
		this(shot.x, shot.y);
	}
	public SPacketSendAction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		handler.hasPlaying(buf.readInt(), buf.readInt());
		
	}
	
}
