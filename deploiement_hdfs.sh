#!/bin/bash

# Script de lancement des démons en local



# Lancer hdfs

#lancer les seveurs pour recevoir les commandes du client :
#java -cp bin hdfs.HdfsServer 3100 //localhost:3100 &
#java -cp bin hdfs.HdfsServer 3500 //localhost:3500 &
#java -cp bin hdfs.HdfsServer 3200 //localhost:3200 &
#java -cp bin hdfs.HdfsServer 3300 //localhost:3300 &
#java -cp bin hdfs.HdfsServer 3400 //localhost:3400 &

chemin="~/Travail/2A/Hidoop_git/Hidoop"
# Entrez votre identifiant INP

# Faut-il générer les clés publiques ? Pas besoin si ça a déjà été fait !
gen_cles=false # < false | true >

###############################################################
# Compiler les fichiers du projet
javac -d bin src/**/*.java

# S'il faut générer les clés
if [ "$gen_cles" = true ]
then
	# Génération des clés publiques
	ssh-keygen -t  rsa

	# Envoyer la clé publique sur les machines du cluster
	ssh-copy-id ${id}@vador
	ssh-copy-id ${id}@leia
	ssh-copy-id ${id}@luke
	ssh-copy-id ${id}@yoda
	ssh-copy-id ${id}@dragon
fi
# kill des processus qui ont été deja lancés sur les ports utilisés

#fuser -k 3100/tcp
#fuser -k 3600/tcp
#fuser -k 3200/tcp
#fuser -k 3300/tcp
#fuser -k 3400/tcp
#fuser -k 3000/tcp


#ssh ${id}@vador fuser -k 3100/tcp
#ssh ${id}@leia fuser -k 3500/tcp
#ssh ${id}@luke fuser -k 3200/tcp
#ssh ${id}@yoda fuser -k 3300/tcp
#ssh ${id}@solo fuser -k 3400/tcp

#ssh vador "kill \$(jps | grep HdfsServer | awk '{print \$1}')"
#ssh leia  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"
#ssh pikachu  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"
#ssh goldorak  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"

# Lancer les démons sur les machines distantes

#ssh vador fuser -k 3158/tcp
ssh sodium java -cp ${chemin}/bin hdfs.HdfsServer 3158 &

#ssh leia fuser -k 3292/tcp
ssh leia java -cp ${chemin}/bin hdfs.HdfsServer 3292 &

#ssh pikachu fuser -k 3692/tcp
ssh tao java -cp ${chemin}/bin hdfs.HdfsServer 3692 &

#ssh goldorak fuser -k 3434/tcp
ssh goldorak java -cp ${chemin}/bin hdfs.HdfsServer 3434 &

#ssh ${id}@sodium fuser -k 3000/tcp 
#ssh ${id}@sodium java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3000 &

#lancer le client
sleep 0.5
java -cp bin hdfs.HdfsClient write line filesample.txt
#java -cp bin hdfs.HdfsClient delete filesample.txt
#java -cp bin hdfs.HdfsClient read filesample.txt

