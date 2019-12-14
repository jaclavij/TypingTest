package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Words {
	
	private static List<String> engList;
	private static List<String> espList;
	private static Random ran = new Random();
	
	public static void main(String[] args) {
		load();
	}
	
	public static void load() {
		File engFile = new File("wordData/1-1000.txt");
		File espFile = new File("wordData/comunes.txt");
		engList = new ArrayList<>();
		espList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(engFile)));) {
			String linea;
			while ((linea = br.readLine()) != null) {
				engList.add(linea);
			}
			for (int i = engList.size() - 1; i >= 0; i--) {
				if (engList.get(i).length() < 5)
					engList.remove(engList.get(i));
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(espFile)));) {
			String linea;
			int i = 0;
			while ((linea = br.readLine()) != null && i < 1000) {
				espList.add(linea);
				i++;
			}
			for (int j = espList.size() - 1; j >= 0; j--) {
				if (espList.get(j).length() < 5 || espList.get(j).contains("Ã"))
					espList.remove(espList.get(j));
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static List<String> getEngList() {
		return engList;
	}

	public void setEngList(List<String> newEngList) {
		engList = newEngList;
	}

	public List<String> getEspList() {
		return espList;
	}

	public void setespList(List<String> newEspList) {
		espList = newEspList;
	}
	
	public static String getRandom(String lang) {
		if (lang.equals("ESP"))
			return espList.get(ran.nextInt(espList.size()));
		else
			return engList.get(ran.nextInt(engList.size()));
	}
}
