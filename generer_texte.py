#VARIABLES A MODIFIER

# N = 5 000 000 -> taille = 1.5 Go
N = 50000

#la ou seront generes les fichiers .txt
path = "/home/lbourgui/test"



#-------------------CODE-------------------

#les lignes ecrites dans le fichier (operation repetee N fois)
l1 ="bonjour monde vie univers et autre monde\n"
l2 ="demain a aube heure ou blanchit la campagne\n"
l3 ="je partirai vois tu je sais que tu attends\n"
l4 ="irai par la foret et irai par le montagne\n"
l5 ="je ne puis demeurer loin de toi plus longtemps\n"
l6 ="je marcherai le dos courbe les mains croise\n"
l7 ="aurevoir tristesse gloire et joies\n"
l8 ="\n"
liste_ligne = [l1, l2, l3, l4, l5, l6, l7, l8]


fichier = open(path+"/data.txt", "w")
fichier.write(l1)
fichier.write(l2)
fichier.write(l3)
fichier.write(l4)
fichier.write(l5)
fichier.write(l6)
fichier.write(l7)
fichier.write(l8)
fichier.close()

fichier = open(path+"/data.txt", "a")
j = -1
for i in range(0,N-2):
    fichier.write(l1)
    fichier.write(l2)
    fichier.write(l3)
    fichier.write(l4)
    fichier.write(l5)
    fichier.write(l6)
    fichier.write(l7)
    fichier.write(l8)
    j = j+1
    if (j == 100000):
        print(i)
        j = 0
fichier.close()


x1 = l1.split()
x2 = l2.split()
x3 = l3.split()
x4 = l4.split()
x5 = l5.split()
x6 = l6.split()
x7 = l7.split()
x8 = l8.split()
total = x1+x2+x3+x4+x5+x6+x7+x8
print(total)


nb_simple = []
for y in total:
    a=[y,total.count(y)]
    nb_simple.append(a)
    
for i in range(0,len(nb_simple)):
    c = nb_simple[i][1]
    k = N*c
    nb_simple[i][1] = k


#ecriture dans le fichier resultat
fichier = open(path+"/nombre_mot.txt", "w")
for m in nb_simple:
    fichier.write(m[0]+" <-> "+str(m[1])+"\n")
fichier.close()

