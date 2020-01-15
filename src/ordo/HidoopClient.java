package ordo;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

import application.MyMapReduce;
import formats.Format;
import formats.Format.Type;
import formats.FormatReader;
import formats.FormatWriter;
import formats.KVFormat;
import formats.LineFormat;
import hdfs.HdfsClient;
import java.io.*;

import java.util.concurrent.Semaphore;

public class HidoopClient {
	
	// liste des références aux démons du cluster dans le registre
	public static Daemon listeDaemon[];
  
	private static void usage() {
		System.out.println("Utilisation : java HidoopClient fichier format");
	}
	
	// récupérer les emplacements indiqués dans le fichier de configuration
	private static void recupURL(String[] urls) {
		File file = new File("../config/config_hidoop.cfg");
		int cpt = 0;
		  
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st; 
			while ((st = br.readLine()) != null)
				urls[cpt] = st;
				cpt++;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main (String args[]) throws RemoteException {
		
		// Formats de fichiers utilisables
		String[] formats = {"line","kv"};
		
		// Fichiers source / destination
		FormatReader reader;
		FormatWriter writer;
		
		// nombre de machines contenues dans le cluster
		int nbCluster = 5;
		
		// informations de format de fichier
		Type ft;
		
		// liste des url correspondant aux démons du cluster
		String urlDaemon[];
		
		try {
			// vérifier le bon usage du client
			if (args.length < 2) {
				usage();
				System.exit(1);
			} else {
				if (!Arrays.asList(formats).contains(args[1])) {
					usage();
					System.exit(1);
				}
			}
			
			// Nom du fichier sur lequel appliquer le traitement
			String hdfsFname = args[0];
			
			// fichier HDFS destination  : ajout du suffixe "-res"
			// Nom du fichier traité avnt application du reduce
			String localFSDestFname = hdfsFname + "-res";
			
			// fichier résultat du reduce : ajout du suffixe "-red"
			// Nom du fichier traité après application du reduce
			String reduceDestFname = hdfsFname + "-red";
			
			// Récupérer le format de fichier indiqué en argument
			if (args[1].equals("line")) {
				ft = Format.Type.LINE;
			} else {
				ft = Format.Type.KV;
			}
			
			// récupérer les URLs depuis le fichier de configuration 
			urlDaemon = new String[nbCluster];			
			recupURL(urlDaemon);

			// récupérer les références des objets Daemon distants
			// à l'aide des url (déjà connues)
			listeDaemon = new Daemon[nbCluster];
			
			for (int i = 0 ; i < nbCluster ; i++) { 
				listeDaemon[i]=(Daemon) Naming.lookup(urlDaemon[i]);
			}
			
			// création et définition des attributs de l'objet Job
			// on donne la liste des références aux Daemons à l'objet Job
			Job job = new Job();
			
			// indiquer à job le nom et format du fichier à traiter
			job.setInputFname(hdfsFname);
			job.setInputFormat(ft);
			
			// création de l'objet MapReduce
			MyMapReduce mr = new MyMapReduce();
			
			// lancement des tâches
			job.startJob(mr);
			
			// attendre que toutes les tâches soient terminées
			// via un sémaphore initialisé à 0
			Semaphore attente = job.cb.getFinTache();
			attente.acquire();
			
			// récupérer le fichier traité via HDFS
			HdfsClient.HdfsRead(hdfsFname, localFSDestFname);
			
			// Reader : fichier local après traitement sur les machines du cluster
			// Writer : fichier final généré après application du reduce
			reader = new KVFormat(localFSDestFname);
			writer = new KVFormat(reduceDestFname);
			
			// appliquer reduce sur le résultat
			// reader : format kv ; writer : format kv
			mr.reduce(reader, writer);
						
		} catch (RemoteException e) {
			e.printStackTrace();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (NotBoundException e) {
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
