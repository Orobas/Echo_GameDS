import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Echo_Game {
	public static void main(String args[]) throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		String host = "localhost";
		int port = 1099;
		int playerid = 0;
		Scanner console = new Scanner(System.in);
		try {
			Registry registry = LocateRegistry.getRegistry(host,port);
			Client gameclient = (Client) registry.lookup("Client");
			ServerServant gameserver = new ServerServant(gameclient);
			System.out.println("Welcome to Echo!\n");
			System.out.println("Enter your playername: ");
			String consoleline;
			while(true){
				consoleline = console.nextLine();
				playerid = gameclient.register(consoleline);
				if (playerid!=0) break;
				System.out.println("Error occured, enter another: ");
			}
			System.out.println("\nWelcome to Echo " + consoleline + "!\n");
			mainscreen(playerid, gameserver, gameclient);
			System.out.println("Should of transfered");
		}catch(RemoteException e) {System.out.println("Remote Exception: " + e.getMessage());
		}catch(Exception e) {System.out.println("Registry: " + e.getMessage());
		}
		System.out.println("Connected");
	}
	public static void mainscreen(int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException {
		System.out.println("Commands:\ngames : Lists all available rooms with number of players in the room\ncreate : Create a room\njoin <gameid> : Join the room of the specified id\nwho : List all players online\nquit : Quit the game\nhelp : List all commands");
		Scanner console = new Scanner(System.in);
		int gameid = 0;
			while (true){
				String consoleline;
				System.out.println("\nEnter a command: ");
				consoleline = console.nextLine();
				String[] consolesplit = consoleline.split(" ");
				if(consolesplit[0].equalsIgnoreCase("/games")){
					HashMap<Integer,List<String>> gamelist = gameclient.listgames();
					for(Integer key: gamelist.keySet()){
						System.out.println("Game id: " + Integer.toString(key));
						System.out.println("Players:");
						for(String pname: gamelist.get(key)){
							System.out.println(pname);
						}
					System.out.println("\n");	
					}
				}
				if(consolesplit[0].equalsIgnoreCase("/create")){
					gameid = gameclient.creategame(playerid,gameserver);
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					System.out.print("Room created: " + Integer.toString(gameid));
					inLobby(gameid,playerid,gameserver,gameclient);
				}
				if(consolesplit[0].equalsIgnoreCase("/join")){
					String plistoutput = gameclient.joingame(Integer.parseInt(consolesplit[1]),playerid,gameserver);
					if(plistoutput.equals("INPROGRESS")) {
						System.out.println("Game in progress\n");
					}
					else{
					System.out.println("Joined room " + consolesplit[1]);
					System.out.println("Players in room:\n" + plistoutput);
					inLobby(Integer.parseInt(consolesplit[1]),playerid,gameserver,gameclient);
					}
				}
				if(consolesplit[0].equalsIgnoreCase("/who")){
					System.out.println(gameclient.listplayers());
				}
				if(consolesplit[0].equalsIgnoreCase("/quit")){
					if(gameclient.unregister(playerid)==0) System.exit(0);
					System.out.println("Something went wrong when removing you");
				}
				if(consolesplit[0].equalsIgnoreCase("/help") || consolesplit[0].equalsIgnoreCase("/h")){
					System.out.println("Commands:\ngames : Lists all available rooms with number of players in the room\ncreate : Create a room\njoin <gameid> : Join the room of the specified id\nwho : List all players online\nquit : Quit the game\nhelp : List all commands");
				}
				if(consolesplit[0].equalsIgnoreCase("/debug")){
					gameclient.debug(playerid);
					//System.out.println("Debug :" + gameclient.debug(playerid));
				}
				/*if(callbackId!=0){
						callbackId = aList.unregister(callbackId);
						System.out.println("Unregistered");
					}
					else {
						System.out.println("Not registered");
					}*/
				
			}
	}
	public static void inLobby(int gameid, int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException{
		Scanner console = new Scanner(System.in);
		while(true){
			String consoleline = console.nextLine();
			if(consoleline.equalsIgnoreCase("/start")){
				if(gameclient.startgame(gameid,playerid)==0) inGame(gameid,playerid,gameserver,gameclient);
			}
			if(consoleline.equalsIgnoreCase("/leave")){
				if(gameclient.leavegame(gameid, playerid)==0) mainscreen(playerid, gameserver, gameclient);
				System.out.println("Something went wrong when leaving the game");
			}
			if(consoleline.equalsIgnoreCase("/quit")){
				if(gameclient.leavegame( gameid, playerid)==0){
					System.out.println("Game left");
					if(gameclient.unregister(playerid)==0) System.exit(0);
					System.out.println("Something went wrong when closing");
				}
				
			}
		}
	}
	public static void inGame(int gameid, int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException{
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		System.out.println("Game started!\nEnter move:");
	}
}

/*
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.Scanner;

public class ShapeListClient {
	   public static void main(String args[]){
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String option = "read";
			String type = "rectangle";
			int callbackId=0;
			Scanner console = new Scanner(System.in);
			if(args.length > 2) option = args[2]; // read or write
			if(args.length> 3) type = args[3]; // circle, line, etc.
			System.out.println("option = " + option + ", shape = " + type);
			
				try {
					Registry registry = LocateRegistry.getRegistry(host, port);
					ShapeList aList = (ShapeList) registry.lookup("ShapeList");
					if(args.length <=2) {
						WhiteboardCallbackServant WBClientServ = new WhiteboardCallbackServant(aList);
						while (true){
						String consoleline = console.nextLine();
							if(consoleline.equalsIgnoreCase("register")){
								if(callbackId==0){
									callbackId = aList.register(WBClientServ);
									System.out.println("Registered, callbackId is " + callbackId);
								}
								else{
									System.out.println("Already registered, callbackId is " + callbackId);
								}
							}
							else if(consoleline.equalsIgnoreCase("unregister")){
								if(callbackId!=0){
									callbackId = aList.unregister(callbackId);
									System.out.println("Unregistered");
								}
								else {
									System.out.println("Not registered");
								}
							}
							else if(consoleline.equalsIgnoreCase("exit")){
								if(callbackId!=0) callbackId = aList.unregister(callbackId);
								System.exit(0);
							}
						}
					}
					else {
						
					
					System.out.println("Found server");
					Vector<Shape> shapeVec = aList.allShapes();
					System.out.println("Got vector");
					if(option.equals("read")){
						for(int i = 0; i < shapeVec.size(); i++){
							GraphicalObject g = ((Shape)shapeVec.elementAt(i)).getAllState();
							g.print();
						}
					} else {
						GraphicalObject g = new GraphicalObject(type, 
							new Rectangle(50,50,300,400), Color.red, Color.blue, false);
						System.out.println("Created graphical object");
						aList.newShape(g);
						System.out.println("Stored shape with ShapeList object on server");
					}
				}
				}catch(RemoteException e) {System.out.println("allShapes: " + e.getMessage());
				}catch(Exception e) {System.out.println("Registry: " + e.getMessage());
				}
			
	}
}
*/
