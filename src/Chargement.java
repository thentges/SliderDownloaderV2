import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
	
	public void plusCompteurLignes(){
		this.compteurLignes++;
	}

public void paintComponent(Graphics g){
	Fichier filePath = new Fichier("Ressources/path.txt");
	Fichier fileSons = new Fichier(filePath.getFirstLine());
		// GIF 
		Image icon = new ImageIcon("Ressources/chargement.gif").getImage();
		g.drawImage(icon, 0, 0, this);
		// COULEUR DE FOND
	    this.setBackground(Color.BLACK); 
	    // LOGO HEADER
	    try {
	        Image img = ImageIO.read(new File("Ressources/slider_logo.png"));
	        g.drawImage(img, 43, 0, this);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    // EN COURS
	    Font fontEnCours = new Font("Arial", Font.PLAIN, 15);
	    FontMetrics metricsEnCours = g.getFontMetrics(fontEnCours);
	    g.setFont(fontEnCours);
	    g.setColor(Color.WHITE);
	    //if (nom!=null){
	    //String strEnCours = nom;
	    //g.drawString(strEnCours, (300 - metricsEnCours.stringWidth(strEnCours))/2, 120); 
	    //}
	    // COMPTEUR
	    int nbLignes = fileSons.getNbLignes();
	    Font fontRecap = new Font("Arial", Font.ITALIC, 12);
	    FontMetrics metricsRecap = g.getFontMetrics(fontRecap);
	    g.setFont(fontRecap);
	    String strLignes = compteurLignes +" sur " + nbLignes +" morceaux traités";
	    g.drawString(strLignes, (300 - metricsRecap.stringWidth(strLignes))/2, 135);
	    // BARRE DE PROGRESSION
	    g.drawRect(50, 150, 200, 20);
	    if (nbLignes!=0){
		    int division = 200/nbLignes;
		    g.setColor(new Color(127, 255, 0, 127));
		    if (compteurLignes*division <= 200){
		    	g.fillRect(50, 151, compteurLignes*division, 18);
		    }
	    } 
	}               
}
