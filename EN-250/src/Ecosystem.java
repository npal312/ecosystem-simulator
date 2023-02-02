import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Ecosystem {
	
	
	public class Organism {
		
		public String name; //name of organism (either specified or auto-generated)
		public int level;
		public String type; //type of organism (plant, animal, etc.)
		public String lifestyle; //what types of organisms it eats (for animals)
		public ArrayList<String> preyList; //all the organisms it eats
		
		//each value in here is a trophic link, as the links are being designed
		//as predator ---> prey links (instead of double counting them or using both prey and predator links)
		
		public Organism(String name, int level, String type, String eating, ArrayList<String> preyList) {
			this.name = name;
			this.level = level;
			this.type = type;
			this.lifestyle = eating;
			this.preyList = preyList;
		}
		
		public Organism(String name) {
			this.name = name;
			this.preyList = new ArrayList<String>();
		}
		
		public void assignLevel() { //assigns trophic level to organism
			int random = (int)(Math.random()*100 + 1); //number between 1 and 100
			
			if (random <= 5) this.level = 4;       //5% chance
			else if (random <= 20) this.level = 3; //15% chance (20 - 15)
			else if (random <= 50) this.level = 2; //30% chance
			else this.level = 1;                   //50% chance
		}
		
		public void assignType() { //assigns type of organism to organism
			//"plant", "animal"
			if (this.level == 1) this.type = "plant";
			else if (this.level > 1 && this.level <= 4) this.type = "animal";
			else System.out.print("assignType: Organism is not in a trophic level");
		}
		
		public void assignLifestyle() { //assigns what the organism eats
			//"none", "carnivore", "herbivore", "omnivore"
			if (this.level == 1) {
				this.lifestyle = "none"; //basal species, so no prey
			}
			else if (this.level == 2) {
				this.lifestyle = "herbivore"; //get food from plants primarily
			}
			else if (this.level == 3) { //get food from plants or animals
				int random = (int)(Math.random()*100 + 1);
				if (random <= 40) this.lifestyle = "omnivore"; //40% chance
				else this.lifestyle = "carnivore"; //60% chance
			}
			else if (this.level == 4) { //mostly get food from animals (but can also get food from plants)
				int random = (int)(Math.random()*100 + 1);
				if (random <= 10) this.lifestyle = "omnivore"; //10% chance
				else this.lifestyle = "carnivore"; //90% chance
			}
		}
		
		public String preyString() { //preyList will be sorted by commas
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < this.preyList.size(); i++) {
				if (i == this.preyList.size() - 1) sb.append(preyList.get(i));
				else sb.append(preyList.get(i)+ ",");
			}
			
			return sb.toString();
		}
		
		public String toString() {
			return this.name + " " + this.type + " " + this.lifestyle + " " + this.preyString();
		}
		
	}
	
	public class DataPoint { //to hold data points in a better manner
		public int trophicLinks;
		public int speciesNum;
		public double connectance;
		
		public DataPoint(int trophicLinks, int speciesNum, double connectance) {
			this.trophicLinks = trophicLinks;
			this.speciesNum = speciesNum;
			this.connectance = connectance;
		}
		
		public String toString() {
			return "Links: " + this.trophicLinks + " Species: " + this.speciesNum + " Connectance: " + this.connectance;
		}
	}
	
	
	//final int[] foodChance = {0, 20, 40, 55, 65, 75, 80, 85, 90}; //(original) low chance
	//final int[] foodChance = {0, 10, 20, 35, 45, 50, 55, 60, 60}; //(original) high chance
	//final int[] foodChance = {0, 25, 45, 65, 75, 80, 85, 90, 90}; //low chance
	//final int[] foodChance = {0, 30, 55, 70, 80, 85, 90, 95, 95}; //lower chance
	
	final int[] foodChance = {0, 50, 65, 75, 85, 90, 90, 95, 95}; // absoluteLow chance
	//final int[] foodChance = {0, 15, 35, 45, 55, 65, 70, 75, 75}; //medium chance
	//final int[] foodChance = {0, 5, 20, 30, 40, 45, 50, 55, 60}; //high chance //inverse chances of getting another food source (
	//makes it so the more food sources an organism has, the less likely it is to get another
	
	ArrayList<ArrayList<Organism>> trophicLevels; //each index is ArrayList for that trophic level
	ArrayList<DataPoint> data; //to hold all the data points for plotting (either in this or another file)
	
	ArrayList<String> carnivoreNames;
	ArrayList<String> herbivoreNames;
	ArrayList<String> omnivoreNames;
	ArrayList<String> plantNames;
	
	public Ecosystem () {
		
		trophicLevels = new ArrayList<ArrayList<Organism>>();
		data = new ArrayList<DataPoint>();
		
		for (int i = 0; i < 4; i++) { //adding each trophic level
			trophicLevels.add(new ArrayList<Organism>()); //IN ORDER (index 0 is trophic level 1)
		}
	}
	
	public ArrayList<Integer> getRandomIndex(ArrayList<Organism> trophic){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		while (indexes.size() != trophic.size()) {
			int random = (int)(Math.random()*trophic.size());
			if (indexes.contains(random) == false) indexes.add(random);
		}
		
		for (int i = 0; i < indexes.size(); i++) {
			System.out.println(indexes.get(i));
		}
		
		return indexes;
	}
	
	
	public void assignPredators(Organism o) { //assign all predators for given organism
		
		if (o.level == 4) return; //no predators for apex predator (4th level)
		else if (o.level > 0 && o.level < 4) { //between 1 and 3
			int predatorLevel = o.level; // level above it (where the predators would be) (level is 1 more than index so it equals index + 1)
			
			ArrayList<Integer> randomIndex = getRandomIndex(trophicLevels.get(predatorLevel)); //gets indexes according to size but in random order
			
			for (int i = 0; i < randomIndex.size(); i++) {
				int random = (int)(Math.random()*100 + 1); //number between 1 and 100
				int sizePrey = trophicLevels.get(predatorLevel).get(randomIndex.get(i)).preyList.size();
				int chance = 0; //chance of becoming a predator for the given organism
				
				if (sizePrey >= foodChance.length) chance = foodChance[8]; //lowest chance
				else chance = foodChance[sizePrey]; //appropriate chance
				
				
				if (trophicLevels.get(predatorLevel).get(randomIndex.get(i)).lifestyle.equals("carnivore")) {
					if (o.type.equals("animal")) {
						if (trophicLevels.get(predatorLevel).get(randomIndex.get(i)).preyList.contains(o.name) == false) { //if carnivore eating animal that it doesn't already eat
							if (random <= 100 - chance) {
								trophicLevels.get(predatorLevel).get(randomIndex.get(i)).preyList.add(o.name);
							}
						}
					}
				}
				else if (trophicLevels.get(predatorLevel).get(randomIndex.get(i)).lifestyle.equals("herbivore")) {
					if (o.type.equals("plant")) {
						if (trophicLevels.get(predatorLevel).get(randomIndex.get(i)).preyList.contains(o.name) == false) { //if herbivore eating plant that it doesn't already eat
							if (random <= 100 - chance) {
								trophicLevels.get(predatorLevel).get(randomIndex.get(i)).preyList.add(o.name);
							}
						}
					}
				}
				else if (trophicLevels.get(predatorLevel).get(i).lifestyle.equals("omnivore")) {
					if (trophicLevels.get(predatorLevel).get(i).preyList.contains(o.name) == false) { //if omnivore eating something that it doesn't already eat
						if (random <= 100 - chance) {
							trophicLevels.get(predatorLevel).get(i).preyList.add(o.name);
						}
					}
				}
			}
		}
	}
	
	
	public void assignPrey(Organism o) { //assign all the prey for given organism
		
		if (o.level == 1) return; //no prey for basal species (1st level)
		else if (o.level > 1 && o.level <= 4) { //between 2 and 4
			int preyLevel = o.level - 2; // level below it (where the prey would be) (since level = index + 1, level below is index - 2)
			
			ArrayList<Integer> randomIndex = getRandomIndex(trophicLevels.get(preyLevel)); //gets indexes according to size but in random order
			
			for (int i = 0; i < randomIndex.size(); i++) {
				int random = (int)(Math.random()*100 + 1); //number between 1 and 100
				int sizePrey = o.preyList.size();
				int chance = 0; //chance of the given organism getting more prey
				
				if (sizePrey >= foodChance.length) chance = foodChance[8]; //lowest chance
				else chance = foodChance[sizePrey]; //appropriate chance
				
				if (o.lifestyle.equals("carnivore")) {
					if (trophicLevels.get(preyLevel).get(i).type.equals("animal")) {
						if (o.preyList.contains(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name) == false) { //if carnivore eating animal that it doesn't already eat
							if (random <= 100 - chance) {
								o.preyList.add(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name);
							}
						}
					}
				}
				else if (o.lifestyle.equals("herbivore")) {
					if (trophicLevels.get(preyLevel).get(i).type.equals("plant")) {
						if (o.preyList.contains(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name) == false) { //if herbivore eating plant that it doesn't already eat
							if (random <= 100 - chance) {
								o.preyList.add(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name);
							}
						}
					}
				}
				else if (o.lifestyle.equals("omnivore")) {
					if (o.preyList.contains(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name) == false) { //if omnivore eating something that it doesn't already eat
						if (random <= 100 - chance) {
							o.preyList.add(trophicLevels.get(preyLevel).get(randomIndex.get(i)).name);
						}
					}
				}
			}
		}
	}
	
	
	public void initializeNames() throws FileNotFoundException { //for adding names into the name arrays using files of words/names
		carnivoreNames = new ArrayList<String>();
		herbivoreNames = new ArrayList<String>();
		omnivoreNames = new ArrayList<String>();
		plantNames = new ArrayList<String>();
		
		Scanner fileContents = new Scanner(new File("names/randomCarnivore.txt"));
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			carnivoreNames.add(contents);
		}
		fileContents.close();
		
		fileContents = new Scanner(new File("names/randomHerbivore.txt"));
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			herbivoreNames.add(contents);
		}
		fileContents.close();
		
		fileContents = new Scanner(new File("names/randomOmnivore.txt"));
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			omnivoreNames.add(contents);
		}
		fileContents.close();
		
		fileContents = new Scanner(new File("names/randomPlant.txt"));
		while (fileContents.hasNext()) {
			String contents = fileContents.nextLine();
			plantNames.add(contents);
		}
		fileContents.close();
	}
	
	public String randomName(ArrayList<String> nameList) { //corresponding ArrayList gets a name chosen and removed
		int random = (int)(Math.random()*nameList.size()); //between 0 and size of list (not including the actual size tho)
		String name = nameList.get(random);
		nameList.remove(random); //so repeat names can't show up
		
		return name;
	}
	
	public void addOrganism(String name, int level, String type, String lifestyle, ArrayList<String> preyList) { //in case any of it is already initialized
		Organism o = new Organism(name); //so organism functions can be used
		
		if (level == 0) { //if no data (besides maybe name) then find everything (will not trigger later if statements)
			o.assignLevel();
			o.assignType();
			o.assignLifestyle();
			if (name.isEmpty() == true) {
				if (o.lifestyle.equals("carnivore")) o.name = randomName(carnivoreNames);
				else if (o.lifestyle.equals("herbivore")) o.name = randomName(herbivoreNames);
				else if (o.lifestyle.equals("omnivore")) o.name = randomName(omnivoreNames);
				else if (o.type.equals("plant")) o.name = randomName(plantNames);
			}
			assignPrey(o);
			assignPredators(o);
		}
		
		//if level is there but type or anything else isn't (goes for other functions but allows to specify generals while randomizing details)
		if (type.isEmpty()) o.assignType();
		
		if (lifestyle.isEmpty()) o.assignLifestyle();
		
		if (name.isEmpty() == true) {
			if (o.lifestyle.equals("carnivore")) o.name = randomName(carnivoreNames);
			else if (o.lifestyle.equals("herbivore")) o.name = randomName(herbivoreNames);
			else if (o.lifestyle.equals("omnivore")) o.name = randomName(omnivoreNames);
			else if (o.type.equals("plant")) o.name = randomName(plantNames);
		}
		
		if (preyList.isEmpty()) assignPrey(o);
		
		assignPredators(o);
		
		trophicLevels.get(o.level - 1).add(o);
		return;
	}
	
	
	public void addBeforeAssign(String name, int level, String type, String lifestyle) { //adding function where predator and prey are assigned later
		Organism o = new Organism(name); //so organism functions can be used
		
		if (level == 0) { //if no data (besides maybe name) then find everything (will not trigger later if statements)
			o.assignLevel();
			o.assignType();
			o.assignLifestyle();
			if (name.isEmpty() == true) {
				if (o.lifestyle.equals("carnivore")) o.name = randomName(carnivoreNames);
				else if (o.lifestyle.equals("herbivore")) o.name = randomName(herbivoreNames);
				else if (o.lifestyle.equals("omnivore")) o.name = randomName(omnivoreNames);
				else if (o.type.equals("plant")) o.name = randomName(plantNames);
			}
		}
		else o.level = level;
		
		//if level is there but type or anything else isn't (goes for other functions but allows to specify generals while randomizing details)
		if (type.isEmpty()) o.assignType();
		else o.type = type;
		
		if (lifestyle.isEmpty()) o.assignLifestyle();
		else o.lifestyle = lifestyle;
		
		if (name.isEmpty() == true) {
			if (o.lifestyle.equals("carnivore")) o.name = randomName(carnivoreNames);
			else if (o.lifestyle.equals("herbivore")) o.name = randomName(herbivoreNames);
			else if (o.lifestyle.equals("omnivore")) o.name = randomName(omnivoreNames);
			else if (o.type.equals("plant")) o.name = randomName(plantNames);
		}
		else o.name = name;
		
		trophicLevels.get(o.level - 1).add(o);
		return;
	}
	
	public boolean removeOrganism(String name) { //removes a given organism (searches by name for it)
		int level = 0; //trophic level of organism
		
		for (int i = 0; i < trophicLevels.size(); i++) {
			for (int j = 0; j < trophicLevels.get(i).size(); j++) {
				if (trophicLevels.get(i).get(j).name.equals(name)) {
					level = i + 1;
					trophicLevels.get(i).remove(j); //removes organism at given index
					
					if (level != 4) { //not apex predator
						for (int k = 0; k < trophicLevels.get(level).size(); k++) {
							if (trophicLevels.get(level).get(k).preyList.contains(name)) { //if organism is prey of one of its potential predators
								trophicLevels.get(level).get(k).preyList.remove(name); //severs all trophic links to organisms (ones coming from organism get removed with it)
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean removeOrganism(String name, int level) { //removes a given organism (searches by name for it but uses level to make it faster)
		int levelUpdated = level - 1;
		for (int j = 0; j < trophicLevels.get(levelUpdated).size(); j++) {
			if (trophicLevels.get(levelUpdated).get(j).name.equals(name)) {
				trophicLevels.get(levelUpdated).remove(j); //removes organism at given index
				
				if (level != 4) { //not apex predator
					for (int k = 0; k < trophicLevels.get(levelUpdated).size(); k++) {
						if (trophicLevels.get(levelUpdated).get(k).preyList.contains(name)) { //if organism is prey of one of its potential predators
							trophicLevels.get(levelUpdated).get(k).preyList.remove(name); //severs all trophic links to organisms (ones coming from organism get removed with it)
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
	public int getTrophicLinks() {
		int trophicLinks = 0;
		
		for (int i = 0; i < trophicLevels.size(); i++) { //each trophic level
			for (int j = 0; j < trophicLevels.get(i).size(); j++) { //each organism in trophic level
				trophicLinks += trophicLevels.get(i).get(j).preyList.size(); //size of preyList (each being a link)
			}
		}
		
		return trophicLinks;
	}
	
	public int getSpeciesNumber() {
		int species = 0;
		
		for (int i = 0; i < trophicLevels.size(); i++) { //each trophic level
			species += trophicLevels.get(i).size(); //size of each trophic level (# of organisms in it)
		}
		
		return species;
	}
	
	public float getConnectance(int trophicLinks, int speciesNumber) {
		float links = (float) trophicLinks;
		float species = (float) speciesNumber;
		return (links / ((species * (species - 1)) / 2));
	}
	
	public DataPoint getData(int links, int species, double connectance) {
		return new DataPoint(links, species, connectance);
	}
	
	public void addData(DataPoint dataPoint) { //may not need function but good to have just in case
		data.add(dataPoint);
	}
	
	
	public void saveEcosystem(String name) throws FileNotFoundException {
		File file = new File("ecostorage/" + name + "Eco.txt"); //make name same as ecosystem name for simplicity
		PrintWriter logger = new PrintWriter(file);
		
		for (int i = 3; i >= 0; i--) {
			int level = i + 1;
			logger.println("Trophic Level " + level + ": " + trophicLevels.get(i).size() + " Organisms");
			ArrayList<Organism> currentLevel = trophicLevels.get(i);
			
			for (int j = 0; j < currentLevel.size(); j++) {
				logger.println(currentLevel.get(j).toString());
			}
			logger.println();
		}
		
		logger.close();
	}
	
	public void saveData(String name) throws FileNotFoundException {
		File file = new File("datastorage/" + name + "Data.txt"); //make name same as ecosystem name for simplicity
		PrintWriter logger = new PrintWriter(file);
		
		for (int i = 0; i < data.size(); i++) {
			logger.println(data.get(i).toString());
		}
		
		logger.close();
	}
	
	
	/*public void testFunc() { //for testing features of subclasses
		Organism tests = new Organism("tests");
		for (int i = 0; i < 10000; i++) {
			int chance = tests.assignLevel();
			if (chance == 0) {
				break;
			}
		}
	}*/
	
	
	public static void main(String[] args) throws FileNotFoundException {
		for (int k = 0; k < 20; k++) {
		Ecosystem test = new Ecosystem();
		test.initializeNames();
		
		ArrayList<String> a = new ArrayList<String>(); //so addOrganism works
		
		//initial species
		test.addBeforeAssign("a", 4, "animal", "carnivore");
		test.addBeforeAssign("g", 3, "animal", "omnivore");
		test.addBeforeAssign("p", 2, "animal", "herbivore");
		test.addBeforeAssign("z", 1, "plant", "none");
		
		for (int i = 0; i < 6; i++) {
			test.addBeforeAssign("", 0, "", ""); //default values so it randomizes them all
		}
		
		//assign predator and prey for them all
		for (int i = 0; i < test.trophicLevels.size(); i++) {
			for (int j = 0; j < test.trophicLevels.get(i).size(); j++) {
				test.assignPrey(test.trophicLevels.get(i).get(j));
			}
		}
		
		//initial data point
		int links = test.getTrophicLinks();
		int species = test.getSpeciesNumber();
		double connectance = test.getConnectance(links, species);
		DataPoint data = test.getData(links, species, connectance);
		test.addData(data);
		
		//test.saveEcosystem("test1");
		//test.saveData("test1");
		
		//adding extra species
		for (int i = 0; i < 25; i++) {
			test.addOrganism("", 0, "", "", a); //default values so it randomizes them all
			links = test.getTrophicLinks();
			species = test.getSpeciesNumber();
			connectance = test.getConnectance(links, species);
			data = test.getData(links, species, connectance);
			test.addData(data);
		}
		
		//saving work
		String name = "rem" + k; //CHANGE name here for new data set
		test.saveEcosystem(name);
		test.saveData(name);
		
		//test.saveEcosystem("test");
		}
	}
	
	
	
}
