#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Entrez votre identifiant INP
id="ssabras"

# Arrêter les démons sur les machines distantes
ssh ${id}@vador ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
ssh ${id}@leia ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
ssh ${id}@luke ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
ssh ${id}@yoda ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
ssh ${id}@solo ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill
