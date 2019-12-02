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

public class Job implements JobInterface, CallBack {
	
	private Type InputFormat;
	private String InputFname;
	private Daemon[] listeDaemon;
	private int nbCluster;
	
	// nombre de tâches finies
	int nbTachesFinies;
	
	public Job (Daemon[] listeDaemon, int nbCluster) {
		this.InputFormat = null;
		this.InputFname = null;
		this.listeDaemon = listeDaemon;
		this.nbCluster = nbCluster;
		this.nbTachesFinies = 0;
	}

	public void setInputFormat(Type ft) {
		this.InputFormat = ft;
	}

	public void setInputFname(String fname) {
		this.InputFname = fname;
	}

	public void startJob(MapReduce mr) {
		// appliquer runMap sur chaque machine du cluster
		for (int i = 0 ; i < nbCluster ; i++) { 
			listeDaemon[i].runMap(m, reader, writer, cb);
		}
	}

	public void tacheFinie() {
		this.nbTachesFinies++;
	}

}
