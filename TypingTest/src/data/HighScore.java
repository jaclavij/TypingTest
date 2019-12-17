package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class HighScore {

	private static ConcurrentHashMap<String, Integer> tableEasy = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableMedium = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableHard = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableGod = new ConcurrentHashMap<>();

	public static void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("highScores/tableEasy.txt")));
			oos.writeObject(tableEasy);
			oos.flush();
			oos.close();
			
			oos = new ObjectOutputStream(new FileOutputStream(new File("highScores/tableMedium.txt")));
			oos.writeObject(tableMedium);
			oos.flush();
			oos.close();
			
			oos = new ObjectOutputStream(new FileOutputStream(new File("highScores/tableHard.txt")));
			oos.writeObject(tableHard);
			oos.flush();
			oos.close();
			
			oos = new ObjectOutputStream(new FileOutputStream(new File("highScores/tableGod.txt")));
			oos.writeObject(tableGod);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void load() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("highScores/tableEasy.txt")));
			tableEasy = (ConcurrentHashMap<String, Integer>) ois.readObject();
			ois.close();

			ois = new ObjectInputStream(new FileInputStream(new File("highScores/tableMedium.txt")));
			tableMedium = (ConcurrentHashMap<String, Integer>) ois.readObject();
			ois.close();

			ois = new ObjectInputStream(new FileInputStream(new File("highScores/tableHard.txt")));
			tableHard = (ConcurrentHashMap<String, Integer>) ois.readObject();
			ois.close();

			ois = new ObjectInputStream(new FileInputStream(new File("highScores/tableGod.txt")));
			tableGod = (ConcurrentHashMap<String, Integer>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void count() {
		System.out.println("Easy: " + tableEasy.size());
		System.out.println("Medium: " + tableMedium.size());
		System.out.println("Hard: " + tableHard.size());
		System.out.println("God: " + tableGod.size());
	}

	public static ConcurrentHashMap<String, Integer> getTableEasy() {
		return tableEasy;
	}

	public static ConcurrentHashMap<String, Integer> getTableMedium() {
		return tableMedium;
	}

	public static ConcurrentHashMap<String, Integer> getTableHard() {
		return tableHard;
	}

	public static ConcurrentHashMap<String, Integer> getTableGod() {
		return tableGod;
	}

}
