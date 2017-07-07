import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Fin extends JPanel {
	
	private int compteurErreur=420;
	private int compteurLignes=420;
	
	public void setCompteurLignes(int nb){
		this.compteurLignes=nb;
	}
	public void setCompteurErreur(int nb){
		this.compteurErreur=nb;
	}
public void paintComponent(Graphics g){
		
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
	        Image img = ImageIO.read(new File("/Ressources/slider_logo.png"));
	        g.drawImage(img, 43, 0, this);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    // COMPTEURS MORCEAUX TÉLÉCHARGÉS 
	    String strRecapDL;
	    String strRecapFail;
	    Font fontRecap = new Font("Arial", Font.PLAIN, 20);
	    FontMetrics metricsRecap = g.getFontMetrics(fontRecap);
	    g.setFont(fontRecap);
	    g.setColor(Color.GREEN);
	    if (compteurLignes - compteurErreur == 1){
	    	strRecapDL = compteurLignes-compteurErreur +" morceau récupéré";
		}
		else {
			strRecapDL = compteurLignes-compteurErreur +" morceaux récupérés";
		}
	    g.drawString(strRecapDL, (300 - metricsRecap.stringWidth(strRecapDL))/2, 130);   
	    g.setColor(Color.RED);
	    if (compteurErreur==1){
	    	strRecapFail = compteurErreur + " morceau non trouvé";
	    	g.drawString(strRecapFail, (300 - metricsRecap.stringWidth(strRecapFail))/2, 150); 
	    }
	    else if (compteurErreur>1){
	    	strRecapFail = compteurErreur + " morceaux non trouvés";
	    	g.drawString(strRecapFail, (300 - metricsRecap.stringWidth(strRecapFail))/2, 150); 
	    }
	    
	}               
}
