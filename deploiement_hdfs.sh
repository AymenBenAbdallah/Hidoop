#!/bin/bash

# Script de lancement des démons

# Lancer hdfs

#lancer les seveurs pour recevoir les commandes du client :
#java -cp bin hdfs.HdfsServer 3100 //localhost:3100 &
#java -cp bin hdfs.HdfsServer 3500 //localhost:3500 &
#java -cp bin hdfs.HdfsServer 3200 //localhost:3200 &
#java -cp bin hdfs.HdfsServer 3300 //localhost:3300 &
#java -cp bin hdfs.HdfsServer 3400 //localhost:3400 &

# Chemin d'accès vers le projet Hidoop
chemin="~/Téléchargements/Hidoop-master"

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
	ssh-copy-id vador
	ssh-copy-id leia
	ssh-copy-id tao
	ssh-copy-id goldorak
fi
# kill des processus qui ont été deja lancés sur les ports utilisés

fuser -k 3100/tcp
fuser -k 3000/tcp
fuser -k 3200/tcp
fuser -k 3300/tcp
#fuser -k 3400/tcp


#ssh vador fuser -k 3100/tcp
#ssh leia fuser -k 3500/tcp
#ssh luke fuser -k 3200/tcp
#ssh yoda fuser -k 3300/tcp
#ssh solo fuser -k 3400/tcp

# Lancer les démons sur les machines distantes

ssh vador fuser -k 3100/tcp
ssh vador java -cp ${chemin}/bin hdfs.HdfsServer 3100 & 

ssh leia fuser -k 3200/tcp
ssh leia java -cp ${chemin}/bin hdfs.HdfsServer 3200 &

ssh luke fuser -k 3300/tcp 
ssh luke java -cp ${chemin}/bin hdfs.HdfsServer 3300 &

ssh yoda fuser -k 3000/tcp 
ssh yoda java -cp ${chemin}/bin hdfs.HdfsServer 3000 &


#ssh dragon fuser -k 3400/tcp 
#ssh dragon java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3400 &

#lancer le client

java -cp bin hdfs.HdfsClient write line data/filesample.txt
#java -cp bin hdfs.HdfsClient delete data/filesample.txt


