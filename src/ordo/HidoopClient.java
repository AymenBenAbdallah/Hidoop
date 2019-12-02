package ordo;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import application.MyMapReduce;
import formats.Format.Type;
import hdfs.HdfsClient;

public class HidoopClient {

	public static void main (String args[]) throws RemoteException {
		// Déclaration des variables
		
		// Nom du fichier écrit sur HDFS via HDFSClient 
		String hdfsFname;
		
		// Nom du fichier traité local
		String localFSDestFname;
		
		// nombre de machines contenues dans le cluster
		int nbCluster;
		
		// registre contenant les références
		// aux services des machines du cluster
		Registry registre;
		
		// port correspondant au registre 
		int port = 0;
		
		// informations de format
		String fname;
		Type ft;
		
		// objet MapReduce utilisé
		MyMapReduce mr;
		
		// objet Job sur lequel on applique startJob
		Job job;
		
		// liste des url correspondant aux démons du cluster
		String urlDaemon[];
		
		// liste des références aux démons du cluster dans le registre
		Daemon listeDaemon[];
		
		try {
			// initialisation des variables
			nbCluster = 0;
			port = 0;
			
			fname = "";
			ft = null;
			
			urlDaemon = new String[nbCluster];
			listeDaemon = new Daemon[nbCluster];
			
			// création du registre sur le port indiqué
			registre = LocateRegistry.createRegistry(port);
			
			// récupérer les références des objets Daemon distants
			// à l'aide des url (déjà connues)
			for (int i = 0 ; i < nbCluster ; i++) { 
				listeDaemon[i]=(Daemon) Naming.lookup(urlDaemon[i]);
			}
			
			// création et définition des atributs de l'objet Job
			// on donne la liste des références aux Daemons à l'objet Job
			job = new Job(listeDaemon, nbCluster);
			
			job.setInputFname(fname);
			job.setInputFormat(ft);
			
			// création de l'objet MapReduce
			mr = new MyMapReduce();
			
			// lancement des tâches
			job.startJob(mr);
			
			// attendre que toutes les tâches soient terminees
			while(true) {
				if (job.nbTachesFinies == nbCluster) {
					break;
				}
			}
			
			// récupérer le fichier traité via HDFS
			HdfsClient.HdfsRead(hdfsFname, localFSDestFname);
			
			// appliquer le reduce() sur le résultat
			mr.reduce(reader, writer);
			
		// gestion des exceptions
			
		} catch (RemoteException e) {
			e.printStackTrace();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
