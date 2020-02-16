package ordo;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import formats.Format;
import map.Mapper;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {
	protected DaemonImpl() throws RemoteException {
	}

	// registre contenant les références aux services fournis par le démon
	static Registry registre;
	
	// emplacement et port du service
	static String url;
	static String hostname;
	static int port;
	
	private static void usage() {
		System.out.println("Utilisation : java DaemonImpl port");
	}

	public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
		// Lancer la fonction map sur le fragment de fichier
		m.map(reader, writer);
		// Utiliser Callback pour prévenir que le traitement est terminé
		cb.tacheFinie();
	}

	public static void main (String args[]) {
		
		// vérifier le bon usage du daemon
		try {
			if (args.length < 1) {
				usage();
				System.exit(1);
			}
			
			port = Integer.parseInt(args[0]);
			hostname = "localhost";
			
			// Création du serveur de noms sur le port indiqué
			try {
				registre = LocateRegistry.createRegistry(port);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			url = "//" + hostname + ":" + port + "/Daemon";
			System.out.println(url);
			
			// Inscription auprès du registre
			Naming.bind(url, new DaemonImpl());
			System.out.println("Bind daemon réussi");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
