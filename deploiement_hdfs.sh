#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Chemin d'accès vers le projet Hidoop
chemin="Téléchargements/Hidoopgit/"

# Récupérer la 2e ligne du fichier de config (noms des machines)
listepc=$(sed "2q;d" src/config/config_hidoop.cfg)

# Découper la chaîne obtenue sur les délimiteurs ","
# et la stocker dans un tableau
IFS=',' read -ra tabpc <<< "$listepc"

# Récupérer la 4e ligne du fichier de config (ports Hdfs)
listeph=$(sed "4q;d" src/config/config_hidoop.cfg)

# Découper la chaîne obtenue sur les délimiteurs ","
# et la stocker dans un tableau
IFS=',' read -ra tabph <<< "$listeph"

###############################################################
# Compiler les fichiers du projet
javac -d bin src/**/*.java

for index in ${!tabpc[*]}; do 
  # Creer les dossiers data
  ssh ${tabpc[$index]} mkdir /tmp/data
  # Lancer les démons Hdfs
  ssh ${tabpc[$index]} java -cp ${chemin}/bin hdfs.HdfsServer ${tabph[$index]} &
done

# Lancement du client
sleep 0.5

java -cp bin hdfs.HdfsClient write line data.txt $1
#java -cp bin hdfs.HdfsClient delete filesample.txt
#java -cp bin hdfs.HdfsClient read data/filesample.txt data/filesample-red.txt
