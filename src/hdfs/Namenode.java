package hdfs;

import java.io.*;

public class Namenode implements Serializable {

	private HashMap<String,int> fichiers;
	private static String nameNode = "../config/node.txt";
	
	public Namenode() {
		FileInputStream fichier = new FileInputStream(nameNode);
		ObjetInputStream objet = new ObjectInputStream(fichier);
		this.HashMap = objectIn.readObject();
		objet.close();
	}
	public int getNbFragments(String NomFichier) {
		FileInputStream fichier = new FileInputStream(nameNode);
		ObjetInputStream objet = new ObjectInputStream(fichier);
		this.HashMap = objectIn.readObject();
		objet.close();
		return this.getNbFragments(NomFichier);
		
	}
	public void Ajouter(String NomFichier, int NbFragment) {
		this.fichiers.put(NomFichier,NbFragment);
		FileOutputStream fichier = new FileOutputStream(nameNode);
		ObjetOutputStream objet = new ObjectOutputStream(fichier);
		objet.writeObject(this.fichiers);
		objet.close();
	}
	public void Enlever(String NomFichier) {
		this.remove(NomFichier);
		FileOutputStream fichier = new FileOutputStream(nameNode);
		ObjetOutputStream objet = new ObjectOutputStream(fichier);
		objet.writeObject(this.fichiers);
		objet.close();
	}
	
}
