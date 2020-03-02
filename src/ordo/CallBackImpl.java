package ordo;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {
	
	int cpt;
	int nbFragments;
	Semaphore finTaches;
	
	// nombre d'appels faits à la méthode tacheFinie
	// et sémaphore pour gérer la fin du traitement par les machines du cluster
	public CallBackImpl(int nbFragments) throws RemoteException {
		this.cpt = 0;
		this.nbFragments = nbFragments;
		this.finTaches = new Semaphore(0);
	}
	
	public Semaphore getTachesFinies() {
		return this.finTaches;
	}

	// compter le nombre de tâches finies
	public void tacheFinie() throws RemoteException {
		this.cpt++;
		System.out.println(cpt);
		
		// s'il y a autant d'appels à tacheFinie que de fragments
		if (cpt == nbFragments) {
			// prévenir le client hidoop que le traitement est terminé
			finTaches.release();
		}
	}

}
