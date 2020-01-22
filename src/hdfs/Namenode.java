package hdfs;

import java.io.*;

public class Namenode implements Serializable {

	private HashMap<String,int> fichiers;
	private String nomNode;
	
	public Namenode(String nameNode) {
		FileOutputStream 
		fichiers = new HashMap<String, int>();
		NomNode = nameNode;
	}
	public int getNbFragments(String NomFichier) {
		return this.getNbFragments(NomFichier);
		
	}
	public void Ajouter(String NomFichier, int NbFragment) {
		this.put(NomFichier,NbFragment);
	}
	public void Enlever(String NomFichier) {
		this.remove(NomFichier);
	}
	
}
