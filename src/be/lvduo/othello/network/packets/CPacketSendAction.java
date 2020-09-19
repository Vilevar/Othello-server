package be.lvduo.othello.network.packets;

import java.awt.Point;

import be.lvduo.othello.network.ISPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketSendAction implements Packet<ISPacketHandler> {

	private int x,y;
	public CPacketSendAction() {}
	public CPacketSendAction(Point shot) {
		this(shot.x, shot.y);
	}
	public CPacketSendAction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		handler.hasPlaying(buf.readInt(), buf.readInt());
		
	}

}
