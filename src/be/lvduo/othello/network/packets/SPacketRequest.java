package be.lvduo.othello.network.packets;

import com.google.common.base.Charsets;

import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketRequest implements Packet<ICPacketHandler> {
	
	private String asker;
	
	public SPacketRequest() {}
	public SPacketRequest(String asker) {
		this.asker = asker;
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		byte[] name = this.asker.getBytes(Charsets.UTF_8);
		buf.writeInt(name.length);
		for(byte b : name) {
			buf.writeByte(b);
		}
		
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		byte[] name = new byte[buf.readInt()];
		for(int i = 0; i < name.length; i++) {
			name[i] = buf.readByte();
		}
		handler.getRequest(new String(name, Charsets.UTF_8)); 
		
	}

}
