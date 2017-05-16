package DataScraping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Organizer {

	public static void writeDataToCSV(ArrayList<String> keyTweetList, String key) throws Exception {
		long seed = System.nanoTime();
		Collections.shuffle(keyTweetList, new Random(seed));
		PrintWriter fw = new PrintWriter("DividedDataSet\\Raw Data Sets\\" + key + "_Raw_Data.csv");
		fw.println("User handle,Date of tweet,Tweet Data");
		for (String str : keyTweetList) {
			fw.println(str);
		}
		fw.close();

		List<String> trainingSet = keyTweetList.subList(0, (int) (keyTweetList.size() * 0.7));
		fw = new PrintWriter("DividedDataSet\\Training Set\\" + key + "_Raw_TrainingSet.csv");
		fw.println("User handle,Date of tweet,Tweet Data");
		for (String str : trainingSet) {
			fw.println(str);
		}
		fw.close();

		List<String> testingSet = keyTweetList.subList((int) (keyTweetList.size() * 0.7), keyTweetList.size());
		fw = new PrintWriter("DividedDataSet\\Testing set\\" + key + "_Raw_TestingSet.csv");
		fw.println("User handle,Date of tweet,Tweet Data");
		for (String str : testingSet) {
			fw.println(str);
		}
		fw.close();

		List<String> trainingSetPure = trainingSet.subList(0, (int) (trainingSet.size() * 0.8));
		fw = new PrintWriter("DividedDataSet\\Training set pure\\" + key + "_Raw_TrainingSetPure.csv");
		fw.println("User handle,Date of tweet,Tweet Data");
		for (String str : trainingSetPure) {
			fw.println(str);
		}
		fw.close();

		List<String> validationSetPure = trainingSet.subList((int) (trainingSet.size() * 0.8), trainingSet.size());
		fw = new PrintWriter("DividedDataSet\\Validation set\\" + key + "_Raw_ValidationSetPure.csv");
		fw.println("User handle,Date of tweet,Tweet Data");
		for (String str : validationSetPure) {
			fw.println(str);
		}
		fw.close();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String key : keywords) {
			String fileName = "ScrappedData\\" + key + "_Raw_Data.csv";
			ArrayList<String> keyTweetList = new ArrayList<>();
			try {
				Scanner input = new Scanner(new File(fileName));
				while (input.hasNext()) {
					keyTweetList.add(input.nextLine());
				}
				input.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				writeDataToCSV(keyTweetList, key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(key + " completed");
		}

	}

}
