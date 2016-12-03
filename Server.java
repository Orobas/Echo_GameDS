import java.rmi.*;

//The remote interface for the Server
public interface Server extends Remote {
	//All are explained in ServerServant
	void gamestart(int turnnumber, int gameid, boolean[] gameinprogress) throws RemoteException;
	void turnstart(int turnnumber) throws RemoteException;
	void endturn(int turnnumber) throws RemoteException;
	void playerjoins(String playername) throws RemoteException;
	void lobbychat(String playername, String message) throws RemoteException;
}
