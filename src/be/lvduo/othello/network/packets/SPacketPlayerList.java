package be.lvduo.othello.network.packets;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;

import be.lvduo.othello.User;
import be.lvduo.othello.network.ICPacketHandler;
import be.lvduo.othello.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketPlayerList implements Packet<ICPacketHandler> {
	
	private List<User> list;

	public SPacketPlayerList() {}
	public SPacketPlayerList(List<User> list) {
		this.list = list;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(list.size());
		int i = 0;
		for(User a : this.list) {
			
			byte[] name = a.getNickname().getBytes(Charsets.UTF_8);
			buf.writeInt(name.length);
			for(byte b : name) {
				buf.writeByte(b);
			}
			
			buf.writeInt(a.getVictories());
			buf.writeInt(a.getDefeats());
			buf.writeBoolean(a.isInGame());
			
			if((i+=1) == 50)
				break;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		int objs = buf.readInt();
		List<User> users = new ArrayList<>();
		for(int i = 1; i <= objs; i++) {
			
			byte[] n = new byte[buf.readInt()];
			for(int j = 0; j < n.length; j++) {
				n[j] = buf.readByte();
			}
			
			String name = new String(n, Charsets.UTF_8);
			int victories = buf.readInt();
			int defeats = buf.readInt();
			boolean inGame = buf.readBoolean();
			
		//	users.add(new User(name, victories, defeats, inGame));
		}
		
		handler.getUsersList(users);
	}

	

}
