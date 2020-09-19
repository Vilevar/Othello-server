package be.lvduo.othello.network.packets;

import com.google.common.base.Charsets;

import be.lvduo.othello.network.ISPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketName implements Packet<ISPacketHandler> {
	
	private String name;
	
	public CPacketName() {}
	public CPacketName(String name) {
		this.name = name;
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		byte[] name = this.name.getBytes(Charsets.UTF_8);
		buf.writeInt(name.length);
		for(byte b : name)
			buf.writeByte(b);
		
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		byte[] name = new byte[buf.readInt()];
		for(int i = 0; i < name.length; i++)
			name[i] = buf.readByte();
		handler.testName(new String(name, Charsets.UTF_8));
		
	}
	
	

}
