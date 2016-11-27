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
	int leavegame(int gameid, int playerid) throws RemoteException;
	int creategame(int playerid, Server gameserver) throws RemoteException;
	String listplayers() throws RemoteException;
	HashMap<Integer,List<String>> listgames() throws RemoteException;
	void debug(int playerid) throws RemoteException;
}
/*
import java.rmi.*;
import java.util.Vector;
import java.rmi.server.UnicastRemoteObject;

public interface ShapeList extends Remote {
	Shape newShape(GraphicalObject g) throws RemoteException;
	Vector<Shape> allShapes() throws RemoteException;
	int getVersion() throws RemoteException;
	int register(WhiteboardCallback callback) throws RemoteException;
	int unregister(int callbackId) throws RemoteException;
	
}
*/

