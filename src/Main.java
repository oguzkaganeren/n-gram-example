import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

	static BufferedWriter output = null;
	static JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main frame = new Main();
				frame.setVisible(true);
				try {
		            File file = new File("theLog.txt");
		            output = new BufferedWriter(new FileWriter(file));
		        } catch ( IOException e ) {
		            e.printStackTrace();
		        } 
				JButton btnUButton = new JButton("UNUTULMUŞ DİYARLAR");
				btnUButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printResults("UNUTULMUŞ DİYARLAR",0,0);
					}
				});
				contentPane.add(btnUButton);
				JButton btnBButton = new JButton("BİLİM İŞ BAŞINDA");
				btnBButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printResults("BİLİM İŞ BAŞINDA",150,100);
					}
				});
				contentPane.add(btnBButton);
				JButton btnBoButton = new JButton("BOZKIRDA");
				btnBoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printResults("BOZKIRDA",300,200);
					}
				});
				contentPane.add(btnBoButton);
				JButton btnDeButton = new JButton("DEĞİŞİM");
				btnDeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printResults("DEĞİŞİM",450,300);
					}
				});
				contentPane.add(btnDeButton);
				JButton btnDenButton = new JButton("DENEMELER");
				btnDenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printResults("DENEMELER",600,300);
					}
				});
				contentPane.add(btnDenButton);
			}
		});
	}
	
	static public void printResults(String fileName,int x,int y) {



		JFrame frame = new JFrame(fileName);
		frame.setBounds(100, 100, 900, 800);
		frame.setLocation(x, y);
		frame.setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new FlowLayout());
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		ArrayList<String> testWords = sanitiseToWords(readFile(fileName+".txt"));
		long startTime = System.nanoTime();
		ArrayList<String> resultOne=ngrams(testWords,1);
	    long resultOneTime = System.nanoTime() - startTime;
	    startTime = System.nanoTime();
	    ArrayList<String> resultTwo=ngrams(testWords,2);
	    long resultTwoTime = System.nanoTime() - startTime;
	    startTime = System.nanoTime();
	    ArrayList<String> resultThree=ngrams(testWords,3);
	    long resultThreeTime = System.nanoTime() - startTime;
		JLabel lblU = new JLabel("<html><h1>"+fileName+"</h1><br>Unigram result:"+String.valueOf(resultOneTime)+" ns"+
	    " <br>Bigram result:"+String.valueOf(resultTwoTime)+" ns"+
				" <br>Trigram result:"+String.valueOf(resultThreeTime)+" ns"+
	    "<br> Total time:"+String.valueOf(resultOneTime+resultTwoTime+resultThreeTime)+" ns");
		lblU.setLocation(0,25);
		contentPane.add(lblU);
		try {
			output.write(fileName+"--------------------"+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createList(countRepeated(resultOne));
		try {
			output.write(fileName+"--------------------bigram"+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createList(countRepeated(resultTwo));
		try {
			output.write(fileName+"--------------------trigram"+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createList(countRepeated(resultThree));
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(readFile("UNUTULMUŞ DİYARLAR.txt"));
	}
	static public void createList(Map<String, Integer> result) { 
		DefaultListModel listModel = new DefaultListModel();
		result.entrySet().forEach(entry->{
			   // System.out.println(entry.getKey().toString() + " " + entry.getValue().toString());  
			    listModel.addElement(entry.getKey().toString() + " " + entry.getValue().toString());
			    try {
					output.write(entry.getKey().toString() + " " + entry.getValue().toString()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 });
		
		
		JList list = new JList(listModel);
		list.setFont(new Font("Serif", Font.PLAIN, 12));
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(200, 250));
		contentPane.add(listScroller, BorderLayout.SOUTH);
	}
	static public void setTableHeight(JTable table, int rows)
	{
	    int width = table.getPreferredSize().width; 
	    int height = rows * table.getRowHeight(); 
	    table.setPreferredScrollableViewportSize(new Dimension(width, height));
	}
	public static String readFile(String fileName) {
		String totatString = "";
		try {
			File fileDir = new File(fileName);
			//Türkçe karakter sorunu çözüldü
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "WINDOWS-1254"));

			String str;

			while ((str = in.readLine()) != null) {
				totatString += str;
			}

			in.close();
			return totatString;

		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static ArrayList<String> ngrams(ArrayList<String> words, int n) {
		/*if (n <= 1) {
			return new ArrayList<String>(words);
		}*/

		ArrayList<String> ngrams = new ArrayList<String>();
		int c = words.size();
		for (int i = 0; i < c; i++) {
			if ((i + n - 1) < c) {
				int stop = i + n;
				String ngramWords = words.get(i);
				for (int j = i + 1; j < stop; j++) {
					ngramWords += " " + words.get(j);
				}
				ngrams.add(ngramWords);
			}
		}

		return ngrams;
	}
	public static Map<String, Integer> countRepeated(ArrayList<String> list) 
    { 
		Map<String, Integer> countMap = new HashMap<>();
  
        // Traverse through the first list 
        for (String element : list) { 
  
            // If this element is not present in newList 
            // then add it 
            if (countMap.containsKey(element))
                countMap.put(element, countMap.get(element) + 1);
            else
                countMap.put(element, 1);
        } 
        Map<String, Integer> sortedMap = sortByComparator(countMap, false);
       
        return sortedMap; 
    } 
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	public static ArrayList<String> sanitiseToWords(String text) {
		String[] characters = text.split("");

		String sanitised = "";

		boolean onSpace = true;

		int xLastCharacter = text.length() - 1;
		for (int i = 0; i <= xLastCharacter; i++) {
			//Türkçe karakterler eklendi
			//-’' karakterleri eklendi
			if (characters[i].matches("^[A-Za-z0-9çÇğĞıİöÖşŞüÜ-’'$£%]$")) {
				sanitised += characters[i];
				onSpace = false;
			} else if (characters[i].equals("'") && i > 0 && i < xLastCharacter) {
				String surrounding = characters[i - 1] + characters[i + 1];
				if (surrounding.matches("^[A-Za-z]{2}$")) {
					sanitised += "'";
					onSpace = false;
				}
			} else if (!onSpace && i != xLastCharacter) {
				sanitised += " ";
				onSpace = true;
			}
		}

		return new ArrayList<String>(Arrays.asList(sanitised.split("\\s+")));
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new FlowLayout());
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
	}
}
