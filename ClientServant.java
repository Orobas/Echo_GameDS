import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ClientServant extends UnicastRemoteObject implements Client {
	private List<Integer> gamelist = new ArrayList<Integer>();
	HashMap<Integer,ArrayList<Pair>> playersingames = new HashMap<Integer, ArrayList<Pair>>();
	private List<Pair> playerlist = new ArrayList<Pair>();
	private int numofplayerstotal = 0;
	private int numofgamestotal = 0;
	private int currentgames = 0;
	private HashMap<Integer,ArrayList<Server>> gserver = new HashMap<Integer,ArrayList<Server>>();
	private int maxgames = 10;
	private boolean[] games = new boolean[maxgames];
	private boolean[] gameinprogress = new boolean[maxgames];
	public ClientServant() throws RemoteException{
		
	}
	public void sendMove(int gamenum, int turnnum, int playerid) throws RemoteException {
		
	}
	public String joingame(int gamenum, int playerid, Server gameserver) throws RemoteException {
		if (gameinprogress[gamenum]==true) return "INPROGRESS";
		if (games[gamenum]==false) return "NOGAME";
		ArrayList<Pair> plist = playersingames.get(gamenum);
		ArrayList<Server> serverlist = new ArrayList<Server>();
		String curname = playerlist.get(getPlayerIndex(playerid)).name;
		Pair newpair = new Pair(curname,playerid);
		serverlist = gserver.get(gamenum);
		serverlist.add(gameserver);
		plist.add(newpair);
		for(Server ser: gserver.get(gamenum)){
			ser.playerjoins(curname);
		}
		gserver.put(gamenum,serverlist);
		playersingames.put(gamenum,plist);
		System.out.println(curname + " joined game " + gamenum);
		String sendback = "Host: ";
		for(Pair p: plist){
			sendback = sendback + p.name + "\n";
		}
		return sendback;
	}
	
	public int register(String playername) throws RemoteException {
		for(Pair p: playerlist){
			if(playername.equals(p.name)) return -1;
		}
		numofplayerstotal++;
		Pair newplayer = new Pair(playername,numofplayerstotal);
		System.out.println(newplayer.name() + " " + newplayer.id() + " registered.");
		playerlist.add(newplayer);
		return numofplayerstotal;
	}
	
	public int unregister(int playerid) throws RemoteException {
		int rIndex = -1;
		for(Pair p:playerlist){
			if(playerid==p.id){
				rIndex = playerlist.indexOf(p);
			}
		}
		playerlist.remove(rIndex);
		if(rIndex>=0) return 0;
		return 1;
	}
	
	public int startgame(int gameid, int playerid) throws RemoteException {
		gameinprogress[gameid]=true;
		for (Server ser: gserver.get(gameid)){
			ser.gamestart(0,gameid,gameinprogress);
		}
		return 0;
	}
	
	public int leavegame(int gameid,int playerid, Server gameserver) throws RemoteException {
		ArrayList<Pair> plist = playersingames.get(gameid);
		ArrayList<Server> slist = gserver.get(gameid);
		String playername="";
		int rIndex = -1;
		for(Pair p:plist){
			if(playerid==p.id){
				playername = p.name;
				rIndex = plist.indexOf(p);
			}
		}
		plist.remove(rIndex);
		for (Server ser: slist){
			if(gameserver.equals(ser)){
				rIndex = slist.indexOf(ser);
			}
		}
		slist.remove(rIndex);
		if(plist.size()==0){
			playersingames.remove(gameid);
		}
		else{
			playersingames.put(gameid,plist);
		}
		if(slist.size()==0){
			gserver.remove(gameid);
		}
		else{
			gserver.put(gameid,slist);
		}
		lobbychat(gameid, playerid, gameserver, " has left the lobby.");
		if(rIndex>=0) return 0;
		return 1;
	}
	
	public int creategame(int playerid, Server gameserver) throws RemoteException {
		int gamecreated=0;
		numofgamestotal++;
		gamelist.add(numofgamestotal++);
		for(int x=0;x<maxgames;x++){
			if (games[x]==false){
				games[x]=true;
				int ind = getPlayerIndex(playerid);
				ArrayList<Pair> newplayerlist = new ArrayList<Pair>();
				ArrayList<Server> serverlist = new ArrayList<Server>();
				serverlist.add(gameserver);
				newplayerlist.add(playerlist.get(ind));
				playersingames.put(x,newplayerlist);
				gamecreated=x;
				gserver.put(x,serverlist);
				String playername="";
				for(Pair p:playerlist){
					if(playerid==p.id) playername=p.name;
				}
				System.out.println("Game created: " + x + " by " + playername);
				break;
			}
		}
		
		//playerlist.add(playername);
		currentgames++;
		return gamecreated;
	}
	
	public String listplayers() {
		String returnstring = "Players:\n";		
		for(Pair p: playerlist){
			returnstring = (returnstring+"Id: " + p.id + " Name: " + p.name + "\n");
		}
		return returnstring;
	}
	
	public String listplayerslobby(int gameid) {
		String returnstring = ("Players:\n in Game " + gameid + ":\n");
		for(Pair p: playersingames.get(gameid)){
			returnstring = (returnstring + "Id: " + p.id + " Name: " + p.name + "\n"); 
		}
		return returnstring;
	}
	
	public void lobbychat(int gamenum, int playerid, Server myserver, String message) throws RemoteException{
		String pname="";
		for (Pair p: playerlist){
			if (playerid==p.id) pname = p.name;
		}
		for (Server ser: gserver.get(gamenum)){
			if(!ser.equals(myserver)) ser.lobbychat(pname, message);
		}
	}
	
	public HashMap<Integer,List<String>> listgames() throws RemoteException {
		HashMap<Integer,List<String>> listgame = new HashMap<Integer,List<String>>();
		for (int x=0;x<maxgames;x++){
			if (games[x]==true){
				ArrayList<String> plist = new ArrayList<String>();
				System.out.println("Game " + Integer.toString(x));
				for (Pair p: playersingames.get(x)){
					System.out.println(p.name);
					plist.add(p.name());
				}
				listgame.put(x,plist);
			}
		}
		return listgame;
	}
	
	public void debug(int playerid) {
		int ind = getPlayerIndex(playerid);
		for(Pair p: playerlist){
			if (playerid==p.id()){
				System.out.println("Selected:" + p.name());
			}
			System.out.println("Id: " + p.id() + " Name: " + p.name());
		}
		for(Integer key: playersingames.keySet()){
			System.out.println("Gameid: " + key);
			for(Pair p: playersingames.get(key)){
				System.out.println("Players: " + p.id + " " + p.name);
			}
		}
	}
	
	public int getPlayerIndex(int pid) {
		for(int x=0;x<playerlist.size();x++){
			if (playerlist.get(x).id == pid)	return x;
		}
		return 0;
	}
	
	//Custom class to hold pair of values
	private class Pair{
	private final String name;
	private final Integer id;
	private Pair(String key, Integer value){
		name = key;
		id = value;
	}
	
	private String name()	{ return name; }
	private Integer id() 	{ return id; }

}
}

