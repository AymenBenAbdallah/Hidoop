package ordo;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

import application.MyMapReduce;
import formats.Format;
import formats.Format.OpenMode;
import formats.Format.Type;
import formats.KVFormat;
import hdfs.HdfsClient;
import java.io.*;

import java.util.concurrent.Semaphore;

public class HidoopClient {
	
	// liste des références aux démons du cluster dans le registre
	public static Daemon listeDaemon[];
  
	private static void usage() {
		System.out.println("Utilisation : java HidoopClient fichier format nbmachines");
	}
	
	private static void usage_config() {
		System.out.println("Utilisation du fichier de configuration :"
				+ "L1 : machine1,machine2"
				+ "L2 : port_hdfs1,port_hdfs2"
				+ "L3 : ports registres RMI"
				+ "L4 : taille");
	}
	
	// récupérer les emplacements indiqués dans le fichier de configuration
	private static String[] recupURL(int nbMachines) {
		String path = "src/config/config_hidoop.cfg";
		
		File file = new File(path);
		int cpt = 0;
		
		String[] ports = new String[nbMachines];
		String[] noms = new String[nbMachines];
		String[] urls = new String[nbMachines];
		  
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st; 
			while ((st = br.readLine()) != null) {
			  // si la ligne n'est pas un commentaire
			  if (!st.startsWith("#")) {
				  // noms des machines
				  if (cpt == 0) {
					  noms = st.split(",");
				  }
				  // ports RMI
				  if (cpt == 2) {
					  ports = st.split(",");
				  }
				  cpt++;					  
			  }
			}
			
			br.close();
			
			// si le fichier de configuration est correct
			if (noms.length != 0 && ports.length == noms.length) {
				for (int i=0 ; i < nbMachines ; i++) {
					urls[i] = "//" + noms[i] + ":" + ports[i] + "/Daemon";
					// System.out.println(urls[i]);
				}
			} else {
				usage_config();
			}
									
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return urls;

	}
	

	public static void main (String args[]) throws RemoteException {
		
		// Formats de fichiers utilisables
		String[] formats = {"line","kv"};
		
		// Fichiers source / destination
		Format reader;
		Format writer;
		
		// nombre de machines à utiliser
		int nbCluster;
		
		// informations de format de fichier
		Type ft;
		
		// liste des url correspondant aux démons du cluster
		String urlDaemon[];
		
		try {
			// vérifier le bon usage du client
			if (args.length < 3) {
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
			// Nom du fichier traité avant application du reduce
			String[] nomExt = hdfsFname.split("\\.");
			String localFSDestFname = nomExt[0] + "-res" + "." + nomExt[1];
			System.out.println(localFSDestFname);
			
			// fichier résultat du reduce : ajout du suffixe "-red"
			// Nom du fichier traité après application du reduce
			String reduceDestFname = "data/" + nomExt[0] + "-red" + "." + nomExt[1];
			// System.out.println(reduceDestFname);

			// Récupérer le format de fichier indiqué en argument
			if (args[1].equals("line")) {
				ft = Format.Type.LINE;
			} else {
				ft = Format.Type.KV;
			}
			
			// récupérer le nombre de machines
			nbCluster = Integer.parseInt(args[2]);
			
			// récupérer les URLs depuis le fichier de configuration 
			urlDaemon = recupURL(nbCluster);

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
			System.out.println("Lancement du Job");
			job.startJob(mr);
			System.out.println("Fin du lancement du Job");
			
			// attendre que toutes les tâches soient terminées
			// via un sémaphore initialisé à 0
			Semaphore attente = job.cb.getTachesFinies();
			System.out.println("Attente du sémaphore");
			attente.acquire();
			System.out.println("Fin du sémaphore");
			
			// récupérer le fichier traité via HDFS
			HdfsClient.HdfsRead(hdfsFname, localFSDestFname);
			System.out.println("Lecture terminée");
			
			// Reader : fichier local après traitement sur les machines du cluster
			// Writer : fichier final généré après application du reduce
			OpenMode modeR = Format.OpenMode.R;
			OpenMode modeW = Format.OpenMode.W;
			
			reader = new KVFormat(localFSDestFname);
			writer = new KVFormat(reduceDestFname);
			
			reader.open(modeR);
			writer.open(modeW);
			
			// appliquer reduce sur le résultat
			// reader : format kv ; writer : format kv
			System.out.println("Début du reduce");
			mr.reduce(reader, writer);
			System.out.println("Fin du reduce");
			
			reader.close();
			writer.close();
						
		} catch (RemoteException e) {
			e.printStackTrace();
			
		} catch (NotBoundException e) {
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			e.printStackTrace();	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
