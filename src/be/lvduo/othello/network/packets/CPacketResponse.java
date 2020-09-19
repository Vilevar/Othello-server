package be.lvduo.othello.network.packets;

import be.lvduo.othello.network.ISPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketResponse implements Packet<ISPacketHandler> {
	
	private boolean b;
	
	public CPacketResponse() {}
	public CPacketResponse(boolean response) {
		this.b = response;
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(this.b);
		
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		handler.getResponse(buf.readBoolean());
		
	}

}
