import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
 
public class Accueil extends JPanel {
	
	public void paintComponent(Graphics g){
		
		Fichier filePath = new Fichier("Ressources/path.txt");
		Fichier fileSons = new Fichier(filePath.getFirstLine());
		
		// COULEUR DE FOND
	    this.setBackground(Color.BLACK); 
	    // CREDITS FOOTER
	    Font fontFooter = new Font("Arial", Font.PLAIN, 10);
	    FontMetrics metricsFooter = g.getFontMetrics(fontFooter);
	    g.setFont(fontFooter);
	    g.setColor(Color.GRAY);
	    String strFooter = "SliderDownloader v.3.0 par Thibault HENTGÈS";
	    g.drawString(strFooter, (300 - metricsFooter.stringWidth(strFooter))/2, 370);   
	    // LOGO HEADER
	    try {
	        Image img = ImageIO.read(new File("Ressources/slider_logo.png"));
	        g.drawImage(img, 43, 0, this);
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    //RECAP FICHIER 
	    String name = fileSons.getName();
	    int nbLignes = fileSons.getNbLignes();
	    Font fontRecap = new Font("Arial", Font.PLAIN, 12);
	    FontMetrics metricsRecap = g.getFontMetrics(fontRecap);
	    g.setFont(fontRecap);
	    g.setColor(Color.WHITE); 
	    String strRecap = name+" contient "+nbLignes+" morceaux à analyser";
	    g.drawString(strRecap, (300 - metricsRecap.stringWidth(strRecap))/2, 130);   
	    repaint();
	}               
}