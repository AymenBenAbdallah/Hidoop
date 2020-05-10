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

for index in ${!listepc[*]}; do 
  # Creer les dossiers data
  ssh ${listepc[$index]} mkdir /tmp/data
  # Lancer les démons Hdfs
  ssh ${listepc[$index]} java -cp ${chemin}/bin hdfs.HdfsServer ${listeph[$index]} &
done

# Lancement du client
sleep 0.5

java -cp bin hdfs.HdfsClient write line filesample.txt
#java -cp bin hdfs.HdfsClient delete filesample.txt
#java -cp bin hdfs.HdfsClient read data/filesample.txt data/filesample-red.txt
