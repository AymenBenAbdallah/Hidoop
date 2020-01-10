package ordo;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import formats.Format;
import formats.Format.Type;
import map.MapReduce;

public class Job implements JobInterface {
	
	// informations sur le fichier à traiter 
	private Type InputFormat;
	private String InputFname;
	
	// objet Callback
	private CallBack cb;
	
	// liste des références aux démons du cluster
	private Daemon[] listeDaemon;
	
	// nombre de tâches finies
	int nbTachesFinies;
	
	// nombre de daemons
	int nbDaemons;
	
	public Job (Daemon[] listeDaemon) {
		this.InputFormat = null;
		this.InputFname = null;
		this.listeDaemon = listeDaemon;		
		this.nbDaemons = listeDaemon.length;
	}

	public void setInputFormat(Type ft) {
		this.InputFormat = ft;
	}

	public void setInputFname(String fname) {
		this.InputFname = fname;
	}
	
	public boolean traitementFini() {
		return (this.cb.getTachesFinies() == nbDaemons);
	}

	public void startJob(MapReduce mr) {
		// appliquer runMap sur chaque machine du cluster
		// reader : format line ; writer : format kv
		for (int i = 0 ; i < nbDaemons; ; i++) {
			listeDaemon[i].runMap(mr, reader, writer, cb);
		}
	}

}
