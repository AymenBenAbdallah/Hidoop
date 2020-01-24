package ordo;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;

import formats.Format;
import formats.Format.Type;
import formats.KVFormat;
import formats.LineFormat;
import hdfs.Namenode;
import map.MapReduce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public class Job implements JobInterface {
	
	// informations sur le fichier à traiter 
	private Type InputFormat;
	private String InputFname;
	
	// objet Callback
	CallBack cb;
	
	// liste des références aux démons du cluster
	private Daemon[] listeDaemon;
	
	// nombre de tâches finies
	int nbTachesFinies;
	
	// nombre de daemons
	int nbDaemons;
		
	public Job () {
		this.InputFormat = null;
		this.InputFname = null;
		this.listeDaemon = HidoopClient.listeDaemon;		
		this.nbDaemons = listeDaemon.length;
		this.cb = new CallBackImpl(recupNode(this.InputFname));
	}
	
	// Récupérer le nombre de fragments du fichier HDFS
	// via le fichier node (objet avec un attribut hashmap)
	private static int recupNode(String fname) {
		String filepath = "../config/node.txt";
		int res = 0;
		
		// Flux d'écriture depuis un fichier (node.txt)
		FileInputStream fis;
		try {
			fis = new FileInputStream(filepath);
			// Lecture d'un objet depuis le flux précédent
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			// Récupérer l'objet Namenode
			Namenode node = (Namenode) ois.readObject();
			
			// Récupérer le nombre de fragments du fichier
			res = node.getNbFragments(fname);
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} 
		
		return res;
	}
	
	public void setInputFormat(Type ft) {
		this.InputFormat = ft;
	}

	public void setInputFname(String fname) {
		this.InputFname = fname;
	}
	
	// appliquer runMap sur tous les fragments du fichier d'origine
	public void startJob(MapReduce mr) {
		// reader ; fragment source (LineFormat dans l'application comptage de mots)
		String fsce;
		Format reader;
		
		// writer ; fragment destination (suffixe "-res") (KVFormat)
		String fdest;
		Format writer;
		
		// nombre de fragments du fichier
		int nbFragments;
		nbFragments = recupNode(this.InputFname);
		
		// appliquer le traitement sur tous les fragments :
		// s'il n'y a qu'un fragment :
		try {
		if (nbFragments == 1) {
			// on met le suffixe "_1" pour le premier fragment
			fsce = this.InputFname + "_1";

			fdest = fsce + "-res";
			
			reader = new LineFormat(fsce);
			writer = new KVFormat(fdest);
			
			listeDaemon[0].runMap(mr, reader, writer, cb);
			
		// s'il y a plusieurs fragments :
		} else {
			for (int i = 0 ; i < nbFragments; i++) {
					// format des noms de fragments : "<nom fichier HDFS>_< n° fragment >"
					fsce = this.InputFname + "_" + (i+1);
					// fragment destination : ajouter le suffixe "-res";
					fdest = fsce + "-res";
	
					reader = new LineFormat(fsce);
					writer = new KVFormat(fdest);
					
					// compteur : si on atteint la fin de la liste de démons,
					// retourner au début de celle-ci ; ainsi, on parcourt
					// bien les fragments conformément à HDFS
					listeDaemon[i%(nbFragments-1)].runMap(mr, reader, writer, cb);
			}
		}
		} catch (RemoteException e) {
				e.printStackTrace();
		}
	}
}
