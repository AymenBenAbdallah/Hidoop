import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import formats.Format;
import map.Mapper;
import ordo.CallBack;
import ordo.Daemon;

public class DaemonImpl implements Daemon {
	static String url;
	
	public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
		// TODO
		// Lire le fragment de fichier -> HDFS
		// Lancer la fonction map sur le fragment de fichier
		m.map(reader, writer);
		// Ecrire le résultat dans un nouveau fichier -> HDFS
		// Utliser Callback pour prévenir que le traitement est terminé
		cb.tacheFinie();
	}

	public static void main (String args[]) throws RemoteException {
		
		// Inscription auprès du registre
		try {
			Naming.bind(url, new DaemonImpl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
