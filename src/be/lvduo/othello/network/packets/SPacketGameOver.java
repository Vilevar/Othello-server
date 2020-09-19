package be.lvduo.othello.network.packets;

import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketGameOver implements Packet<ICPacketHandler> {

	private boolean b;
	
	public SPacketGameOver() {}
	public SPacketGameOver(boolean win) {
		this.b = win;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(this.b);
		
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		handler.gameOver(buf.readBoolean());
		
	}

}
