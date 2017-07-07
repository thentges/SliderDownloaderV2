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
import javax.swing.JButton;
import javax.swing.JPanel;
 
public class Accueil extends JPanel {
	
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
	
	public  int getNbLignes(File file){ // RECUPERE NOMBRE LIGNES D'UN FICHIER
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
	
	public void paintComponent(Graphics g){
		
		File filePath = new File(getClass().getResource("/path.txt").toString());
		File fileSons = new File(getFirstLine(filePath));
		
		// COULEUR DE FOND
	    this.setBackground(Color.BLACK); 
	    // CREDITS FOOTER
	    Font fontFooter = new Font("Arial", Font.PLAIN, 10);
	    FontMetrics metricsFooter = g.getFontMetrics(fontFooter);
	    g.setFont(fontFooter);
	    g.setColor(Color.GRAY);
	    String strFooter = "SliderDownloader v.2 par Thibault HENTGÈS";
	    g.drawString(strFooter, (300 - metricsFooter.stringWidth(strFooter))/2, 370);   
	    // LOGO HEADER
	    try {
	        Image img = ImageIO.read((getClass().getResource("/slider_logo.png")));
	        g.drawImage(img, 43, 0, this);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    //RECAP FICHIER 
	    String name = fileSons.getName();
	    int nbLignes = getNbLignes(fileSons);
	    Font fontRecap = new Font("Arial", Font.PLAIN, 12);
	    FontMetrics metricsRecap = g.getFontMetrics(fontRecap);
	    g.setFont(fontRecap);
	    g.setColor(Color.WHITE); 
	    String strRecap = name+" contient "+nbLignes+" morceaux à analyser";
	    g.drawString(strRecap, (300 - metricsRecap.stringWidth(strRecap))/2, 130);   
	    repaint();
	}               
}