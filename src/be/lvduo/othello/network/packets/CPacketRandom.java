package be.lvduo.othello.network.packets;

import be.lvduo.othello.network.ISPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketRandom implements Packet<ISPacketHandler> {
	
	private boolean b;
	public CPacketRandom() {}
	public CPacketRandom(boolean randomMode) {
		this.b = randomMode;
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(this.b);
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		handler.setRandomMode(buf.readBoolean());
	}

}
