import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Chargement extends JPanel {
	private String nom;
	
	public void setNom(String str){
		nom = str;
	}
public void paintComponent(Graphics g){
		
		// GIF TEST
    	
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
	    g.setColor(Color.GRAY);
	    String strEnCours = nom;
	    g.drawString(strEnCours, (300 - metricsEnCours.stringWidth(strEnCours))/2, 130);   
	    repaint();
	      

	}               
}
