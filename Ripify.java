import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.jsoup.Jsoup;


public class Ripify {
	static String Directory = "";
	static  int index = 0;
	static int nSongs = 0;
	static ExecutorService executor = Executors.newFixedThreadPool(8);
	static int start = 0;
	static int end = 0;
	static String time = "00:00";
	 static JLabel timeLeft = new JLabel("Querying " + nSongs + " Songs Estimated time left " + time);
	static String list = "Input directory of your list\n like C:/Users/accountName/Desktop/music.txt";	
    static JTextArea label = new JTextArea(list);
    static JTextField field = new JTextField(); 
   static JFrame frame = new JFrame("Ripify");
   static JPanel scrollLabel = new JPanel(new BorderLayout());
   public static void main(String[] args) throws IOException{
	   SwingUtilities.invokeLater(new Runnable(){
	   public void run() {
           createAndShowGUI();             
       }
   });
     
   }
   public static void genSongList(final String loc){
	 final String t = loc;
	 list = "Searching " + field.getText() + " for songs names";
		
		String temp[] = loc.split("\\\\");
		//int i = t.length;
	
		String e = temp[0];
		if(temp.length > 1){
			for(int i = 1; i < temp.length - 1; i++){
				e = e + "\\"+temp[i];
			}
			Directory = e + "\\";
		} else {
			Directory = "";
		}
		
	System.out.println(e);
	   Thread one = new Thread() {
		    public void run() {
		        File file = new File(t);
				try {
		
					Scanner scan = new Scanner(new FileReader(file));
					while(scan.hasNext()){
						String nothing = scan.nextLine();
						nSongs++;
						timeLeft.setText("Querying " + nSongs + " Songs Estimated time left " + time);
					}
					
					 scan = new Scanner(new FileReader(file));
					while(scan.hasNext()){
						
						index++;
						long m = System.currentTimeMillis();
						String url = scan.nextLine();
						 String html = Jsoup.connect(url).get().html();
						
						 final String thtml = html;
						String[] temp = html.split("title");

						boolean record = false;
					
						for(int i = 0; i < html.length(); i++){
							if(record){
								if(html.charAt(i) == '<'){
									end = i;
									 record = false;
									 i = html.length();
								}
							} else {
							if(html.charAt(i) == '<'){
								if(html.charAt(i + 1) == 't'){
									if(html.charAt(i + 2) == 'i'){
										if(html.charAt(i + 3) == 't'){
										  if(html.charAt(i + 4) == 'l'){
												if(html.charAt(i + 5) == 'e'){
													if(html.charAt(i + 6) == '>'){
														  i = i + 7;
														  start = i;
														  record = true;
													}
												}
											}	  
										}  	  
									}  
								}
							}
							
							}
						}
						String downloaded =" Not Yet Downloaded\n";
						File folder = new File(Directory + "Songs");
			
						File[] listOfFiles = folder.listFiles();
					    String titlee = (String) html.subSequence(start, end - 11);
						titlee = titlee.replace("\\" , " ");
					    titlee = titlee.replace("/", " ");
					    titlee = titlee.replace("-", " ");
					  
					    titlee = titlee.replace(".", " ");
					  
					    final String title = titlee;
						
						
						System.out.println(title);
						boolean download =true;
						//if(folder.exists()){
						String me[] = title.split(", a song by ");
						me[0] = me[0].replace(",", " ");
						me[1] = me[1].replace(",", " ");
						String tem = me[0] + ".m4a";
						System.out.println("Song Name = " + tem);
						//System.out.print(listOfFiles.length + ":" );
//						for(int i = 0; i < listOfFiles.length; i++){
//				downloaded = title + " Not Yet Downloaded\n";
//							
//						   if(tem.equals(listOfFiles[i].getName())){
//							   i = listOfFiles.length;
//							   download = false;
//							  downloaded ="";
//						   } else {
//							// downloaded = tem + " Not Yet Downloaded\n";	    
//						   }
//						}
//						}
						//System.out.print(downloaded);
						download = true;
						if(download){
						String nTitle = title.replaceAll(" ", "+");
				 final String albumName = getAlbum(thtml);
						
						  final String songName[] = title.split(", a song by ");
						  for(int i = 0; i < songName[0].length(); i++){
							  if(songName[0].charAt(i) == '?'){
								  songName[0] = songName[0].substring(0, i - 1);
							  }
						  }
						  songName[0] = songName[0].replaceAll("[^\\x00-\\x7F]", "").replaceAll("\\s+", " ");;
						  
				       list = list + "\n"+ title;
						label.setText(list);
						frame.revalidate(); 
						    frame.repaint();
						    System.out.println("https://www.youtube.com/results?search_query=" + me[0].replace(" ","+") + "+by+" + me[1].replace(" ", "+"));
						    String url2 = "https://www.youtube.com/results?search_query=" + me[0].replace(" ","+") + "+by+" + me[1].replace(" ", "+");
						    url2 = Normalizer.normalize(url2, Normalizer.Form.NFD);
						    html = Jsoup.connect(url2).get().html(); 
						 
						
						    record = false;
						    String times[] = null;
						    if(html.contains("<span class=\"video-time\" aria-hidden=\"true\">__length_seconds__</span>")){
						    String timestemp[] = html.split("class=\"video-time\" aria-hidden=\"true\">__length_seconds__</span>");
						    times = timestemp[1].split("class=\"video-time\" aria-hidden=\"true\">");
						   
						    } else {
						    times = html.split("class=\"video-time\" aria-hidden=\"true\">");	
						  System.out.println("Splitting");
						    }
						    boolean notfound = true;
						  String  videoLength="";  
						  int indexLoc = 1;
						  while(notfound && indexLoc< times.length){
						     videoLength="";
						     int charLoc = 0;
							 char tVal= times[indexLoc].charAt(charLoc);
							 while(tVal != '<'){
							 videoLength = videoLength.concat(String.valueOf(tVal));
							 charLoc++;
							 tVal = times[indexLoc].charAt(charLoc);
							 }
							 System.out.println(videoLength);
							 videoLength = videoLength.replace(":","");
							 if(Integer.valueOf(videoLength) < 2000){
								 notfound = false;
							 } else{
							 System.out.println("VIDEO LENGTH : " + videoLength);
							 indexLoc++;
							 }
						    }
						  	String htmlf = times[indexLoc];
							for(int i = 0; i < htmlf.length(); i++){
								if(record){
									if(htmlf.charAt(i) == '"'){
										 end = i;
										 record = false;
										 i = htmlf.length();
									}
								} else {
								if(htmlf.charAt(i) == '/'){
									if(htmlf.charAt(i + 1) == 'w'){
										if(htmlf.charAt(i + 2) == 'a'){
											if(htmlf.charAt(i + 3) == 't'){
											  if(htmlf.charAt(i + 4) == 'c'){
													if(htmlf.charAt(i + 5) == 'h'){
														if(htmlf.charAt(i + 6) == '?'){
															start = i;
															i = i + 7;
															record = true;
														}
													}
												}	  
											}  	  
										}  
									}
								}
								
								}
							}
							String vidURL = (String) htmlf.subSequence(start,end);
							final String videoURL = "https://www.youtube.com" + vidURL;
							executor.execute(new Runnable() {
							    public void run() {
							    	System.out.println(songName[0]);
							    	imgDownload(thtml, "songs", songName[0]);		
							    	Runtime rt = Runtime.getRuntime();
							        try {		
				        				 System.out.println("youtube-dl -o \"" + Directory + "Songs\\"+ songName[0] + ".m4a\" \""+ videoURL +  "\" --extract-audio --add-metadata");
										 Process proc =rt.exec("youtube-dl -o \"" + Directory + "Songs\\"+ songName[0] + ".m4a\" "+ videoURL +  " --extract-audio --add-metadata");
									     proc.waitFor();
										 BufferedReader stdInput = new BufferedReader(new 
									             InputStreamReader(proc.getInputStream()));

									        BufferedReader stdError = new BufferedReader(new 
									             InputStreamReader(proc.getErrorStream()));
                                         
									        // read the output from the command
									     
									        String s = null;
									        while ((s = stdInput.readLine()) != null) {
									           System.out.println(s);
									        }

									        // read any errors from the attempted command
							
									        while ((s = stdError.readLine()) != null) {
									            System.out.println(s);
									        }
									      //  proc = rt.exec("ffmpeg -i \""+ Directory + "\\Songs\\" +songName[0] + ".mp3\" -acodec libmp3lame -ab 128k \"" + Directory + "\\Songs\\" +songName[0] + ".mp3\"");
									      //	proc.waitFor();
									   System.out.println("atomicparsley \"" + Directory + "Songs\\" + songName[0] + ".m4a\" --artwork \"" + Directory + "Songs\\" + songName[0]+".jpg\" --artist \"" + songName[1] + "\" --title \"" + songName[0] + "\" --album \"" + albumName + "\" --overWrite");
									    	proc = rt.exec("atomicparsley \"" + Directory + "Songs\\" + songName[0] + ".m4a\" --artwork \"" + Directory + "Songs\\" + songName[0]+".jpg\" --artist \"" + songName[1] + "\" --title \"" + songName[0] + "\" --album \"" + albumName.replaceAll("-", " ") + "\" --overWrite");
											proc.waitFor();
								        File file = new File(Directory + "\\" + songName[0] + ".jpg");
								    
								        file.delete();
							        } catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
							       
							     
							    }
							});
												
								        

								long seconds = 1000;
								long test = nSongs - index;
							long te =(((long)System.currentTimeMillis() - m) * test) / seconds; //* (float)nSongs);
								timeLeft.setText("Downloading " +( nSongs - index) + " Songs Estimated time left " + te + " Seconds");
							
					}
					}
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "File Not Found");
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Site does not exist");
					e.printStackTrace();
				}
		    }  
		};
     
