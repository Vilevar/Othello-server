package be.lvduo.othello.network.packets;

import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketName implements Packet<ICPacketHandler> {

	private boolean good;
	
	public SPacketName() {}
	public SPacketName(boolean good) {
		this.good = good;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(good);
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		handler.getStateOfName(buf.readBoolean());
	}

}
