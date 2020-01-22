package hdfs;

import java.io.*;
import java.net.Socket;
import formats.Format;
import formats.KV;
import formats.KVFormat;
import formats.LineFormat;

public class HdfsClient {

    private static int numPorts[] = {7650, 7654, 3478, 5481, 7193};
    private static String nomMachines[] = {"aymen, ali, luc, sherwin, khalil"};
    final static int nbServers = 5;
    private static long taille_fragment = 185;
    private static KV cst = new KV("hi","hello");

    private static void usage() {
        System.out.println("Usage: java HdfsClient read <file>");
        System.out.println("Usage: java HdfsClient write <line|kv> <file>");
        System.out.println("Usage: java HdfsClient delete <file>");
    }
	
    public static void HdfsDelete(String hdfsFname) {
        try{
        	int j ;
        	
            // supprimer les fichiers générés des serveurs
        	
        	Namenode node = new Namenode();
        	int nbfragments = node.getNbFragments(hdfsFname);	// nb de fragments du fichier
        	
            for (int i = 0; i < nbfragments; i++) {
            	j = i % nbServers; 
                Socket sock = new Socket (nomMachines[j], numPorts[j]);
                ObjectOutputStream objectOS = new ObjectOutputStream(sock.getOutputStream());
                objectOS.writeObject(Commande.CMD_DELETE.toString() + " " + hdfsFname + Integer.toString(i));
                objectOS.close();
                sock.close();                
            }
        	
        	//supprimer les info de hdfsFname du fichier node.
        	
            
        	node.Enlever(hdfsFname);					// suppression de l'occurence du fichier dans la node
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void HdfsWrite(Type fmt, String localFSSourceFname, int repFactor) {
         try {
            
            // créer les constantes du problème.
            
            String fragment;
            int index ;
            KV buffer = new KV();
            
            // calculer la taille du fichier
            
        	File file = new File(localFSSourceFname);
        	long taille = file.getTotalSpace();
        	
        	// vérifier que la taille du bloc est un diviseur de la taille totale sinon ajouter 1.
        	
        	int nbfragments = taille/taille_fragment;
        	if (taille%taille_fragment != 0) { nbfragments ++;}
            
        	// ajouter le nombre de fragments dans le fichier node.
        	
        	Namenode node = new Namenode();
        	node.Ajouter(localFSSourceFname, nbfragments);
            
        	// le cas d'un lineformat fichier:
        	
            if (fmt == Type.LINE){
            	
            	
                LineFormat fichier = new LineFormat(localFSSourceFname);
                fichier.open(Format.OpenMode.R);
                
                for (int i=0; i < nbfragments; i++){
                    
                	index = 0;
                    buffer = cst ;
                    
                    //créer le fragment à partir des lignes:
                    
                    fragment = "";
                    while (index < taille_fragment && buffer != null){
                        buffer = fichier.read();
                        fragment = fragment + buffer.v;
                        index = fichier.getIndex()-i*nbfragments;
                    }
                    
                    int t = i%nbServers;
                    
                    Socket socket = new Socket (nomMachines[t], numPorts[t]);
                    ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                    objectOS.writeObject(Commande.CMD_WRITE.toString() + "/@/" + localFSSourceFname + "_" + Integer.toString(i) + "/@/" + fragment);
                    objectOS.close();
                    socket.close();
                }
                fichier.close();
            }else if (fmt == formats.Type.KV){
            
            KVFormat fichier = new KVFormat(localFSSourceFname);
            fichier.open(Format.OpenMode.R);
            
            for (int i=0; i < nbfragments ; i++){
                index = 0;
                buffer =  cst;
                while (index < taillebloc && buffer != null){
                    buffer = fichier.read();
                    fragment = fragment + buffer.v;
                    index = fichier.getIndex()-i*nbfragments;
                }
                // il se trouve nécessaire de réinitialiser le buffer à une valeur non nulle du fait que
                // la condition d'entrer dans la boucle while est que buffer != null
                
                int t = i%nbServers;
                
                Socket socket = new Socket (nomMachines[t], numPorts[t]);
                ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                objectOS.writeObject(Commande.CMD_WRITE.toString() + "/@/" + localFSSourceFname + "_" + Integer.toString(i) + "/@/" + fragment);
                objectOS.close();
                socket.close();
            }
            fichier.close();
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void HdfsRead(String hdfsFname, String localFSDestFname) {
        File file = new File(localFSDestFname + "-res");
        try {
        	int j;
            FileWriter fWrite = new FileWriter(file);
            Namenode node = new Namenode();
        	int nbfragments = node.getNbFragments(localFSDestFname);
            for (int i = 0; i < nbfragments; i++) {
            	j = i % nbServers;
                Socket socket = new Socket (nomMachines[j], numPorts[j]);
                ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectIS = new ObjectInputStream(socket.getInputStream());
                objectOS.writeObject(Commande.CMD_READ.toString() + "/@/" + hdfsFname + Integer.toString(i)+ "-res");
                String fragment = (String) objectIS.readObject();
                fWrite.write(fragment,0,fragment.length());
                objectOS.close();
                objectIS.close();
                socket.close();
                fWrite.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	
    public static void main(String[] args) {
        // java HdfsClient <read|write> <line|kv> <file>

        try {
            if (args.length<2) {usage(); return;}

            switch (args[0]) {
              case "read": HdfsRead(args[1],null); break;
              case "delete": HdfsDelete(args[1]); break;
              case "write": 
                Format.Type fmt;
                if (args.length<3) {usage(); return;}
                if (args[1].equals("line")) fmt = Format.Type.LINE;
                else if(args[1].equals("kv")) fmt = Format.Type.KV;
                else {usage(); return;}
                HdfsWrite(fmt,args[2],1);
            }	
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

// un fichier node dans lequel en écrit les noms des fichiers sur lesquels on 
// travaille et puis le nombre de fragments. ce fichier sera en format Key 
// value avec le signe ":" séparant les deux parties. il sera édité pendant 
// la phase de création et de suppression des fragments.


