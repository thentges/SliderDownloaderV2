import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;


public class Instance implements Runnable {

	private String line;
	private Fichier fileErreur;
	private Fichier fileSuccess;
	private Chargement chargement;
	
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
				ecrire(SliderDownloaderV2.logs, "OPENING ::  " + url, false);
				ecrire(SliderDownloaderV2.logs, "::EEE::  " + e, false);
			}
	}
	public Instance(String line, Fichier fileErreur, Fichier fileSuccess, Chargement chargement) {
		this.line = line;
		this.fileErreur = fileErreur;
		this.fileSuccess= fileSuccess;
		this.chargement=chargement;
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
			//open(Morceau.getLink());
			URL website = null;
			try {
				website = new URL(Morceau.getLink());
			} catch (MalformedURLException e) {}
			ReadableByteChannel rbc = null;
			try {
				rbc = Channels.newChannel(website.openStream());
			} catch (IOException e1) {}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("Downloads/"+this.line + ".mp3");
			} catch (FileNotFoundException e) {}
			try {
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e) {}
			chargement.plusCompteurLignes();
		 }
		  else { // si le programme n'a pas trouvé de lien
			  ecrire(fileErreur, this.line, false); // ajoute le nom du morceau à la liste des erreurs
		  }
	}

}
