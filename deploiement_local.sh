#!/bin/bash

# Script de lacement des démons en local

# Compiler les fichiers
javac -d bin src/**/*.java

# Lancer les démons (tournent en arrière-plan)
java -cp bin ordo.DaemonImpl //localhost:7654/ali 7654 &
java -cp bin ordo.DaemonImpl //localhost:7650/aymen 7650 &
java -cp bin ordo.DaemonImpl //localhost:3478/luc 3478 &
java -cp bin ordo.DaemonImpl //localhost:5481/sherwin 5481 &
java -cp bin ordo.DaemonImpl //localhost:7193/khalil 7193 &
