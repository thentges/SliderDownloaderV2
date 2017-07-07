import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Chargement extends JPanel {
	private String nom;
	private int compteurLignes=0;
	
	public void setNom(String str){
		nom = str;
	}
	public void setCompteurLignes(int nb){
		this.compteurLignes=nb;
	}

	public int getNbLignes(File file){ // RECUPERE NOMBRE LIGNES D'UN FICHIER
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
	
	public String getFirstLine(File file){
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
	
	
public void paintComponent(Graphics g){
	File filePath = new File("path.txt");
	File fileSons = new File(getFirstLine(filePath));
	
		// GIF 
		Image icon = new ImageIcon("chargement.gif").getImage();
		g.drawImage(icon, 0, 0, this);
		// COULEUR DE FOND
	    this.setBackground(Color.BLACK); 
	    // LOGO HEADER
	    try {
	        Image img = ImageIO.read(new File("slider_logo.png"));
	        g.drawImage(img, 43, 0, this);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    // EN COURS
	    Font fontEnCours = new Font("Arial", Font.PLAIN, 15);
	    FontMetrics metricsEnCours = g.getFontMetrics(fontEnCours);
	    g.setFont(fontEnCours);
	    g.setColor(Color.WHITE);
	    if (nom!=null){
	    String strEnCours = nom;
	    g.drawString(strEnCours, (300 - metricsEnCours.stringWidth(strEnCours))/2, 120); 
	    }
	    // COMPTEUR
	    int nbLignes = getNbLignes(fileSons);
	    Font fontRecap = new Font("Arial", Font.ITALIC, 12);
	    FontMetrics metricsRecap = g.getFontMetrics(fontRecap);
	    g.setFont(fontRecap);
	    String strLignes = compteurLignes +" sur " + nbLignes +" morceaux traités";
	    g.drawString(strLignes, (300 - metricsRecap.stringWidth(strLignes))/2, 135);
	    // BARRE DE PROGRESSION
	    g.drawRect(50, 150, 200, 20);
	    if (nbLignes!=0){
		    int division = 200/nbLignes;
		    g.setColor(Color.GREEN);
		    if (compteurLignes*division <= 200){
		    	g.fillRect(50, 151, compteurLignes*division, 18);
		    }
	    }
	    repaint();
	      

	}               
}
