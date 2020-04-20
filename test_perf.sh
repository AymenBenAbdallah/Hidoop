echo '------------------------------------------------------'
echo '           TESTS DE PERFORMANCES HIDOOP               '
echo '------------------------------------------------------'

# Date actuelle
DATE='/bin/date'

# Paramétrage des tests
# echo "Entrez ... : "
# read PARAMETRE

# Lancement des commandes
DEBUT=$($DATE +'%s')
echo $'\n## Début de l\'exécution ##\n'

sh arret_hdfs.sh
sh arret_hidoop.sh
sh deploiement_hdfs.sh
sh deploiement_hidoop.sh

FIN=$($DATE +'%s')

echo $'\n## Fin de l\'exécution ##\n'

# Calcul du temps d'exécution
DUREE=$(($FIN-$DEBUT))
MINUTES=$(($DUREE/60))
SECONDES=$(($DUREE%60))

echo '------------------------------------------------------'
echo "Temps d'exécution total : $MINUTES:$SECONDES (mm:ss)"
echo '------------------------------------------------------'
