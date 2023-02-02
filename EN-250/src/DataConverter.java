import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataConverter {
	
	public static ArrayList<String[]> openFile(File file) throws FileNotFoundException {
		ArrayList<String[]> total = new ArrayList<String[]>();
		
		Scanner fileContents = new Scanner(file);
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			String[] content = contents.split(" ");
			total.add(content);
		}
		fileContents.close();
		
		return total;
	}
	
	public static void doDataLC(ArrayList<String[]> total) throws FileNotFoundException {
		PrintWriter logger = new PrintWriter("finaldata/remLinksConnectance.csv"); //CHANGE name here for new data set
		
		for (int i = 0; i < total.size(); i++) {
			logger.println(total.get(i)[1] + "," + total.get(i)[5]);
		}
		logger.close();
	}
	
	public static void doDataSL(ArrayList<String[]> total) throws FileNotFoundException {
		PrintWriter logger = new PrintWriter("finaldata/remSpeciesLinks.csv"); //CHANGE name here for new data set
		
		for (int i = 0; i < total.size(); i++) {
			logger.println(total.get(i)[3] + "," + total.get(i)[1]);
		}
		logger.close();
	}
	
	public static void doDataSC(ArrayList<String[]> total) throws FileNotFoundException {
		PrintWriter logger = new PrintWriter("finaldata/remSpeciesConnectance.csv"); //CHANGE name here for new data set
		
		for (int i = 0; i < total.size(); i++) {
			logger.println(total.get(i)[3] + "," + total.get(i)[5]);
		}
		logger.close();
	}
	
	public static void doDataAll(ArrayList<String[]> total) throws FileNotFoundException {
		PrintWriter logger = new PrintWriter("finaldata/remLinksSpeciesConnectanceData.csv"); //CHANGE name here for new data set
		
		for (int i = 0; i < total.size(); i++) {
			logger.println(total.get(i)[1] + "," + total.get(i)[3] + "," + total.get(i)[5]);
		}
		logger.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("datastorage/remAllData.txt"); //CHANGE name here for new data set
		ArrayList<String[]> total = openFile(file);
		doDataLC(total);
		doDataSL(total);
		doDataSC(total);
		doDataAll(total);
	}
	
}
