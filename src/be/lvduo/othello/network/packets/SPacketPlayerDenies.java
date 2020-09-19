package be.lvduo.othello.network.packets;

import com.google.common.base.Charsets;

import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketPlayerDenies implements Packet<ICPacketHandler> {

	
	private String name;
	
	public SPacketPlayerDenies() {}
	public SPacketPlayerDenies(String name) {
		
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		byte[] name = this.name.getBytes(Charsets.UTF_8);
		buf.writeInt(name.length);
		for(byte b : name) {
			buf.writeByte(b);
		}
		
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		byte[] name = new byte[buf.readByte()];
		for(int i = 0; i < name.length; i++) {
			name[i] = buf.readByte();
		}
		handler.getResponse(new String(name, Charsets.UTF_8));
		
	}
	
}
