package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HighScore {

	private static ConcurrentHashMap<String, Integer> tableEasy = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableMedium = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableHard = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Integer> tableGod = new ConcurrentHashMap<>();
	private static Set<String> tableUsername = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

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

			oos = new ObjectOutputStream(new FileOutputStream(new File("highScores/tableUsername.txt")));
			oos.writeObject(tableUsername);
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

			ois = new ObjectInputStream(new FileInputStream(new File("highScores/tableUsername.txt")));
			tableUsername = (Set<String>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean usernameAvailable(String username) {
		return !tableUsername.contains(username);
	}

	public static void putScore(String key, Integer[] values) {
		tableUsername.add(key);
		if (tableEasy.containsKey(key)) {
			if (tableEasy.get(key) < values[0])
				tableEasy.put(key, values[0]);
		} else if (values[0] > 0) {
			tableEasy.put(key, values[0]);
		}
		if (tableMedium.containsKey(key)) {
			if (tableMedium.get(key) < values[1])
				tableMedium.put(key, values[1]);
		} else if (values[1] > 0) {
			tableMedium.put(key, values[1]);
		}
		if (tableHard.containsKey(key)) {
			if (tableHard.get(key) < values[2])
				tableHard.put(key, values[2]);
		} else if (values[2] > 0) {
			tableHard.put(key, values[2]);
		}
		if (tableGod.containsKey(key)) {
			if (tableGod.get(key) < values[3])
				tableGod.put(key, values[3]);
		} else if (values[3] > 0) {
			tableGod.put(key, values[3]);
		}
		save();
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
