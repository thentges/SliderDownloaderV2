import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;



/*       						TODO
 * 
 * 	Choisir et mettres images pour les JOptionPane.
 *  nettoyer le code (system.out.println de debug)
 *  
 *  TODO OPTION POUR ALBUMS 
 *  https://www.allmusic.com/{{nomalbumartisteetc (fichier txt}}
 *  chopper lien vers le good album (premier surement)
 *  chopper la list sur la page du good album
 *  REQUETES HTTP CLASSIQUE SI POSSIBLE. (+ rapide, no JS, verifier pas de JS)
 *  
 *  TODO BOUTON OUVRIR DOSSIER DE DL A LA PLACE DE TELECHARGER LES SONS TODO telecharger les sons pas analyser
 *  
 *  mieux parser pour vrmt avoir le bon lien
 *  menu stylé pdt chargement avec liste sons statut etc
 *  
 * 								TODO	
 */ 


public class SliderDownloaderV2 {
	public static Fichier logs = new Fichier("logs.txt");
	
	
	private static void ecrire(Fichier fichier, ArrayList<String> array, String separateur){ // ECRITURE de array dans fichier
		try {
			fichier.createNewFile(); // crée le fichier s'il n'existe pas
            final FileWriter writer = new FileWriter(fichier.getFile().getAbsolutePath()); // création d'un writer
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
	
	private static void savePath(JFileChooser selecteur, Fichier filePath){ // sauvegarde le chemin du fichier séléctionné
	    int retour=selecteur.showDialog(null, "Selectionner"); // lance boite de selection
	    if(retour==JFileChooser.APPROVE_OPTION){ // si un fichier a été séléctionné
	    	ecrire(filePath , selecteur.getSelectedFile().getAbsolutePath(), true); // écrit son chemin dans filePath
	    	filePath.majFirstLine();
	    }
	}
	
	private static void mustSavePath(JFileChooser selecteur, Fichier filePath){ // savePath avec obligation de choisir
	    int retour=selecteur.showDialog(null, "Selectionner"); // affiche boite de selection
	    if(retour==JFileChooser.APPROVE_OPTION){ // si un fichier a été sélectionné
	    	ecrire(filePath , selecteur.getSelectedFile().getAbsolutePath(), true); // écrit son chemin dans filePath
	    	filePath.majFirstLine();
	    }
	    if (retour==JFileChooser.CANCEL_OPTION){ // si "Annuler"
	    	Object[] options = {"Selectionner un fichier", "Quitter le programme"};
			int optionChoisie = JOptionPane.showOptionDialog(null,
			"Il est obligatoire de choisir un fichier .txt pour lancer le programme",
			"",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,     //champ a remplacer pour image
			options,  //titres des boutons
			options[0]); //titre du bouton par defaut
			if (optionChoisie==0){ // si l'utilisateur a cliqué sur selectionner un fichier
				mustSavePath(selecteur, filePath);	
			}
	    	if (optionChoisie==1  || optionChoisie==-1){ // s'il a cliqué sur quitter le programme
	    		System.exit(0);
	    	}
	    }
	}
	
	private static void open(Fichier file){
		try
		{
			java.awt.Desktop.getDesktop().open(file.getFile());
		}
		catch (IOException exc)
		{
	    	System.out.println("Exception: " + exc.toString());
		}
	}
	
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
				ecrire(logs, "OPENING ::  " + url, false);
				ecrire(logs, "::EEE::  " + e, false);
			}
	}
	
	
	static int choix=0; 
	// choix = 1 > lance le programme
	// choix = 2 > revient a la page d'accueil
	
	public static void main(String[] args) {
		Fichier filePath = new Fichier("Ressources/path.txt"); // fichier dans lequel le chemin de la liste de sons est enregistré
		Fichier fileErreur = new Fichier("liste_erreur.txt"); // liste des erreurs
		ecrire(fileErreur, "", true);
		Fichier fileSuccess = new Fichier("Ressources/liste_success.txt"); // liste des liens
		ecrire(fileSuccess, "", true);
// initialisation des vues
		
		//	FENETRE
		JFrame fenetre = new JFrame(); 
	    fenetre.setTitle("SliderDownloader"); 
	    fenetre.setBackground(Color.BLACK);
	    fenetre.setSize(300, 400);
	    fenetre.setLocationRelativeTo(null); // PLACE LA FENETRE AU CENTRE
	    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stoppe le programme a la fermeture (croix rouge) 
	    fenetre.setResizable(false);
	    
	    //	PANELS
	    Accueil accueil = new Accueil(); // création écran accueil
	    Chargement chargement = new Chargement(); // création écran de chargement
	    Fin end = new Fin(); // création écran de fin
	    
	    //	BOITE DE DIALOGUES
	    //JOptionPane mustChoose = new JOptionPane();
	    
	    //	SELECTEUR DE .TXT
	    JFileChooser selecteur = new JFileChooser();
		FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers .txt", "txt");
		selecteur.setMultiSelectionEnabled(false);
        selecteur.addChoosableFileFilter(filtre);
        selecteur.setAcceptAllFileFilterUsed(false);
        selecteur.setFileFilter(filtre);
        selecteur.setDialogTitle("Choississez une liste de sons au format .txt");
        
	    
	    //	BOUTON OUVRIR LE FICHIER
	    JButton btnOpen = new JButton("Ouvrir le fichier");
	    btnOpen.setBounds(50, 170, 200, 20);
	    btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)){
					open(new Fichier(filePath.getFirstLine()));
				} 	
			}
	    });
	    
	    
	    //	BOUTON SELECTIONNER UN FICHIER
	    JButton btnSelect = new JButton("Selectionner un autre fichier");
	    btnSelect.setBounds(50, 200, 200, 20);
	    btnSelect.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				savePath(selecteur, filePath);
			}
	    });
	    
	    
	    //	BOUTON LANCER LE PROGRAMME
	    JButton btnLaunch = new JButton("Analyser les morceaux");
	    btnLaunch.setBounds(50, 230, 200, 70); 
	    btnLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				//System.out.println(choix);
				choix=1;
				//System.out.println(choix);
			}
	    });
	    
	    //	BOUTON OUVRIR LISTE ERREUR
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
	    
	    //	BOUTON RETOUR À L'ACCUEIL
	    JButton btnRetour = new JButton("Retour à l'accueil");
	    btnRetour.setBounds(50, 300, 200, 25);
	    btnRetour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				choix = 2;
			}
	    });
	    
	    //	BOUTON OUVRIR LE DOSSIER DE DL
		JButton btnDL = new JButton("Voir le dossier");
		btnDL.setBounds(50, 190, 200, 50);
		    
