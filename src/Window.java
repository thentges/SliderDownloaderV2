import java.awt.Color;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window(){
		this.setContentPane(new Accueil());	
	this.setTitle("SliderDownloader"); 
    this.setBackground(Color.BLACK);
    this.setSize(300, 400);
    this.setLocationRelativeTo(null); // PLACE LA FENETRE AU CENTRE
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stoppe le programme a la fermeture (croix rouge) 
    this.setResizable(false);
    
    
	}
}
