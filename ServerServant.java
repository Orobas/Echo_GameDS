import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ServerServant extends UnicastRemoteObject implements Server {
	private boolean[] games = new boolean[10];
	private int[] turntimers = new int[10];
	private int[] turnn = new int[10];
	Timer timer = new Timer();
	boolean time = false;
	private int timeduration = 0;
	public ServerServant(Client gameclient) throws RemoteException{

	}
	public void gamestart(int turnnumber,int gameid,boolean[] gameinprogress) throws RemoteException {
		System.out.println("Game started");
		games = gameinprogress;
		turntimers[gameid]=5;
		turnn[gameid]=0;
		if (time==false){
			time=true;
			timer.schedule(new turntimer(), 1000,1000);
		}
	}
	public void turnstart(int turnnumber) throws RemoteException {
		
	}
	public void endturn(int turnnumber) throws RemoteException {
		
	}
	public void playerjoins(String playername) throws RemoteException {
		System.out.println(playername + " has joined the labby.\n");
	}
	public void lobbychat(String playername, String message) throws RemoteException {
		System.out.println(playername + message + "\n");
	}
	class turntimer extends TimerTask{
		public void run() {
			timeduration++;
			for(int x=0;x<10;x++){
				if (turntimers[x]==timeduration){
					turnn[x]++;
					System.out.println("Turn over");
					System.out.println("Start of turn #" + turnn[x]);
					turntimers[x] = turntimers[x]+5;
				}
			}
		}
	}
	
}
