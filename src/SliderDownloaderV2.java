import java.awt.Color;
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
	
	public static int getNbLignes(File file){ // RECUPERE NOMBRE LIGNES D'UN FICHIER
		int compteur=0;
		try{ 

			BufferedReader buff = new BufferedReader(new FileReader(file.getAbsolutePath())); 

			try { 
			String line; 
			// Lire le fichier ligne par ligne 
			// La boucle se termine quand la méthode affiche "null" 
			while ((line = buff.readLine()) != null) {
				compteur=compteur+1;
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
		return compteur;
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
	
	static ArrayList<String> liste_sons = new ArrayList<String>(); // liste dans laquelle l'utilisateur entre des artistes 
	static ArrayList<String> liste_liens = new ArrayList<String>(); // liste pour trier et garder le meme lien
	static ArrayList<String> liste_erreur = new ArrayList<String>(); // liste des erreurs

	public static void main(String[] args) {
		
		//INITIALISATION
		File filePath = new File("path.txt");
		//File fileErreur = new File("liste_erreur.txt");
		
		//FENETRE
		JFrame fenetre = new JFrame(); 
	    fenetre.setTitle("SliderDownloader"); 
	    fenetre.setSize(300, 400);
	    fenetre.setLocationRelativeTo(null); // PLACE LA FENETRE AU CENTRE
	    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stoppe le programme a la fermeture (croix rouge) 
	    fenetre.setResizable(false);
	    fenetre.setContentPane(new Accueil()); //afficher l'accueil
	    fenetre.getContentPane().setLayout(null);
	    
	    //BOUTON OUVRIR LE FICHIER
	    JButton btnOpen = new JButton("Ouvrir le fichier");
	    btnOpen.setBounds(50, 170, 200, 20);
	    btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
				open(new File(getFirstLine(filePath)));
			}
	         });
	    fenetre.getContentPane().add(btnOpen);
	    
	    //BOUTON SELECTIONNER UN FICHIER
	    JButton btnSelect = new JButton("Selectionner un autre fichier");
	    btnSelect.setBounds(50, 200, 200, 20);
	    btnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
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
	         });
	    fenetre.getContentPane().add(btnSelect);
	    
	    // BOUTON LANCER LE PROGRAMME
	    JButton btnLaunch = new JButton("Lancer le programme");
	    btnLaunch.setBounds(50, 230, 200, 70);
	    fenetre.getContentPane().add(btnLaunch);
	    fenetre.setVisible(true);
	    btnLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //LORSQU'ON CLIQUE SUR LE BTN
			
			}
	         });
	    
	    
	    
		
	}

}
