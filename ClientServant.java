
/*
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class ShapeListServant extends UnicastRemoteObject implements ShapeList {
	private static final long serialVersionUID = 1L;
	private Vector<Shape> theList;
	private int version;
	private Map<Integer, WhiteboardCallback> subbed = new HashMap<Integer, WhiteboardCallback>();
	private int subnum=0;

	public ShapeListServant() throws RemoteException{
		theList = new Vector<Shape>();
		version = 0;
	}

	public Shape newShape(GraphicalObject g) throws RemoteException{
		version++;
		Shape s = new ShapeServant(g, version);
		theList.addElement(s);
		for (int key: subbed.keySet()) {
			//System.out.println("iterating...");
			//System.out.println("key: "+subbed.get(key));
			subbed.get(key).callback(version);
		}
		return s;
	}

	public Vector<Shape> allShapes() throws RemoteException{
		return theList;
	}

	public int getVersion() throws RemoteException{
		return version;
	}
	
	public int register(WhiteboardCallback whiteboard) throws RemoteException{
		subnum++;
		subbed.put(subnum, whiteboard);
		System.out.println(subbed);
		return subnum;
		
	}
	
	public int unregister(int callbackId) throws RemoteException{
		subbed.remove(callbackId);
		return 0;
	}
}
*/