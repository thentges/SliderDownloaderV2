# SliderDownloaderV2 

## BUT
SliderDownloader est un programme qui permet de télécharger une liste de morceaux à partir du site slider.kz

## UTLISATION SUR VOTRE MACHINE
Pour l'utiliser, il faut télécharger l'archive "SliderDownloader.zip" qui, une fois décompressée vous donnera un dossier contenant toutes les ressources nécessaires à l'execution du .jar contenu dans cette archive. Le .jar doit impérativement rester dans le même dossier que le sous-dossier Ressources, et la liste d'erreurs.

Si vous avez pull la totalité du projet github, il n'est pas nécessaire de décompresser SliderDownloader.zip, il suffit de lancer le .jar déjà présent dans le dossier.

## FONCTIONNEMENT
Pour utiliser SliderDownloader, il faut remplir un fichier .txt au format :
Artiste 1 - Morceau 1
Artiste 2 - Morceau 2
Artiste 3 - Morceau 3
...

Lorsqu'un fichier est séléctionné, il est "sauvegardé" par le programme, qui utilisera ce même fichier à chaque fois que vous le lancerez, tant qu'un autre fichier n'est pas séléctionné.

Une fois ce fichier séléctionné via le programme, il se chargera d'aller chercher sur slider.kz la liste des liens disponibles pour chaque morceau et séléctionnera le "meilleur lien", c'est à dire, celui de meilleure qualité audio.
Lorsque le programme a terminé de tourner, vous avez la possibilité d'ouvrir une "liste des erreurs", il s'agit des morceaux pour lesquels slider.kz ne possède aucun lien.


