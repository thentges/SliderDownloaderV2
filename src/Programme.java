import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Programme implements Runnable {
	//private ArrayList<String> liste_liens = new ArrayList<String>(); // liste des liens sliders
	//private ArrayList<String> liste_erreur = new ArrayList<String>(); // liste des erreurs
	private static Fichier filePath;
	private Fichier fileSuccess;
	private Fichier fileErreur;
	//private int compteurLignes=0;
	//private int compteurErreur=0;
	private Chargement chargement;
	private ArrayList<Runnable> runnables = new ArrayList<Runnable>();
	//private ArrayList<Morceau> liste_morceaux = new ArrayList<Morceau>();
	private static void playSound(String morceau){ // joue un son (string=path du fichier)
		try {
		 	File son = new File(morceau);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(son);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	static void ecrire(Fichier fichier, String str, Boolean effacer){ // ECRITURE d'un string dans fichier
		try {
			fichier.createNewFile(); // crée le fichier s'il n'existe pas
            final FileWriter writer = new FileWriter(fichier.getFile().getAbsolutePath(), !effacer); // création d'un writer
            try {        	
            		writer.write(str);
            		writer.write("\n");
            }
            	 finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
	}	
	
	
	private static void executeRunnables(final ExecutorService service, ArrayList<Runnable> runnables){
        //On exécute chaque "Runnable" de la liste "runnables"
		for(Runnable r : runnables){
			service.execute(r);
		}
		
		service.shutdown();
		// le programme est terminé
		//ecrire(fileErreur, prog.getliste_erreur(), "\n"); // ecrit la liste des erreurs dans le fichier liste_erreur			
		//playSound("Ressources/ah_denis.wav"); // joue le fameux "AH" de Denis Brogniart
		//ecrire(new Fichier(filePath.getFirstLine()) , "", true); // vide la liste des de l'utilisateur
		/*			
		//end.setCompteurLignes(prog.getCompteurLignes());
		//end.setCompteurErreur(prog.getCompteurErreur());
							
		fenetre.setContentPane(end); //affiche l'écran de fin
		fenetre.getContentPane().setLayout(null);			
		// ajoute les différents boutons à l'écran de fin
		fenetre.getContentPane().add(btnDL);
		fenetre.getContentPane().add(btnErreur);
		fenetre.getContentPane().add(btnRetour);
		fenetre.validate();*/
	}		
	Programme(Fichier filePath, Fichier fileErreur, Fichier fileSuccess,Chargement chargement){
		this.filePath = filePath;
		this.fileSuccess = fileSuccess;
		this.fileErreur = fileErreur;
		this.chargement = chargement;
	
	}
	
	//public int getCompteurLignes(){
	//	return this.compteurLignes;
	/*}
	
	public int getCompteurErreur(){
		return this.compteurErreur;
	}*/
	public void run(){
		execute(this.chargement);
	}
	public void execute(Chargement chargement){
		try{ 
			BufferedReader buff = new BufferedReader(new FileReader(filePath.getFirstLine())); 
			try { 
				String line;
				while ( (line = buff.readLine()) != null){	// tant que la ligne lue n'est pas nulle
					runnables.add(new Instance(line, fileErreur, fileSuccess));
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
		//Pool
		ExecutorService execute = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		executeRunnables(execute, runnables);	
	}	
}
