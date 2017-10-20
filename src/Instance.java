import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class Instance implements Runnable {

	private String line;
	private Fichier fileErreur;
	private Fichier fileSuccess;
	
	private static void open(String url){
		URI uri;
			try {
				uri = new URI(url);
				try
				{
					java.awt.Desktop.getDesktop().browse(uri);
				}
				catch (IOException exc)
				{ 
			    	System.out.println("Exception: " + exc.toString());
				}
			} catch (URISyntaxException e) {
				ecrire(new Fichier("logs.txt"), "OPENING ::  " + url, false);
				ecrire(new Fichier("logs.txt"), "::EEE::  " + e, false);
			}
	}
	public Instance(String line, Fichier fileErreur, Fichier fileSuccess) {
		this.line = line;
		this.fileErreur = fileErreur;
		this.fileSuccess= fileSuccess;
	}
			
	static void ecrire(Fichier fichier, String str, Boolean delete){ // ECRITURE d'un string dans fichier
		try {
			fichier.createNewFile(); // crée le fichier s'il n'existe pas
            final FileWriter writer = new FileWriter(fichier.getFile().getAbsolutePath(), !delete); // création d'un writer
            try {        	
            		writer.write(str);	
            }
            	 finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
	}	
	
	public void run() {
		//liste_morceaux.add(new Morceau(line)); // crée un nouvel objet Morceau
		  //chargement.setCompteurLignes(compteurLignes); 
		 // chargement.setNom(line);
		  //chargement.repaint();
		Morceau Morceau = new Morceau(this.line);
		Morceau.setSource(); // recupere code source de la page après execution du JS
		Morceau.parse(); // parse le code source pour en extraire le lien de dl
		if (Morceau.getSuccess()) { // si le programme a trouvé un lien
			//ecrire(fileSuccess, Morceau.getLink()); // ajoute le lien a la liste de liens
			//compteurLignes = compteurLignes + 1; //MAJ le compteur
			open(Morceau.getLink());
		 }
		  else { // si le programme n'a pas trouvé de lien
		    //compteurErreur = compteurErreur + 1; //MAJ les compteurs
		    //compteurLignes = compteurLignes + 1;
			  ecrire(fileErreur, Morceau.getLink(), false); // ajoute le nom du morceau à la liste des erreurs
		    
		  }
	}

}
