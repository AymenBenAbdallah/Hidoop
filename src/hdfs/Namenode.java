package hdfs;

import java.io.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Namenode implements Serializable {

	private HashMap<String,Integer> fichiers;
	private static String nameNode = "../config/node.txt";
	
	@SuppressWarnings("unchecked")
	public  Namenode() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fichier = new FileInputStream(nameNode);
			ObjectInputStream objet = new ObjectInputStream(fichier);
			fichiers = (HashMap<String, Integer>) objet.readObject();
			objet.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public int getNbFragments(String NomFichier) throws IOException, ClassNotFoundException {
		try {
			FileInputStream fichier = new FileInputStream(nameNode);
			ObjectInputStream objet = new ObjectInputStream(fichier);
			this.fichiers = (HashMap<String, Integer>) objet.readObject();
			objet.close();
		} catch(IOException ex) {
			ex.printStackTrace();
			return -1;
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
			return -1;
		}
		return this.getNbFragments(NomFichier);
		
	}
	public void Ajouter(String NomFichier, int NbFragment) throws IOException {
		try {
			this.fichiers.put(NomFichier,NbFragment);
			FileOutputStream fichier = new FileOutputStream(nameNode);
			ObjectOutputStream objet = new ObjectOutputStream(fichier);
			objet.writeObject(this.fichiers);
			objet.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void Enlever(String NomFichier) throws IOException {
		try {
			this.fichiers.remove(NomFichier);
			FileOutputStream fichier = new FileOutputStream(nameNode);
			ObjectOutputStream objet = new ObjectOutputStream(fichier);
			objet.writeObject(this.fichiers);
			objet.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}

