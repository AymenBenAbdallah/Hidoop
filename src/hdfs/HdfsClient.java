/* une PROPOSITION de squelette, incomplète et adaptable... */

package src.hdfs;

import java.io.*;
import java.net.Socket;
import src.formats.Format;
import src.formats.KV;
import src.formats.KVFormat;
import src.formats.LineFormat;

public class HdfsClient {

    private static int numPorts[] = {7650, 7654, 3478, 5481, 7193};
    private static String nomMachines[] = {"aymen, ali, luc, sherwin, khalil"};

    private static void usage() {
        System.out.println("Usage: java HdfsClient read <file>");
        System.out.println("Usage: java HdfsClient write <line|kv> <file>");
        System.out.println("Usage: java HdfsClient delete <file>");
    }
	
    public static void HdfsDelete(String hdfsFname) {
        try{
            int nbServers = nomMachines.length;
            for (int i = 0; i < nbServers; i++) {
                Socket sock = new Socket (nomMachines[i], numPorts[i]);
                ObjectOutputStream objectOS = new ObjectOutputStream(sock.getOutputStream());
                objectOS.writeObject(Commande.CMD_DELETE.toString() + " " + hdfsFname + Integer.toString(i));
                objectOS.close();
                sock.close();                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname, 
     int repFactor) {
         try {
            int nbLignes = 0;
            FileReader fRead = new FileReader(localFSSourceFname);
            BufferedReader bRead = new BufferedReader(fRead);
            while (bRead.readLine() != null) {
                nbLignes ++;
            }
            BufferedReader bRRead = new BufferedReader(fRead);

            // Division du fichier
            int nbServers = nomMachines.length;
            int division = nbLignes / nbServers;
            int reste = nbLignes % nbServers;
            for (int i = 0; i < nomMachines.length; i++) {
                int taille = division;

                // cas ou reste existe
                if (reste != 0) {
                    taille ++;
                    reste --;
                }
                String fragment = "";
                for (int j = 0; j < taille; j++) {
                    fragment = fragment + bRRead.readLine() + "\n";
                }

                Socket socket = new Socket (nomMachines[i], numPorts[i]);
                ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                objectOS.writeObject(Commande.CMD_WRITE.toString() + " " + localFSSourceFname + "_i" + fragment);
                objectOS.close();
                bRead.close();
                bRRead.close();
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void HdfsRead(String hdfsFname, String localFSDestFname) {
        File file = new File(localFSDestFname);
        try {
            FileWriter fWrite = new FileWriter(file);
            for (int i = 0; i < nomMachines.length; i++) {
                Socket socket = new Socket (nomMachines[i], numPorts[i]);
                ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectIS = new ObjectInputStream(socket.getInputStream());
                objectOS.writeObject(Commande.CMD_READ.toString() + " " + hdfsFname + "_" + Integer.toString(i));
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
