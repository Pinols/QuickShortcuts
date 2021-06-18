package newQuickShortcuts;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class QuickShortcuts extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static int amount=0;
	public static String filePath=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"QuickShortcuts.txt";
	//public static String filePath="C:\\Eureka"+File.separator+"txt.txt";
	public static String name;
	public static String link;
	public static File file;
	Icon icon=null;
	
	public static JMenuBar menu;
	public static ImagePanel body;
	public static JScrollPane scroll;
	public static PButton add;
	public static JLabel peepo;
	
	public static ArrayList<PButton> items;
	public static ArrayList<PButton> delete;
	public static ArrayList<PButton> move;
	public static ArrayList<String> list;
	
	
	
	public QuickShortcuts() {
		try {			
			startup();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startup() throws IOException{
		manageTooltips();
		svuotaOggetti();
		generaMenu();
		popolaScroll();
		finalize();
	}
	
	public void manageTooltips() {
		ToolTipManager tm=ToolTipManager.sharedInstance();
		tm.setInitialDelay(1200);
		tm.setReshowDelay(50);
	}

	public void svuotaOggetti() {
		if(menu!=null)menu.removeAll();
		if(body!=null)body.removeAll();
		if(scroll!=null)scroll.removeAll();
	}
	
	public void generaMenu() {
		add=new PButton("• Add","link");
		add.setForeground(Color.white);
		add.addActionListener(this);
		add.setFocusPainted(false);
		add.setToolTipText("New Shortcut");
		menu=new JMenuBar();
		menu.add(add);
		peepo=new JLabel(" Elements: "+amount);
		menu.add(peepo);
		setJMenuBar(menu);
		try {
			Image icon1 = new ImageIcon(this.getClass().getResource("res/shortcut128mod.png")).getImage();
			Image icon2 = new ImageIcon(this.getClass().getResource("res/shortcut64mod.png")).getImage();
			Image icon3 = new ImageIcon(this.getClass().getResource("res/shortcut32mod.png")).getImage();
			Image icon4 = new ImageIcon(this.getClass().getResource("res/shortcut16mod.png")).getImage();			
			Image[] icones=new Image[] {icon1,icon2,icon3,icon4};
			ArrayList<Image> icons= new ArrayList<Image>(Arrays.asList(icones));
			setIconImages(icons);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> letturaFile() throws IOException{
		String fileContent="";
		try {
			fileContent =new String(Files.readAllBytes(Paths.get(filePath)));
		}catch(NoSuchFileException ne) {
			ne.printStackTrace();
			System.out.println("File inesistente");
		}catch(Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		if(!fileContent.isEmpty()) {
			System.out.println("Stampa filecontent:"+fileContent);
			String[] contentArray=fileContent.split("#");
			ArrayList<String> contentList=new ArrayList<String>(Arrays.asList(contentArray));
			return contentList;
		}else {
			return new ArrayList<String>();
		}
	}
	
	public void nuovoFile() {
		JFileChooser chooser=new JFileChooser();
		chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(new File("C:\\")));
		chooser.setFileView(new FileView() {
			public Icon getIcon(File f) {
				return FileSystemView.getFileSystemView().getSystemIcon(f);
			}
		});
		chooser.setLocation(400,400);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = chooser.showOpenDialog(scroll);
		if(result==JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();			
		}
		else {
			return;
		}
		link=file.getPath();
		ImageIcon img=new ImageIcon(QuickShortcuts.class.getResource("res/plus2.png"));
//showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue)
		name = (String) JOptionPane.showInputDialog(scroll,"Choose a name :","Type!",JOptionPane.QUESTION_MESSAGE,img,null,"");
		if(name==null)return;
		try {
			String s=""+name+"@"+link;
			aggiuntaFile(s);
			System.out.println("post aggiunta file"+name+link);
			startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void aggiuntaFile(String s) throws IOException {
		if(list==null) {
			list=new ArrayList<String>();
			list.add(s);
		}else list.add(s);
		for(String z:list) {
			System.out.println("stampafor"+z);
		}
		if(!s.equals("")) {
			aggiornaFile();
		}
	}
	
	public void creaPopMod(String s) {
		int start=Integer.parseInt(s);
		int end;
		Object[] values=new Object[list.size()];
		for(int i=0;i<list.size();i++) {
			values[i]=i;
		}
		try {
			end =(int)JOptionPane.showInputDialog(scroll," Move to  :", "Moving object "+start,JOptionPane.PLAIN_MESSAGE,null,values,values[0]);
			System.out.println("da pos "+start+" a pos "+end);
			modificaFile(start,end);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void modificaFile(int start,int dest) throws IOException {
		String temp=list.get(start);
		list.remove(start);
		list.add(dest,temp);
		aggiornaFile();
		startup();
	}
	
	public void eliminaEntry(String s) throws IOException{
		int n=Integer.parseInt(s);
		list.remove(n);
		aggiornaFile();
		startup();
	}
	
	public void aggiornaFile() {
		PrintWriter printer=null;
		try {
			printer=new PrintWriter(filePath);
			for(int i=0;i<list.size();i++) {
				printer.append(list.get(i));
				if(!(i==list.size()-1))printer.append("#");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			printer.close();
		}
	}
	
	public void generaBottoni() throws IOException{
		list=letturaFile();
		list.removeAll(Arrays.asList("",null," "));
		items=new ArrayList<PButton>();
		delete=new ArrayList<PButton>();
		move=new ArrayList<PButton>();
		amount=0;
		String[] temp=null;
		try {
			if(!list.isEmpty()) {
				for(int i=0;i<list.size();i++) {
					temp=list.get(i).split("@");
					items.add(new PButton(temp[0],temp[1]));
					items.get(i).addActionListener(this);					
					items.get(i).setIcon(generaIcone(i));					
					items.get(i).setPreferredSize(new Dimension(60,35));
					items.get(i).setBackground(Color.black);
					items.get(i).setForeground(Color.white);
					items.get(i).setToolTipText(""+temp[1]);
					delete.add(new PButton("Delete",""+i));
					delete.get(i).addActionListener(this);
					delete.get(i).setBackground(Color.black);
					delete.get(i).setForeground(Color.orange);
					move.add(new PButton("Move ",""+i));
					move.get(i).addActionListener(this);
					move.get(i).setBackground(Color.black);
					move.get(i).setForeground(Color.lightGray);
					amount++;
				}
				peepo.setText(" Elements: "+amount);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public Icon generaIcone(int i) {
		try {
			File f=new File(items.get(i).getLink().trim());
			icon=FileSystemView.getFileSystemView().getSystemIcon(f);
		}catch(Exception e) {
			e.printStackTrace();
		}
			return icon;
	}
	
	
	public void popolaBody() throws IOException {
		generaBottoni();
		JPanel temp;
		GridLayout g=new GridLayout(0,1,15,15);
		g.setVgap(10);
		g.setHgap(10);
		Image img = new ImageIcon(this.getClass().getResource("res/geometry.jpg")).getImage();
		body=new ImagePanel(img);
		body.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		body.setLayout(g);
		for(int i=0;i<list.size();i++) {
			body.add(items.get(i));
			temp=new JPanel();			
			temp.add(delete.get(i));
			temp.add(move.get(i));
			temp.setPreferredSize(new Dimension(20,10));
			temp.setOpaque(false);
			body.add(temp);
		}		
	}

	public void popolaScroll() throws IOException{
		popolaBody();
		scroll=new JScrollPane(body);		
		scroll.getVerticalScrollBar().setUnitIncrement(32);
	}
	
	public void finalize() {
		setContentPane(scroll);
		setTitle("QuickShortcuts");
		setPreferredSize(new Dimension(300,700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(250,150);
		setVisible(true);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {		// Gestione click bottoni
		if(ae.getSource().equals(add)) {
			nuovoFile();
		}
		
		for(PButton b:items) {							// Apertura file
			if(ae.getSource().equals(b)) {
				Desktop desk=Desktop.getDesktop();
				try {
					//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + b.getLink());
					desk.open(new File(b.getLink()));
					//Runtime.getRuntime().exec(b.getLink());
					//Runtime.getRuntime().exec("rundll32 SHELL32.DLL, ShellExec_RunDLL "+b.getLink());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		for(PButton b:delete) {
			if(ae.getSource().equals(b)) {
				try {
					eliminaEntry(b.getLink());
					startup();
				}catch(Exception e) {
					e.printStackTrace();
				}				
			}
		}
		
		for(PButton b:move) {
			if(ae.getSource().equals(b)) {
				creaPopMod(b.getLink());
			}
		}
	}
}