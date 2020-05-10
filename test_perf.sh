echo '------------------------------------------------------'
echo '           TESTS DE PERFORMANCES HIDOOP               '
echo '------------------------------------------------------'

# Date actuelle
DATE='/bin/date'

# Paramétrage des tests
echo $'Entrez le nombre de machines à utiliser \n(les n premières de la liste du fichier de config) :'
read NBMACHINES

# Lancement des commandes
DEBUT=$($DATE +'%s')
echo $'\n## Début de l\'exécution ##\n'

echo $'\n# Arret des Daemons Hidoop et HDFS #\n'
./arret_daemons.sh
echo $'\n# Déploiement de HDFS #\n'
./deploiement_hdfs.sh $NBMACHINES
echo $'\n# Déploiement de Hidoop #\n'
./deploiement_hidoop.sh $NBMACHINES

FIN=$($DATE +'%s')

echo $'\n## Fin de l\'exécution ##\n'

# Calcul du temps d'exécution
DUREE=$(($FIN-$DEBUT))
MINUTES=$(($DUREE/60))
SECONDES=$(($DUREE%60))

echo '------------------------------------------------------'
echo "Temps d'exécution total : $MINUTES:$SECONDES (mm:ss)"
echo '------------------------------------------------------'
