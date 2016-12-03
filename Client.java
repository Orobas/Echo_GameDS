import java.rmi.*;
import java.util.Vector;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public interface Client extends Remote {
	void sendMove(int gamenum, int turnnum, int playerid) throws RemoteException;
	String joingame(int gameid, int playerid, Server gameserver) throws RemoteException;
	int register(String playername) throws RemoteException;
	int unregister(int playerid) throws RemoteException;
	int startgame(int gamenum, int playerid) throws RemoteException;
	int leavegame(int gameid, int playerid, Server gameserver) throws RemoteException;
	int creategame(int playerid, Server gameserver) throws RemoteException;
	String listplayers() throws RemoteException;
	String listplayerslobby(int gameid) throws RemoteException;
	void lobbychat(int gamenum, int playerid, Server myserver, String message) throws RemoteException;
	HashMap<Integer,List<String>> listgames() throws RemoteException;
	void debug(int playerid) throws RemoteException;
}
