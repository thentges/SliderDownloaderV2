import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Fichier {
	
	private String name;
	private File file;
	private int nbLignes;
	private String firstLine;
	
	private void setFirstLine(File file){
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
		
		this.firstLine=line;
		} 
	
	private  void setNbLignes(File file){
		
		int compteur=0;
		try{ 

			BufferedReader buff = new BufferedReader(new FileReader(file.getAbsolutePath())); 

			try { 
				// Lire le fichier ligne par ligne 
				// La boucle se termine quand la méthode affiche "null" 
				while (buff.readLine() != null) {
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
		this.nbLignes = compteur;
	} 
	
	public String getFirstLine(){
		return this.firstLine;
	}
	
	public void majFirstLine(){
		this.setFirstLine(this.file);
	}
	
	public int getNbLignes(){
		return this.nbLignes;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public String getName(){
		return this.name;
	}
	
	Fichier(String pathname){
		this.file = new File(pathname);
		this.setFirstLine(this.file);
		this.setNbLignes(file);
		this.name = file.getName();
		
	}

	public void createNewFile() throws IOException {
		this.file.createNewFile();	
	}

	public boolean exists() {
		return this.file.exists();
	}

}
