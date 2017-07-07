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
 *  REGLER SOUCIS CHEEDRA setBackground(Color.BLACK); fonctionne AP
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
	
	public static String sliderify(String line){
		line=line.replace(" ", "+");
		String lien="http://slider.kz/?page=1&act=source1&q=" + line;
		return lien;
	}
	
	public static void FileChooser(File filePath){
		JFileChooser selecteur = new JFileChooser();
		FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers .txt", "txt");
        selecteur.addChoosableFileFilter(filtre);
        selecteur.setAcceptAllFileFilterUsed(false);
        selecteur.setFileFilter(filtre);
        selecteur.setDialogTitle("Choississez une liste de sons au format .txt");
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
	
	static ArrayList<String> liste_sons = new ArrayList<String>(); // liste des liens sliders
	static ArrayList<String> liste_liens = new ArrayList<String>(); // liste pour trier et garder le meme lien
	static ArrayList<String> liste_erreur = new ArrayList<String>(); // liste des erreurs
	static int choix=0;
	
	public static void main(String[] args) {
		do{
		//INITIALISATION
		
		File filePath = new File("Ressources/path.txt");
		File fileErreur = new File("liste_erreur.txt");
		if (getFirstLine(filePath)!=null){
			System.out.println("jesuisla");
			File fileSons = new File(getFirstLine(filePath));
			if (!fileSons.exists()){
				FileChooser(filePath);
			}
		}
		else{
			FileChooser(filePath);
		}

		//FENETRE
		JFrame fenetre = new JFrame(); 
	    fenetre.setTitle("SliderDownloader");  
	    fenetre.setSize(300, 400);
	    fenetre.setLocationRelativeTo(null); // PLACE LA FENETRE AU CENTRE
	    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stoppe le programme a la fermeture (croix rouge) 
	    fenetre.setResizable(false);
	    Accueil accueil = new Accueil();
	    fenetre.setContentPane(accueil); //afficher l'accueil
	    fenetre.getContentPane().setLayout(null);
	    
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
	    fenetre.getContentPane().add(btnOpen);
	    
	    //BOUTON SELECTIONNER UN FICHIER
	    JButton btnSelect = new JButton("Selectionner un autre fichier");
	    btnSelect.setBounds(50, 200, 200, 20);
	    btnSelect.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				FileChooser(filePath);
			}
	         });
	    fenetre.getContentPane().add(btnSelect);
	    
	    // BOUTON LANCER LE PROGRAMME
	    JButton btnLaunch = new JButton("Analyser les morceaux");
	    btnLaunch.setBounds(50, 230, 200, 70);
	    fenetre.getContentPane().add(btnLaunch);
	    btnLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				System.out.println(choix);
				choix=1;
				System.out.println(choix);
			}
	         });
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
				String nom; 
				int debut;
				int fin;
				int compteurLignes=0;
				int compteurErreur=0;
					// Lire le fichier ligne par ligne 
					// La boucle se termine quand la méthode affiche "null" 
					while ((nom = buff.readLine()) != null) {	
						chargement.setNom(nom);
						String end = nom.substring(nom.length()-4) +".mp3";
						String lien = sliderify(nom);
						// se connecte a slider et attends l'execution du JS.
						java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
						WebClient webClient = new WebClient(BrowserVersion.CHROME);   
						webClient.getOptions().setJavaScriptEnabled(true);
						webClient.getOptions().setThrowExceptionOnScriptError(false);
						webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);					
						HtmlPage page = webClient.getPage(lien);
						webClient.waitForBackgroundJavaScriptStartingBefore(1000); 
						String pageAsXml = page.asXml(); // recupere le code source
						// parsing des différents liens dispo
						debut = pageAsXml.indexOf("playlist_gen"); 
						pageAsXml = pageAsXml.substring(debut);
						fin = pageAsXml.indexOf("</div>");
						pageAsXml = pageAsXml.substring(0, fin);
						//parsing du premier lien
						debut = pageAsXml.indexOf("/download/");
						fin = pageAsXml.indexOf(".mp3");
						fin = fin + 4;
						if (debut !=-1 && fin !=3){ 
							String first = pageAsXml.substring(debut, fin);
							first="http://slider.kz" + first;
							liste_liens.add(first);
							int compteur=0;
							// parsing des autres liens
							while (debut!=-1 && fin!=-1){
								compteur=compteur+1;
								if (compteur==8){ // 8 liens max traités
									break;
								} 
								debut = pageAsXml.indexOf("/download/" , debut+10);
								fin = pageAsXml.indexOf(".mp3" , fin);
								fin = fin + 4;
								if (debut != -1 && fin !=3){
									String second = pageAsXml.substring(debut, fin);
									second = "http://slider.kz" + second;
									liste_liens.add(second);
								}
							}
							int comparateur = 0;
							int index = -1;
							for (int i=0 ; i<liste_liens.size() ; i++)  {
								int size = getFileSize(liste_liens.get(i));
								if (comparateur < size && liste_liens.get(i).endsWith(end)) { 
									comparateur = size;
									index = i ;
								}
								if (index==-1 && comparateur < size){ // si aucun fichier ne possède la bonne fin de nom prendre le plus gros de tous
									comparateur=size;
									index = i;
								}
							}
							String link = liste_liens.get(index); // recupere le plus grand lien
							link = link.replace(" ", "%20"); // vire les espace dans le lien
							liste_sons.add(link);
							liste_liens.clear();
							compteurLignes=compteurLignes+1;
							chargement.setCompteurLignes(compteurLignes);
						}
						else { 
							compteurErreur=compteurErreur+1;
							compteurLignes=compteurLignes+1;
							chargement.setCompteurLignes(compteurLignes);
							liste_erreur.add(nom);
	        
						}
						webClient.close();
					} 
					ecrire(fileErreur, liste_erreur, "\n");		// rempli liste erreur				
					playSound("Ressources/ah_denis.wav");
					ecrire(new File(getFirstLine(filePath)) , ""); // vide la liste des sons
					Fin end = new Fin();
					end.setCompteurLignes(compteurLignes);
					end.setCompteurErreur(compteurErreur);
					fenetre.setContentPane(end); //afficher l'écran de chargement
					fenetre.getContentPane().setLayout(null);
					// BOUTON TELECHARGER 
					JButton btnDL = new JButton("Telecharger les morceaux");
						btnDL.setBounds(50, 190, 200, 50);
					    btnDL.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
								if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
									for (int i=0 ; i<liste_sons.size() ; i++)  {
									open(liste_sons.get(i));
									}
								}
							}
					         });
					    fenetre.getContentPane().add(btnDL);
					
					
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
				    fenetre.getContentPane().add(btnErreur);
				    
				    // BOUTON retour a l'accueil
				    JButton btnRetour = new JButton("Retour à l'accueil");
				    btnRetour.setBounds(50, 300, 200, 25);
				    btnRetour.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
							choix = 2;
						}
				         });
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
