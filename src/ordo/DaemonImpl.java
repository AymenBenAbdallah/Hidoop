package ordo;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import formats.Format;
import map.Mapper;

public class DaemonImpl implements Daemon {
	// registre contenant les références aux services fournis par le démon
	static Registry registre;
	
	// emplacement et port du service
	static String url;
	static int port;
	
	private static void usage() {
		System.out.println("Utilisation : java DaemonImpl url port");
	}

	public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
		// Lancer la fonction map sur le fragment de fichier
		m.map(reader, writer);
		// Utiliser Callback pour prévenir que le traitement est terminé
		cb.tacheFinie();
	}

	public static void main (String args[]) throws RemoteException {
		
		try {
			if (args.length < 2) {
				usage();
				System.exit(1);
			}
			
			url = args[0];
			port = Integer.parseInt(args[1]);
			
			// Création du serveur de noms sur le port indiqué
			registre = LocateRegistry.createRegistry(port);
			
			// Inscription auprès du registre
			Naming.bind(url, new DaemonImpl());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
