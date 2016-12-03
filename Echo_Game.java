import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
//Client side of the game
public class Echo_Game {
	public static void main(String args[]) throws IOException, InterruptedException {
		//clear cmd line
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		String host = "localhost";
		int port = 1099;
		int playerid = 0;
		Scanner console = new Scanner(System.in);
		try {
			//Connect to the registry and make a Client object and a ServerServant object
			Registry registry = LocateRegistry.getRegistry(host,port);
			Client gameclient = (Client) registry.lookup("Client");
			ServerServant gameserver = new ServerServant(gameclient);
			System.out.println("Welcome to Echo!\n");
			System.out.println("Enter your playername: ");
			String consoleline;
			//Register to the server with a playername and get given an id.
			while(true){
				consoleline = console.nextLine();
				playerid = gameclient.register(consoleline);
				if (playerid>0) break;
				else if (playerid==-1) {
					System.out.println("Player already exists with that name");
				}
				else{
					System.out.println("Error occured, enter another: ");
				}
			}
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			System.out.println("\nWelcome to Echo " + consoleline + "!\n");
			mainscreen(playerid, gameserver, gameclient);
		}catch(RemoteException e) {System.out.println("Remote Exception: " + e.getMessage());
		}catch(Exception e) {System.out.println("Registry: " + e.getMessage());
		}
		System.out.println("Connected");
	}
	//mainscreen part of the client, view game list, view players, create and join lobbies
	public static void mainscreen(int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException {
		System.out.println("Commands:\n/games : Lists all available rooms with number of players in the room\n/create : Create a room\n/join <gameid> : Join the room of the specified id\n/who : List all players online\n/quit : Quit the game\n/help : List all commands");
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
				else if(consolesplit[0].equalsIgnoreCase("/create")){
					gameid = gameclient.creategame(playerid,gameserver);
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					System.out.println("Room created: " + Integer.toString(gameid) + "\n");
					inLobby(gameid,playerid,gameserver,gameclient);
				}
				else if(consolesplit[0].equalsIgnoreCase("/join")){
					if(consolesplit.length==2){
						try{
							int inputnumber = Integer.parseInt(consolesplit[1]);
							String plistoutput = gameclient.joingame(inputnumber,playerid,gameserver);
							if(plistoutput.equals("INPROGRESS")) {
								System.out.println("Game in progress\n");
							}
							else if(plistoutput.equals("NOGAME")) {
								System.out.println("No game exists with that id");
							}
							else{
							System.out.println("Joined room " + consolesplit[1]);
							System.out.println("Players in room:\n" + plistoutput);
							inLobby(Integer.parseInt(consolesplit[1]),playerid,gameserver,gameclient);
							}
						}
						catch(NumberFormatException e) {
							System.out.println("Second argument was not a valid number");
						}
					}
					else{
						System.out.println("Incorrect command");
					}
				}
				else if(consolesplit[0].equalsIgnoreCase("/who")){
					System.out.println(gameclient.listplayers());
				}
				else if(consolesplit[0].equalsIgnoreCase("/quit")){
					if(gameclient.unregister(playerid)==0) System.exit(0);
					System.out.println("Something went wrong when removing you");
				}
				else if(consolesplit[0].equalsIgnoreCase("/help") || consolesplit[0].equalsIgnoreCase("/h")){
					System.out.println("Commands:\n/games : Lists all available rooms with number of players in the room\n/create : Create a room\n/join <gameid> : Join the room of the specified id\n/who : List all players online\n/quit : Quit the game\n/help : List all commands");
				}
				else if(consolesplit[0].equalsIgnoreCase("/debug")){
					gameclient.debug(playerid);
				}
				else {
					System.out.println("Unrecognized command, type /help for the command list");
				}
			}
	}
	//lobby part of the client, can view players in lobby, chat with players in lobby, leave lobby and start a game
	public static void inLobby(int gameid, int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException{
		Scanner console = new Scanner(System.in);
		while(true){
			String consoleline = console.nextLine();
			if(consoleline.equalsIgnoreCase("/start")){
				if(gameclient.startgame(gameid,playerid)==0) inGame(gameid,playerid,gameserver,gameclient);
			}
			else if(consoleline.equalsIgnoreCase("/leave")){
				if(gameclient.leavegame(gameid, playerid, gameserver)==0) mainscreen(playerid, gameserver, gameclient);
				System.out.println("Something went wrong when leaving the game");
			}
			else if(consoleline.equalsIgnoreCase("/quit")){
				if(gameclient.leavegame( gameid, playerid, gameserver)==0){
					System.out.println("Game left");
					if(gameclient.unregister(playerid)==0) System.exit(0);
					System.out.println("Something went wrong when closing");
				}
			}
			else if(consoleline.equalsIgnoreCase("/who")){
				System.out.println(gameclient.listplayerslobby(gameid));
			}
			else if(consoleline.equalsIgnoreCase("/help")){
				System.out.println("Commands:\n/who : List all players in lobby\n/leave : Leave the lobby and go back to main menu\n/quit : Quit the game\n/help : List all commands");
			}
			else {
				System.out.println("\n");
				gameclient.lobbychat(gameid, playerid, gameserver, " says: " + consoleline);
			}
		}
	}
	//ingame part of the client, can pass moves to the server, not implemented fully
	public static void inGame(int gameid, int playerid, ServerServant gameserver, Client gameclient) throws IOException, InterruptedException{
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		System.out.println("Game started!\nEnter move:");
	}
}
