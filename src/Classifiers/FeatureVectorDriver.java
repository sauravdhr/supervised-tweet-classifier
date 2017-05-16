package Classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * contains the main function to run the methods of FeatureVector.java
 */



public class FeatureVectorDriver {


	public static void main(String[] args) throws FileNotFoundException {
		
		ArrayList<String> keywords = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("categories.txt"));
			while (scanner.hasNext()) {
				String key = scanner.next();
				keywords.add(key);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintWriter fw = new PrintWriter("FeatureExtraction\\training.arff");

		
		for (String key : keywords) {
			String fileName = "CleanData\\Training Set\\" + key + "_Raw_TrainingSet.csv";
			
			try {
				Scanner input = new Scanner(new File(fileName));
				while (input.hasNext()) {
					String line = input.nextLine();
					if(line.length()>2)
						fw.println(key + ",'"+line+ "'");
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		fw.close();
	}
	
	

}