package src.hdfs;

import java.io.*;
import java .net.*;

public class HdfsServeur {

    public static void main(String[] args) throws IOException {
        try {
            int port = Integer.parseInt(args[0]);
            ServerSocket sSocket = new ServerSocket(port);
            while (true) {
                // Ouverture Socket Serveur
                Socket socket = sSocket.accept();
                ObjectInputStream objectIS = new ObjectInputStream(socket.getInputStream());
                String msg = (String) objectIS.readObject();
                String[] req = msg.split("/@/");
                String commande = req[0];

                // définition des differents cas selon la commande
                switch (commande) {
                    case "CMD_DELETE" :
                        File delFile = new File(req[1]);
                        delFile.delete();
                        break;
                    
                    case "CMD_WRITE" :
                        File wFile = new File(req[1]);
                        FileWriter fWriter = new FileWriter(wFile);
                        fWriter.write(req[2], 0, req[2].length());
                        fWriter.close();
                    
                    case "CMD_READ" :
                        File rFile = new File(req[1]);
                        BufferedReader bufReader = new BufferedReader(new FileReader(rFile));
                        String fragment = "";
                        while (bufReader.readLine() != null) {
                            fragment = fragment + bufReader.readLine() + "\n";
                        }
                        ObjectOutputStream objectOS = new ObjectOutputStream(socket.getOutputStream());
                        objectOS.writeObject(fragment);
                        bufReader.close();
                        objectOS.close();
                        break;
                }
                objectIS.close();
                sSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}