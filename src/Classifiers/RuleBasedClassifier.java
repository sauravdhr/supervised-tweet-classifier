package Classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class RuleBasedClassifier {
	
	HashMap<String, Integer[]> totalMap;
	ArrayList<String> keywords;
	List<Map.Entry<String, Integer[]>> entries;
	HashMap<String, Integer> catagoryMap;
	HashMap<String, Integer> maxCountMap;
	
	public void initialize(){
		totalMap = new HashMap<String, Integer[]>();
		Scanner input;
		keywords = new ArrayList<String>();
		
		try {
			input = new Scanner(new File("categories.txt"));
			while (input.hasNext()) {
				String key = input.next();
				keywords.add(key);
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	
		for(int i = 0; i<keywords.size(); i++){
			String fileName = "CleanData\\Training Set\\" + keywords.get(i) + "_Raw_TrainingSet.csv";
			try {
				input = new Scanner(new File(fileName));
				while (input.hasNext()) {
					String next = input.next();
		            if (totalMap.containsKey(next)) {
		            	Integer[] array = totalMap.get(next);
		            	array[i]+=1;
		            	totalMap.put(next, array);
		            } else {
		            	Integer[] array = new Integer[5];
		            	Arrays.fill(array, 0);
		            	array[i]=1;
		            	totalMap.put(next, array);
		            }
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		entries = new ArrayList<Map.Entry<String, Integer[]>>(totalMap.entrySet());
	}
	
	public void print(){
//		List<Map.Entry<String, Integer[]>> entries = new ArrayList<Map.Entry<String, Integer[]>>(totalMap.entrySet());
		for(int i = 0; i < entries.size(); i++){
			System.out.println(i + " : "+ entries.get(i).getKey()+" -> "+Arrays.toString(entries.get(i).getValue()));
        }
	}
	
	public void makeModel() {
		PrintWriter fw = null;
		try {
			fw = new PrintWriter("FeatureExtraction\\RuleModel.txt");
			for(int i = 0; i < entries.size(); i++){
//				System.out.println(i + " : "+ entries.get(i).getKey()+" -> "+Arrays.toString(entries.get(i).getValue()));
				Integer[] arr = entries.get(i).getValue();
				int maxpos = 0; 
				int maxval = arr[0];
				for(int j = 1; j < arr.length; j++){
					if(arr[j] > arr[maxpos]){
						maxpos = j;
						maxval = arr[j];
					}
				}
				fw.println(entries.get(i).getKey() + ","+ maxpos + ","+maxval);
	        }
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadModel(){
		catagoryMap = new HashMap<>();
		maxCountMap = new HashMap<>();
		Scanner input;		
		try {
			input = new Scanner(new File("FeatureExtraction\\RuleModel.txt"));
			while (input.hasNextLine()) {
				String[] line = input.nextLine().split(",");
				catagoryMap.put(line[0], Integer.parseInt(line[1]));
				maxCountMap.put(line[0], Integer.parseInt(line[2]));
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Load complete");
	}
	
	public String predict(String str){
		String[] input = str.split(" ");
		Integer[] point = new Integer[5];
		Arrays.fill(point, 0);
		for(String word: input){
			if(catagoryMap.containsKey(word))
				point[catagoryMap.get(word)]+=maxCountMap.get(word);
		}
		int maxCat = 0;
		for(int i=1; i<point.length; i++){
			if(point[i] > point[maxCat])
				maxCat = i;
		}
//		System.out.println("Max : "+maxCat);
		return keywords.get(maxCat);
	}
	
	public static void main(String[] args) {
		RuleBasedClassifier rbc = new RuleBasedClassifier();
		rbc.initialize();
		rbc.print();
		rbc.makeModel();
		rbc.loadModel();
		String str = "advice mental employe attack minor admit mental problem";
//		rbc.predict(str);
		System.out.println(rbc.predict(str));
	}

}
