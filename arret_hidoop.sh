#!/bin/bash

###############################################################
###################### CONFIGURATION ##########################
###############################################################

# Arrêter les démons hidoop sur les machines distantes
ssh vador "kill \$(jps | grep DaemonImpl | awk '{print \$1}')"
ssh leia  "kill \$(jps | grep DaemonImpl | awk '{print \$1}')"
ssh tao  "kill \$(jps | grep DaemonImpl | awk '{print \$1}')"
ssh goldorak  "kill \$(jps | grep DaemonImpl | awk '{print \$1}')"
