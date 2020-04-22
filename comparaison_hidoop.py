# code pour comparer le fichier txt "résultat attendu" : mot <-> nombre de présence 
# à celui "résultat obtenu" : mot <-> nombre de présence calculée par hidoop


#VARIABLES A MODIFIER

#là où sont placés les fichiers .txt
path_attendu = "/home/lbourgui/test/data_attendu.txt"
path_obtenu = "/home/lbourgui/test/data_obtenu.txt"

#windows
#path_attendu = "C:/Users/Luc/Documents/COURS N7/2SN/hidoop/python/data_attendu.txt"
#path_obtenu = "C:/Users/Luc/Documents/COURS N7/2SN/hidoop/python/data_obtenu.txt"



#-----------------fonctions---------------

def chercher(mot, lignes_attendu):
    
    for i in range(0,len(lignes_attendu)):
        if mot == lignes_attendu[i][0]:
            return lignes_attendu[i][1]
    return 0
    
#-------------------CODE-------------------

#stocker l'ensemble des attendu puis resultat dans des variables

fichier = open(path_attendu, "r")
lignes_attendu1 = fichier.readlines()
fichier.close()

lignes_attendu = []
for l1 in lignes_attendu1:
    x = l1.split()
    lignes_attendu.append([x[0],int(x[2])])

fichier1 = open(path_obtenu, "r")
lignes_obtenu1 = fichier1.readlines()
fichier.close()

lignes_obtenu = []
for l2 in lignes_obtenu1:
    y = l2.split()
    lignes_obtenu.append([y[0],int(y[2])])
    
    
#chercher des différences

different = False
for j in range(0,len(lignes_obtenu)):
    nb_attendu = chercher(lignes_obtenu[j][0], lignes_attendu)
    nb_obtenu = lignes_obtenu[j][1]
    #print(" / nb_attendu : "+ str(nb_attendu))
    #print("/ nb_obtenu : " + str(nb_obtenu) + "\n")
    if nb_attendu != nb_obtenu:
        print(" Il ya une différence entre les deux fichier :")
        print("Mot : " + str(lignes_obtenu[j][0]))
        print("nb_attendu : "+ str(nb_attendu))
        print("nb_obtenu : " + str(nb_obtenu) + "\n")
        different = True

if different == False:
    print("Les nombres de mots sont identiques")