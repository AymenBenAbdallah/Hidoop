import java.io.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class confignodepremierefois implements Serializable {

	private static String nameNode = "node.txt";
	
    public static void main(String[] args){
        HashMap<String,Integer> hash = new HashMap<String,Integer>();
		try {
			FileOutputStream fichier = new FileOutputStream(nameNode);
			ObjectOutputStream objet = new ObjectOutputStream(fichier);
			objet.writeObject(hash);
			objet.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
    }

}

