import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/*       						TODO
 * 	selecteur CAS path incorrect / inexistant : 
	annuler >>> AFFICHE JOPTION PANE + relancer savePath 
 *  REGLER SOUCIS DOUBLES FENETRES (CHEEDRA) -- possiblement fix, en attente retour chidra
 *  MAJ ReadMe -- BUT + Comment l'utiliser (archive ...) + comment il fonctionne?
 *  Description sur git
 *  Passer repo en Public
 *  nettoyer le code (System.out.println), (import non utilisés)...
 *  Exporter FINAL JAR
 * 								TODO	
 */ 

public class SliderDownloaderV2 {
	
	public static void ecrire(File fichier, ArrayList<String> array, String separateur){ // ECRITURE de array dans fichier
		try {
			fichier.createNewFile(); // crée le fichier s'il n'existe pas
            final FileWriter writer = new FileWriter(fichier.getAbsolutePath()); // création d'un writer
            try {        	
            	for (int i=0 ; i<array.size() ; i++)  {
            		writer.write(array.get(i));	
            		writer.write(separateur);
            	}
            }
            	 finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
	}	
	
	public static void ecrire(File fichier, String str){ // ECRITURE d'un string dans fichier
		try {
			fichier.createNewFile(); // crée le fichier s'il n'existe pas
            final FileWriter writer = new FileWriter(fichier.getAbsolutePath()); // création d'un writer
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
	
	public static String getFirstLine(File file){
		String line="init";
		try{ 
			
			BufferedReader buff = new BufferedReader(new FileReader(file.getAbsolutePath())); 
			
			try {  
			line = buff.readLine();
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
		
		return line;
		} 
	
	public static void playSound(String morceau){ // joue un son (string=path du fichier)
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
	
	public static int getFileSize(String path){ // recupère le poids d'un fichier depuis son URL web
		try {
		        URL url=new URL(path);
		        URLConnection urlConnection=url.openConnection();
		        urlConnection.connect();
		        int file_size=urlConnection.getContentLength();
		        return  file_size;

		    } catch (MalformedURLException e) {

		    }catch (IOException e){

		    }
		    return -1;
		}
	
	public static void savePath(JFileChooser selecteur, File filePath){
	    int retour=selecteur.showOpenDialog(null);
	    if(retour==JFileChooser.APPROVE_OPTION){
	    	ecrire(filePath , selecteur.getSelectedFile().getAbsolutePath()); // sauvegarde le chemin
	    }
	}
	
	public static void open(File file){
		try
		{
			java.awt.Desktop.getDesktop().open(file);
		}
		catch (IOException exc)
		{
	    	System.out.println("Exception: " + exc.toString());
		}
	}
	
	public static void open(String url){
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
			e.printStackTrace();
		}
		
	}
	
	
	
	// ARRAYS pour execution du programme
	
	static ArrayList<String> liste_liens = new ArrayList<String>(); // liste des liens sliders
	static ArrayList<String> liste_erreur = new ArrayList<String>(); // liste des erreurs
	
	static int choix=0;
	
	public static void main(String[] args) {
		
		//INITIALISATION
		
		File filePath = new File("Ressources/path.txt");
		File fileErreur = new File("liste_erreur.txt");
		

		//FENETRE
		JFrame fenetre = new JFrame(); 
	    fenetre.setTitle("SliderDownloader"); 
	    fenetre.setBackground(Color.BLACK);
	    fenetre.setSize(300, 400);
	    fenetre.setLocationRelativeTo(null); // PLACE LA FENETRE AU CENTRE
	    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stoppe le programme a la fermeture (croix rouge) 
	    fenetre.setResizable(false);
	    Accueil accueil = new Accueil();
	    
	    // SELECTEUR DE .TXT
	    JFileChooser selecteur = new JFileChooser();
		FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers .txt", "txt");
        selecteur.addChoosableFileFilter(filtre);
        selecteur.setAcceptAllFileFilterUsed(false);
        selecteur.setFileFilter(filtre);
        selecteur.setDialogTitle("Choississez une liste de sons au format .txt");
	    
	    //BOUTON OUVRIR LE FICHIER
	    JButton btnOpen = new JButton("Ouvrir le fichier");
	    btnOpen.setBounds(50, 170, 200, 20);
	    btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)){
					open(new File(getFirstLine(filePath)));
				} 
				
			}
	         });
	    
	    
	    //BOUTON SELECTIONNER UN FICHIER
	    JButton btnSelect = new JButton("Selectionner un autre fichier");
	    btnSelect.setBounds(50, 200, 200, 20);
	    btnSelect.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				savePath(selecteur, filePath);
			}
	         });
	    
	    
	    // BOUTON LANCER LE PROGRAMME
	    JButton btnLaunch = new JButton("Analyser les morceaux");
	    btnLaunch.setBounds(50, 230, 200, 70); 
	    btnLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				System.out.println(choix);
				choix=1;
				System.out.println(choix);
			}
	         });
	    
	 // BOUTON OUVRIR LISTE ERREUR
		JButton btnErreur = new JButton("Ouvrir la liste des erreurs");
		btnErreur.setBounds(50, 260, 200, 25);
	    btnErreur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)){
					open(fileErreur);
				}
			}
	         });
	    
	    
	    // BOUTON retour a l'accueil
	    JButton btnRetour = new JButton("Retour à l'accueil");
	    btnRetour.setBounds(50, 300, 200, 25);
	    btnRetour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				choix = 2;
			}
	         });
	    
	 // BOUTON TELECHARGER 
		JButton btnDL = new JButton("Telecharger les morceaux");
			btnDL.setBounds(50, 190, 200, 50);
		    btnDL.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
					if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
						for (int i=0 ; i<liste_liens.size() ; i++)  {
						open(liste_liens.get(i));
						}
					}
				}
		         });
		    
		    
	    do{
	    	if (getFirstLine(filePath)!=null){
				System.out.println("jesuisla");
				File fileSons = new File(getFirstLine(filePath));
				if (!fileSons.exists()){
					savePath(selecteur, filePath);
				}
			}
			else{
				savePath(selecteur, filePath);
			}
	    fenetre.setContentPane(accueil); //affiche l'accueil
		fenetre.getContentPane().setLayout(null);
	    fenetre.getContentPane().add(btnOpen);
	    fenetre.getContentPane().add(btnSelect);
	    fenetre.getContentPane().add(btnLaunch);
	    fenetre.setVisible(true);
	    
	    while (choix!=1){
	    	System.out.println("choix pas égal a 1"); 
	    }
	    Chargement chargement = new Chargement();
	    fenetre.setContentPane(chargement); //afficher l'écran de chargement
	    fenetre.validate();
	    
	 // PROGRAMME COMMENCE ICI
    	System.out.println("LE PROGRAMME COMMENCE"); // pour test
		try{ 

			BufferedReader buff = new BufferedReader(new FileReader(getFirstLine(filePath))); 

			try { 
				String line;
				int compteurLignes=0;
				int compteurErreur=0;
				ArrayList<Morceau> liste_morceaux = new ArrayList<Morceau>();

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
					ecrire(fileErreur, liste_erreur, "\n"); // ecrit la liste des erreurs dans le fichier liste_erreur			
					playSound("Ressources/ah_denis.wav"); // joue un son
					ecrire(new File(getFirstLine(filePath)) , ""); // vide la liste des de l'utilisateur
					Fin end = new Fin(); 
					end.setCompteurLignes(compteurLignes);
					end.setCompteurErreur(compteurErreur);
					fenetre.setContentPane(end); //affiche l'écran de fin
					fenetre.getContentPane().setLayout(null);
					
					
					    fenetre.getContentPane().add(btnDL);
					
					
					 fenetre.getContentPane().add(btnErreur);
				    fenetre.getContentPane().add(btnRetour);
				    
				fenetre.validate();
					
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
		while (choix!=2){
	    	System.out.println("choix pas égal a 2"); 
	    }
		}while(choix==2);

	    	
	    
	    
		
	}

}
