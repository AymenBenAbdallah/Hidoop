#!/bin/bash

# Script de lacement des démons en local



# Lancer hdfs

#lancer les seveurs pour recevoir les commandes du client :
#java -cp bin hdfs.HdfsServer 3100 //localhost:3100 &
#java -cp bin hdfs.HdfsServer 3500 //localhost:3500 &
#java -cp bin hdfs.HdfsServer 3200 //localhost:3200 &
#java -cp bin hdfs.HdfsServer 3300 //localhost:3300 &
#java -cp bin hdfs.HdfsServer 3400 //localhost:3400 &

# Entrez votre identifiant INP
id="aaitbela"

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

fuser -k 3100/tcp
fuser -k 3000/tcp
fuser -k 3200/tcp
fuser -k 3300/tcp
#fuser -k 3400/tcp


#ssh ${id}@vador fuser -k 3100/tcp
#ssh ${id}@leia fuser -k 3500/tcp
#ssh ${id}@luke fuser -k 3200/tcp
#ssh ${id}@yoda fuser -k 3300/tcp
#ssh ${id}@solo fuser -k 3400/tcp

# Lancer les démons sur les machines distantes

ssh ${id}@vador fuser -k 3100/tcp
ssh ${id}@vador java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3100 & 

ssh ${id}@leia fuser -k 3200/tcp
ssh ${id}@leia java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3200 &

ssh ${id}@luke fuser -k 3300/tcp 
ssh ${id}@luke java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3300 &

ssh ${id}@yoda fuser -k 3000/tcp 
ssh ${id}@yoda java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3000 &


#ssh ${id}@dragon fuser -k 3400/tcp 
#ssh ${id}@dragon java -cp ~/Téléchargements/Hidoop-master/bin hdfs.HdfsServer 3400 &

#lancer le client

java -cp bin hdfs.HdfsClient write line data/filesample.txt
#java -cp bin hdfs.HdfsClient delete data/filesample.txt


