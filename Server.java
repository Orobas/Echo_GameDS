import java.rmi.*;

public interface Server extends Remote {
	void gamestart(int turnnumber, int gameid, boolean[] gameinprogress) throws RemoteException;
	void turnstart(int turnnumber) throws RemoteException;
	void endturn(int turnnumber) throws RemoteException;
	void playerjoins(String playername) throws RemoteException;
	void lobbychat(String playername, String message) throws RemoteException;
}
