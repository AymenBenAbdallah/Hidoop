#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Entrez votre identifiant INP
id="ssabras"

# Faut-il générer les clés publiques ? Pas besoin si ça a déjà été fait !
gen_cles=false # < false | true >

###############################################################
# Compiler les fichiers du projet
javac -d bin src/**/*.java

# Lancer les démons localement (en arrière-plan)
#java -cp bin ordo.DaemonImpl //localhost:7654/ali 7654 &
#java -cp bin ordo.DaemonImpl //localhost:7650/aymen 7650 &
#java -cp bin ordo.DaemonImpl //localhost:3478/luc 3478 &
#java -cp bin ordo.DaemonImpl //localhost:5481/sherwin 5481 &
#java -cp bin ordo.DaemonImpl //localhost:7193/khalil 7193 &

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
	ssh-copy-id ${id}@solo
fi

# Lancer les démons sur les machines distantes
ssh ${id}@vador java -cp eclipse-workspace/Hidoop/bin ordo.DaemonImpl //localhost:7654/ali 7654 &
ssh ${id}@leia java -cp eclipse-workspace/Hidoop/bin ordo.DaemonImpl //localhost:7650/aymen 7650 &
ssh ${id}@luke java -cp eclipse-workspace/Hidoop/bin ordo.DaemonImpl //localhost:3478/luc 3478 &
ssh ${id}@yoda java -cp eclipse-workspace/Hidoop/bin ordo.DaemonImpl //localhost:5481/sherwin 5481 &
ssh ${id}@solo java -cp eclipse-workspace/Hidoop/bin ordo.DaemonImpl //localhost:7193/khalil 7193 &
