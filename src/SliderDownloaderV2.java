import java.awt.Color;
import java.awt.Desktop;
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
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;



/*       						TODO
 * 	Choisir et mettres images pour les JOPTION Panel savePath
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
	
	public static void savePath(JFileChooser selecteur, File filePath){ // sauvegarde le chemin du fichier séléctionné
	    int retour=selecteur.showDialog(null, "Selectionner"); // lance boite de selection
	    if(retour==JFileChooser.APPROVE_OPTION){ // si un fichier a été séléctionné
	    	ecrire(filePath , selecteur.getSelectedFile().getAbsolutePath()); // écrit son chemin dans filePath
	    }
	}
	
	public static void savePath(JFileChooser selecteur, File filePath, JOptionPane jop){ // savePath avec obligation de choisir
	    int retour=selecteur.showDialog(null, "Selectionner"); // affiche boite de selection
	    if(retour==JFileChooser.APPROVE_OPTION){ // si un fichier a été sélectionné
	    	ecrire(filePath , selecteur.getSelectedFile().getAbsolutePath()); // écrit son chemin dans filePath
	    }
	    if (retour==JFileChooser.CANCEL_OPTION){ // si "Annuler"
	    	jop.showMessageDialog(null, "Il est obligatoire de choisir un fichier .txt pour lancer le programme", "", JOptionPane.WARNING_MESSAGE);
	    	savePath(selecteur, filePath, jop);
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
	
	
	
	static int choix=0; 
	// choix = 1 > lance le programme
	// choix = 2 > revient a la page d'accueil
	
	public static void main(String[] args) {
		
		File filePath = new File("Ressources/path.txt"); // fichier dans lequel le chemin de la liste de sons est enregistré
		File fileErreur = new File("liste_erreur.txt"); // liste des erreurs
		
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
	    JOptionPane mustChoose = new JOptionPane();
	    
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
					open(new File(getFirstLine(filePath)));
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
				System.out.println(choix);
				choix=1;
				System.out.println(choix);
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
	    
	    // 	BOUTON RETOUR À L'ACCUEIL
	    JButton btnRetour = new JButton("Retour à l'accueil");
	    btnRetour.setBounds(50, 300, 200, 25);
	    btnRetour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				choix = 2;
			}
	    });
	    
	    //	 BOUTON TELECHARGER 
		JButton btnDL = new JButton("Telecharger les morceaux");
		btnDL.setBounds(50, 190, 200, 50);
		    
// fin de l'initialisation des vues    
		
	    do{
	    	Programme prog = new Programme(filePath);
	    	btnDL.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
					if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
						for (int i=0 ; i<prog.getliste_liens().size() ; i++)  {
						open(prog.getliste_liens().get(i));
						}
						if (!prog.getliste_liens().isEmpty()){
							prog.getliste_liens().clear();
						}
					}
				}
	    	});
	    	if (getFirstLine(filePath)!=null){ // si un chemin est enregistré
				System.out.println("path.txt contient un chemin");
				File fileSons = new File(getFirstLine(filePath)); //crée un fichier à partir de ce chemin
				System.out.println("fichier fileSons crée");
				
				if (!fileSons.exists()){ // si ce fichier n'existe pas
					// ouvre boite de dialogue
					mustChoose.showMessageDialog(null, "Vous devez choisir un fichier .txt pour lancer le programme", "", JOptionPane.INFORMATION_MESSAGE);
					savePath(selecteur, filePath, mustChoose); // force l'utilisateur a choisir un fichier
				}
			}
			else{ // si aucun chemin n'est enregistré
				// ouvre boite de dialogue
				mustChoose.showMessageDialog(null, "Vous devez choisir un fichier .txt pour lancer le programme", "", JOptionPane.INFORMATION_MESSAGE);
				savePath(selecteur, filePath, mustChoose); // force l'utilisateur a choisir un fichier
			}
	    
	    fenetre.setContentPane(accueil); //affiche l'accueil
	    // positionne les différents bouton de l'accueil
		fenetre.getContentPane().setLayout(null);
	    fenetre.getContentPane().add(btnOpen);
	    fenetre.getContentPane().add(btnSelect);
	    fenetre.getContentPane().add(btnLaunch);
	    fenetre.setVisible(true);
	    
	    while (choix!=1){ // tant que le le bouton lancer le programme n'a pas été pressé
	    	System.out.println("choix pas égal a 1"); 
	    } // sort quand le bouton a été pressé
	    
	    
	    fenetre.setContentPane(chargement); // afficher l'écran de chargement
	    fenetre.validate();
	    
	    prog.execute(chargement); // lancement du programme
	    
	    // le programme est terminé
		ecrire(fileErreur, prog.getliste_erreur(), "\n"); // ecrit la liste des erreurs dans le fichier liste_erreur			
		playSound("Ressources/ah_denis.wav"); // joue le fameux "AH" de Denis Brogniart
		ecrire(new File(getFirstLine(filePath)) , ""); // vide la liste des de l'utilisateur
					
		end.setCompteurLignes(prog.getCompteurLignes());
		end.setCompteurErreur(prog.getCompteurErreur());
					
		fenetre.setContentPane(end); //affiche l'écran de fin
		fenetre.getContentPane().setLayout(null);			
		// ajoute les différents boutons à l'écran de fin
		fenetre.getContentPane().add(btnDL);
		fenetre.getContentPane().add(btnErreur);
		fenetre.getContentPane().add(btnRetour);
		fenetre.validate();
							
		while (choix!=2){ // tant que l'utilisateur n'a pas cliqué sur "retour à l'accueil"
	    	System.out.println("choix pas égal a 2"); // reste dans la boucle
	    } // sort de la boucle quand l'utilisateur a cliqué, et relance donc le "do...while"
		}while(choix==2);
	
	}
}
