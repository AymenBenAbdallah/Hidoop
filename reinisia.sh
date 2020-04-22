#!/bin/bash

# récupérer la 2e ligne du fichier de config (noms des machines)
listepc=$(sed "2q;d" src/config/config_hidoop.cfg)

# découper la chaîne obtenue sur les délimiteurs ","
# et la stocker dans un tableau
IFS=',' read -ra tabpc <<< "$listepc"

# Supprimer les dossiers contenant les fragments et données hdfs + hidoop
for pc in "${tabpc[@]}"
do
    ssh $pc rm -rf /tmp/data
done
