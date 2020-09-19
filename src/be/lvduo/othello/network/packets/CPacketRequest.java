package be.lvduo.othello.network.packets;

import com.google.common.base.Charsets;

import be.lvduo.othello.network.ISPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketRequest implements Packet<ISPacketHandler> {

	private String name;
	public CPacketRequest() {}
	public CPacketRequest(String name) {
		this.name = name;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		
		byte[] n = this.name.getBytes(Charsets.UTF_8);
		buf.writeInt(n.length);
		for(byte b : n) {
			buf.writeByte(b);
		}
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		byte[] n = new byte[buf.readInt()];
		for (int i = 0; i < n.length; i++) {
			n[i] = buf.readByte();
		}
		String name = new String(n, Charsets.UTF_8);
		handler.getRequest(name);
	}

}
