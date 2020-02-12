#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Chemin d'accès vers le projet Hidoop
chemin="~/Travail/2A/Hidoop_git/Hidoop/"

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

# Lancer les démons sur les machines distantes
ssh vador fuser -k 1100/tcp
ssh vador java -cp ${chemin}/bin ordo.DaemonImpl 1100 &

ssh leia fuser -k 1200/tcp
ssh leia java -cp ${chemin}/bin ordo.DaemonImpl 1200 &

ssh tao fuser -k 1300/tcp
ssh tao java -cp ${chemin}/bin ordo.DaemonImpl 1300 &

ssh goldorak fuser -k 1400/tcp
ssh goldorak java -cp ${chemin}/bin ordo.DaemonImpl 1400 &
