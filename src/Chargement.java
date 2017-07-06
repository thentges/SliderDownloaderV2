import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Chargement extends JPanel {
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
	        Image img = ImageIO.read(new File("slider_logo.png"));
	        g.drawImage(img, 43, 0, this);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    
	}               
}
