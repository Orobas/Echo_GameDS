import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
//Server side of the game
public class Echo_GameServer {
	public static void main(String args[]) {
		try{
			//Open the registry
			System.setProperty("java.rmi.server.hostname", "localhost");
			ClientServant gameclient = new ClientServant();
			Registry registry;
			try{
				registry = LocateRegistry.getRegistry();
				registry.list();
			}catch (RemoteException e){
				registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
				System.out.println("RMI registry created at port " + Registry.REGISTRY_PORT);
			}
			registry.rebind("Client", gameclient);
			System.out.println("Echo_Game server open");
		}catch(Exception e){
			System.err.println("Server exception: "  + e.toString());
			e.printStackTrace();
		}
	}
}
