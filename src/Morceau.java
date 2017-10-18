import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Morceau {

	private String nom;
	private String URL;
	private String source; // ex-pageAsXml
	private String link;
	private String endName;
	private ArrayList<String> liste_liens = new ArrayList<String>();
	private Boolean success; // true si ça a fonctionné false si ça a fail
	
	//	METHODES INTERNES
	private String sliderify(String line){
		line=line.replace(" ", "+");
		String lien="http://slider.kz/?page=1&act=source1&q=" + line;
		return lien;
	}
	
	private int getFileSize(String path){ // recupère le poids d'un fichier depuis son URL web
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
	
	private String getBestLink(ArrayList<String> liste){
		int comparateur = 0;
		int index = -1;
		for (int i=0 ; i<liste.size() ; i++)  {
			int size = getFileSize(liste.get(i));
			if (comparateur < size && liste.get(i).endsWith(this.endName)) { 
				comparateur = size;
				index = i ;
			}
			if (index==-1 && comparateur < size){ // si aucun fichier ne possède la bonne fin de nom prendre le plus gros de tous
				comparateur=size;
				index = i;
			}
		}
		String link = liste.get(index); // recupere le plus grand lien
		//link = link.replace(" ", "%20"); // reformate ce lien
		try {
			link = java.net.URLEncoder.encode(link, "UTF-8");
			link = link.replace("%2F", "/");
			link = link.replace("%3A", ":");
			link = link.replaceAll("2B", "+");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//System.out.println(link);
		SliderDownloaderV2.ecrire(new Fichier("logs.txt"), link);
		return link;
	}
	
	//	CONSTRUCTEUR
	Morceau(String line){
		this.nom = line;
		this.URL = sliderify(line);
		this.endName = nom.substring(nom.length()-4) +".mp3";
	}
	
	//	METHODES EXTERNES
	
	public void setSource(){
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.CHROME);   
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);					
		HtmlPage page;
		try {
			page = webClient.getPage(this.URL);
			webClient.waitForBackgroundJavaScriptStartingBefore(1000); 
			this.source = page.asXml(); // recupere le code source
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		webClient.close();
	}
	
	public void parse(){
		int debut;
		int fin;
		Boolean noInternalError = true;
		// parsing d'une partie du code contenant tout les liens dispo sur slider 
		do {
			debut = this.source.indexOf("playlist_gen");
			
			if (this.source.indexOf("Internal Server Error") != -1){
				noInternalError = false;
			}
			else if (debut != -1){ // handle bis des erreurs seveurs de slider.
				this.source = this.source.substring(debut);
				fin = this.source.indexOf("</div>");
				this.source = this.source.substring(0, fin);
				//parsing du premier lien
				debut = this.source.indexOf("/download/");
				fin = this.source.indexOf(".mp3");
				fin = fin + 4;
				if (debut !=-1 && fin !=3){ 
					String first = this.source.substring(debut, fin);
					first="http://slider.kz" + first;
					this.liste_liens.add(first);
					int compteur=0;
					// parsing des autres liens
					while (debut!=-1 && fin!=-1){
						compteur=compteur+1;
						if (compteur==8){ // 8 liens max traités
							break;
						} 
						debut = this.source.indexOf("/download/" , debut+10);
						fin = this.source.indexOf(".mp3" , fin);
						fin = fin + 4;
						if (debut != -1 && fin !=3){
							String second = this.source.substring(debut, fin);
							second = "http://slider.kz" + second;
							this.liste_liens.add(second);
						}
					}
					this.link = getBestLink(this.liste_liens);
					this.success = true; // on a recup un lien
				}
				else {
					this.success = false; // on a pas recup de lien
				}
			}
			else {
				this.success = false;
			}
		}while (!noInternalError);
		
	}
	
	public Boolean getSuccess(){
		return this.success;
	}

	public String getLink(){
		return this.link;
	}
}
