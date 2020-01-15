package ordo;
import java.util.concurrent.Semaphore;

public class CallBackImpl implements CallBack {
	
	int cpt;
	int nbFragments;
	Semaphore finTaches;
	
	// nombre d'appels faits à la méthode tacheFinie
	// et sémaphore pour gérer la fin du traitement par les machines du cluster
	public CallBackImpl(int nbFragments) {
		this.cpt = 0;
		this.finTaches = new Semaphore(0);
	}
	
	public Semaphore getFinTache() {
		return this.finTaches;
	}
	
	// compter le nombre de tâches finies
	public void tacheFinie() {
		this.cpt++;
		
		// s'il y a autant d'appels à tacheFinie que de fragments
		if (cpt == nbFragments) {
			// prévenir le client hidoop que le traitement est terminé
			finTaches.release();
		}
	}

}
