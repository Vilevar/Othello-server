package be.lvduo.othello.network;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Game;
import be.lvduo.othello.Server;
import be.lvduo.othello.User;
import be.lvduo.othello.network.packets.SPacketName;
import be.lvduo.othello.network.packets.SPacketPlayerDenies;
import be.lvduo.othello.network.packets.SPacketPlayerList;
import be.lvduo.othello.network.packets.SPacketRequest;
import be.lvduo.othello.network.packets.SPacketSendAction;

public class ServerPacketHandler implements ISPacketHandler {

	private User user;
	private Server server;
	
	public ServerPacketHandler(User user, Server server) {
		this.user = user;
		this.server = server;
	}
	
	@Override
	public User getUser() {
		return this.user;
	}
	
	@Override
	public void testName(String name) throws Exception {
		if(this.user.getNickname() != null)
			user.getManager().sendPacket(new SPacketName(false));
		for(User p : server.getUsers()) {
			if(name.equals(p.getNickname())) {
				user.getManager().sendPacket(new SPacketName(false));
				return;
			}
		}
		user.setNickname(name);
		user.getManager().sendPacket(new SPacketName(true));
		
	}

	@Override
	public void clientIsWaiting(boolean isWaiting) {
		if(!server.getUsers().contains(user)) {
			user.setStatus(isWaiting);
			server.getUsers().add(user);
		} else {
			for(User u : server.getUsers()) {
				if(u.equals(this.user)) {
					u.setStatus(isWaiting);
				}
			}
		}
		
		for(User u : server.getUsers()) {
			try {
				u.getManager().sendPacket(new SPacketPlayerList(server.getUsers()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getRequest(String name) {
		if(server.getProposals().containsValue(user)) {
			for(User u : server.getProposals().values()) {
				User asker = server.getProposals().get(u);
				if(asker.equals(user)) {
					server.getProposals().remove(u);
					break;
				}
			}
		}
		
		User opponent = null;
		for(User u : server.getUsers()) {
			if(!u.isInGame())
				if(u.getNickname().equalsIgnoreCase(name)) {
					opponent = u;
					break;
				}
		}
		try {
			opponent.getManager().sendPacket(new SPacketRequest(user.getNickname()));
		
			server.getProposals().put(opponent, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setRandomMode(boolean randomMode) {
		server.randomMode(randomMode, user);
	}

	@Override
	public void startWithAI(boolean AIMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hasPlaying(int x, int y) {
		System.out.println("Receive action ["+user+"] ("+x+"; "+y+")");
		for(Game game : server.getGames()) {
			User current = game.getUser(game.getCurrent());
			System.out.println("Test game "+game+" : u1"+game.u1+" u2"+game.u2+" current"+current);
			if((game.u1.equals(user) || game.u2.equals(user)) && current.equals(user)) {
				System.out.println("Good player");
				Point pt = game.getUser(game.getPlayer(1)).equals(user) ? new Point(x, (Board.HEIGHT-1) - y) : new Point(x, y);
				game.play(pt);
				System.out.println("Played");
				try {
					game.getOther(user).getManager().sendPacket(new SPacketSendAction(x, y));
					System.out.println("Sent");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
	}

	@Override
	public void getResponse(boolean response) {
		if(server.getProposals().containsKey(user))
			if(response) {
				server.newGame(user, server.getProposals().get(user));
			} else {
				try {
					server.getProposals().remove(user).getManager().sendPacket(new SPacketPlayerDenies(user.getNickname()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
				
	}
	
	
/*	
	@Override
	public void clientIsWaiting(boolean first) throws Exception {
		player.setState(State.WAITING);
		
		server.WaitingList(player, true);
		
		if(first) {
			ArrayList<Player> p = new ArrayList<>(server.getPlayers());
			p.remove(player);
			for(Player pls : p) {
				player.getNetworkManager().sendPacket(new SPacketClientIsWaiting(pls.getName(), true));
			} 
		}
	}
*/
}