// fin de l'initialisation des vues    
		
	    do{
	    	Programme prog = new Programme(filePath, fileErreur, fileSuccess, chargement);
	    	btnDL.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
					if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
						//playSound("Ressources/ah_denis.wav"); // joue le fameux "AH" de Denis Brogniart
						// ouvrir dossier ici
						try {
							java.awt.Desktop.getDesktop().browse(new URI("Downloads/"));
						} catch (IOException | URISyntaxException e1) {}
					}
				}
	    	});
	    	if (filePath.getFirstLine()!=null){ // si un chemin est enregistré
				System.out.println("path.txt contient un chemin");
				File fileSons = new File(filePath.getFirstLine()); //crée un fichier à partir de ce chemin
				System.out.println("fichier fileSons crée");
				
				if (!fileSons.exists()){ // si ce fichier n'existe pas
					// ouvre boite de dialogue
					JOptionPane.showMessageDialog(null, "Vous devez choisir un fichier .txt pour lancer le programme", "", JOptionPane.INFORMATION_MESSAGE);
					mustSavePath(selecteur, filePath); // force l'utilisateur a choisir un fichier
				}
			}
			else{ // si aucun chemin n'est enregistré
				// ouvre boite de dialogue
				JOptionPane.showMessageDialog(null, "Vous devez choisir un fichier .txt pour lancer le programme", "", JOptionPane.INFORMATION_MESSAGE);
				mustSavePath(selecteur, filePath); // force l'utilisateur a choisir un fichier
			}
	    
	    fenetre.setContentPane(accueil); //affiche l'accueil
	    // positionne les différents bouton de l'accueil
		fenetre.getContentPane().setLayout(null);
	    fenetre.getContentPane().add(btnOpen);
	    fenetre.getContentPane().add(btnSelect);
	    fenetre.getContentPane().add(btnLaunch);
	    fenetre.setVisible(true);
	    fenetre.validate();
	    
	    while (choix!=1){ // tant que le le bouton lancer le programme n'a pas été pressé
	    	System.out.println("!1"); 
	    } // sort quand le bouton a été pressé
	    
	    
	    fenetre.setContentPane(chargement); // afficher l'écran de chargement
	    fenetre.validate();
	    
	    prog.getExecutorService().execute(prog);
	    //ExecutorService ES = Executors.newSingleThreadExecutor();
	    //ES.execute(prog); // lance le programme
	    while(!prog.getExecutorService().isTerminated()){
	    	int wed = 420;
	    }
	    //prog.execute(chargement); // lancement du programme
	    
	    // le programme est terminé
		//ecrire(fileErreur, prog.getliste_erreur(), "\n"); // ecrit la liste des erreurs dans le fichier liste_erreur			
		playSound("Ressources/ah_denis.wav"); // joue le fameux "AH" de Denis Brogniart
		ecrire(new Fichier(filePath.getFirstLine()) , "", true); // vide la liste des de l'utilisateur
					
		//end.setCompteurLignes(prog.getCompteurLignes());
		//end.setCompteurErreur(prog.getCompteurErreur());
					
		fenetre.setContentPane(end); //affiche l'écran de fin
		fenetre.getContentPane().setLayout(null);			
		// ajoute les différents boutons à l'écran de fin
		fenetre.getContentPane().add(btnDL);
		fenetre.getContentPane().add(btnErreur);
		fenetre.getContentPane().add(btnRetour);
		fenetre.validate();
							
		while (choix!=2){ // tant que l'utilisateur n'a pas cliqué sur "retour à l'accueil"
	    	System.out.println("!2");// reste dans la boucle
	    } // sort de la boucle quand l'utilisateur a cliqué, et relance donc le "do...while"
		}while(choix==2);
	
	}
}
