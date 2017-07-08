import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Programme {
	private ArrayList<String> liste_liens = new ArrayList<String>(); // liste des liens sliders
	private ArrayList<String> liste_erreur = new ArrayList<String>(); // liste des erreurs
	private File filePath;
	private int compteurLignes=0;
	private int compteurErreur=0;
	private ArrayList<Morceau> liste_morceaux = new ArrayList<Morceau>();
	
	Programme(File filePath){
		this.filePath = filePath;
	}
	
	public int getCompteurLignes(){
		return this.compteurLignes;
	}
	
	public int getCompteurErreur(){
		return this.compteurErreur;
	}
	
	public ArrayList<String> getliste_liens(){
		return this.liste_liens;
	}
	
	public ArrayList<String> getliste_erreur(){
		return this.liste_erreur;
	}
	
	public void execute(Chargement chargement){
		try{ 
			BufferedReader buff = new BufferedReader(new FileReader(SliderDownloaderV2.getFirstLine(filePath))); 
			try { 
				String line;
				while ( (line = buff.readLine()) != null){	// tant que la ligne lue n'est pas nulle
				  liste_morceaux.add(new Morceau(line)); // crée un nouvel objet Morceau
				  chargement.setCompteurLignes(compteurLignes); 
				  chargement.setNom(line);
				  chargement.repaint();
				  liste_morceaux.get(compteurLignes).setSource(); // recupere code source de la page après execution du JS
				  liste_morceaux.get(compteurLignes).parse(); // parse le code source pour en extraire le lien de dl
				  if (liste_morceaux.get(compteurLignes).getSuccess()) { // si le programme a trouvé un lien
				    liste_liens.add(liste_morceaux.get(compteurLignes).getLink()); // ajoute le lien a la liste de liens
				    compteurLignes = compteurLignes + 1; //MAJ le compteur
				  }
				  else { // si le programme n'a pas trouvé de lien
				    compteurErreur = compteurErreur + 1; //MAJ les compteurs
				    compteurLignes = compteurLignes + 1;
				    liste_erreur.add(line); // ajoute le nom du morceau à la liste des erreurs
				    
				  }
				}		
					buff.close(); //Lecture fini donc on ferme le flux 
				} 
				catch (IOException e){ 
					System.out.println(e.getMessage()); 
					System.exit(1); 
				} 
		} 
		catch (IOException e) { 
			System.out.println(e.getMessage()); 
			System.exit(1); 
		} 
	}
	
	
}
