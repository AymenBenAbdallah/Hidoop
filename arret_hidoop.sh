#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Entrez votre identifiant INP
id="ssabras"

# Arrêter les démons hidoop sur les machines distantes
ssh ${id}@vador jps | grep DaemonImpl | awk '{print $1}' | xargs kill
ssh ${id}@leia jps | grep DaemonImpl | awk '{print $1}' | xargs kill
ssh ${id}@luke jps | grep DaemonImpl | awk '{print $1}' | xargs kill
ssh ${id}@yoda jps | grep DaemonImpl | awk '{print $1}' | xargs kill
ssh ${id}@solo jps | grep DaemonImpl | awk '{print $1}' | xargs kill