		one.start();

   }
   public void rip(String song[]){
	
	  // URL url = new URL("")  url.openConnection();
   }
   private static void createAndShowGUI() {
       //Create and set up the window.
	   JPanel panel = new JPanel();
       JPanel tracking = new JPanel();
	   scrollLabel.add(label, BorderLayout.NORTH);
	   JScrollPane plable = new JScrollPane(scrollLabel);
	   plable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	   plable.setViewportBorder(new LineBorder(new Color(0, 162, 232)));
	   plable.setPreferredSize(new Dimension(200, 300));
       tracking.add(timeLeft);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Dimension d = new Dimension(500,500);
      frame.setPreferredSize(d);
      label.setEditable(false);
      label.setBorder(null);
      label.setForeground(UIManager.getColor("Label.foreground"));
      label.setFont(UIManager.getFont("Label.font"));
     //frame.getContentPane().add(label, BorderLayout.NORTH);
     frame.pack();
       JButton startButton = new JButton("Start");//The JButton name.
       startButton.setPreferredSize(new Dimension(70, 40));
      field.setPreferredSize(new Dimension(300, 20));
		panel.add(startButton);//Add the button to the JFrame.
		panel.add(field, BorderLayout.CENTER);
	//	panel.add(timeLeft);
		startButton.addActionListener(new recordNames());//Reads the action.
		 frame.getContentPane().add(panel, BorderLayout.SOUTH);
       frame.getContentPane().add(plable, BorderLayout.CENTER);
       frame.getContentPane().add(tracking, BorderLayout.NORTH);
      label.setText(list);
       
       frame.pack();
       frame.setVisible(true);
    }
   
   public static void imgDownload(String html, String dir, String name) {
	   System.out.println("Downloading Image");
		boolean record = false;
		int end = 0; int start = 0;
		boolean recUrl = false;
		
		for(int i = 0; i < html.length(); i++){
				if(recUrl){
						if(html.charAt(i) == ')'){
							end = i;
							 record = false;
							 i = html.length();
				}
			} else if(record){
				start = i;				
				recUrl = true;	
					}
			 else {
			if(html.charAt(i) == 'u'){
				if(html.charAt(i + 1) == 'r'){
					if(html.charAt(i + 2) == 'l'){
						if(html.charAt(i + 3) == '('){
						  if(html.charAt(i + 4) == '/'){
								if(html.charAt(i + 5) == '/'){
										  i = i + 5;
										  start = i;
										  record = true;
								}
							}	  
						}  	  
					}  
				}
			}
			}
		}
		String title = (String) html.subSequence(start, end);
		System.out.println(start + " " + end  + " f " + title);
		String Imageurl;
		System.out.println("TITLE : " + title);
		URL url = null;
		try {
		 url = new URL("http://" + title);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		try{
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();
		 FileOutputStream fos = new FileOutputStream(dir + "\\" + name+".jpg");
		 fos.write(response);
		 fos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
   public static String getAlbum(String test) throws IOException{
	 String album ="";
	 String res[] = test.split("class=\"owner-name hdr-l\"");
	
	int i = 1;
	 char tVal= res[2].charAt(i);
	 while(tVal != '<'){
	 album = album.concat(String.valueOf(tVal));
	 i++;
	 tVal = res[2].charAt(i);
	 }

	   return album;
   }
   static class recordNames implements ActionListener {        

	public void actionPerformed(ActionEvent arg0) {
		
	
	label.setText(list);
  
	genSongList(field.getText());
	}
	   }
   class MyAdjustmentListener implements AdjustmentListener {
	


	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		 System.out.println("    New Value is " + e.getValue() + "      ");
	}
}
}