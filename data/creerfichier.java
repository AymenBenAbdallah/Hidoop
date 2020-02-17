import java.io.*;
import java.math.*;

public class creerfichier{

    public static String generate(final int length) {
        final String chars = "abcdefghijklmnopqrstuvwxyz";
        String pass = "";
        for (int x = 0; x < length; x++) {
            final int i = (int) Math.floor(Math.random() * 26);
            pass += chars.charAt(i);
        }
        return pass;
    }

    public static void main(String[] args) {
        try {
            final int taille = Integer.parseInt(args[0]);
            final int taille_mot = Integer.parseInt(args[1]);
            final int nb_mots = Integer.parseInt(args[2]);
            final File file = new File(args[3]);
            final FileWriter fWriter = new FileWriter(file);
            final BufferedWriter writer = new BufferedWriter(fWriter);
            int i=0;
            while (i< taille){
                String ligne = "";
                for (int j=0; j <nb_mots;j++){
                    ligne = ligne + generate(taille_mot) + " ";
                }
                writer.write(ligne + "\n", 0, ligne.length());
                i += ligne.length();
            }
            writer.close();
            fWriter.close();
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }catch (IOException t) {
            t.printStackTrace();
        }
    }
}
