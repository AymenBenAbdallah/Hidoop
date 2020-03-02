package ordo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public interface CallBack extends Remote {
	public void tacheFinie() throws RemoteException;
	public Semaphore getTachesFinies() throws RemoteException;

}
