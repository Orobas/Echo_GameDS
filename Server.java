import java.rmi.*;

public interface Server extends Remote {
	void gamestart(int turnnumber, int gameid, boolean[] gameinprogress) throws RemoteException;
	void turnstart(int turnnumber) throws RemoteException;
	void endturn(int turnnumber) throws RemoteException;
}

/*
import java.rmi.*;

public interface WhiteboardCallback extends Remote {
void callback(int version) throws RemoteException;
}
*/