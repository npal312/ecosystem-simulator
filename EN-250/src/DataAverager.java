import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataAverager {
	
	public class DataPoint { //to hold data points in a better manner
		public float trophicLinks;
		public int speciesNum;
		public double connectance;
		
		public DataPoint(float trophicLinks, int speciesNum, double connectance) {
			this.trophicLinks = trophicLinks;
			this.speciesNum = speciesNum;
			this.connectance = connectance;
		}
		
		public String toString() {
			return "Links: " + this.trophicLinks + " Species: " + this.speciesNum + " Connectance: " + this.connectance;
		}
	}

	public DataAverager() {
		
	}
	
	public ArrayList<String[]> openFile(File file) throws FileNotFoundException {
		ArrayList<String[]> partial = new ArrayList<String[]>();
		
		Scanner fileContents = new Scanner(file);
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			String[] content = contents.split(" ");
			partial.add(content);
		}
		fileContents.close();
		
		return partial;
	}
	
	public ArrayList<ArrayList<String[]>> openAllFiles(int n) throws FileNotFoundException {
		ArrayList<ArrayList<String[]>> total = new ArrayList<ArrayList<String[]>>();
		
		for (int i = 0; i < n; i++) {
			File file = new File("datastorage/rem" + i + "Data.txt"); //CHANGE name here for new data set
			ArrayList<String[]> partial = openFile(file);
			total.add(partial);
		}
		
		return total;
	}
	
	public float getConnectance(float trophicLinks, int speciesNumber) {
		float links = (float) trophicLinks;
		float species = (float) speciesNumber;
		return (links / ((species * (species - 1)) / 2));
	}
	
	public ArrayList<DataPoint> takeAverage(ArrayList<ArrayList<String[]>> total) { //make sure all data files have same amount of data points
		
		float linksTotal = 0;
		
		ArrayList<DataPoint> newData = new ArrayList<DataPoint>();
		
		for (int i = 0; i < total.get(0).size(); i++) {
			for (int j = 0; j < total.size(); j++) {
				linksTotal += Float.parseFloat(total.get(j).get(i)[1]); //links
			}
			linksTotal /= total.size();
			float connectanceAverage = getConnectance(linksTotal, Integer.parseInt(total.get(0).get(i)[3]));

			newData.add(new DataPoint(linksTotal, Integer.parseInt(total.get(0).get(i)[3]), connectanceAverage));
		}
		
		return newData;
	}
	
	public void saveData(String name, ArrayList<DataPoint> data) throws FileNotFoundException {
		File file = new File("datastorage/" + name + "Data.txt"); //make name same as ecosystem name for simplicity
		PrintWriter logger = new PrintWriter(file);
		
		for (int i = 0; i < data.size(); i++) {
			logger.println(data.get(i).toString());
		}
		
		logger.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DataAverager d = new DataAverager();
		ArrayList<ArrayList<String[]>> total = d.openAllFiles(20);
		ArrayList<DataPoint> data = d.takeAverage(total);
		d.saveData("remAll", data); //CHANGE name here for new data set
	}
	
}
