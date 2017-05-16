package DataPreprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class DataPreprocessingDriver {
	static Spelling corrector = null;
	static AbstractSequenceClassifier<CoreLabel> classifier = null;
	static ArrayList<String> stopWords = null;

	public static void writeCleanDataToCSV(ArrayList<NoiseRemoval> keyTweetList, String key) throws Exception {
		PrintWriter fw = new PrintWriter("CleanData\\Raw Data Sets\\" + key + "_Raw_Data.csv");
		for (NoiseRemoval str : keyTweetList) {
			fw.println(str.getString());
		}
		fw.close();

		List<NoiseRemoval> trainingSet = keyTweetList.subList(0, (int) (keyTweetList.size() * 0.7));
		fw = new PrintWriter("CleanData\\Training Set\\" + key + "_Raw_TrainingSet.csv");
		for (NoiseRemoval str : trainingSet) {
			fw.println(str.getString());
		}
		fw.close();

		List<NoiseRemoval> testingSet = keyTweetList.subList((int) (keyTweetList.size() * 0.7), keyTweetList.size());
		fw = new PrintWriter("CleanData\\Testing set\\" + key + "_Raw_TestingSet.csv");
		for (NoiseRemoval str : testingSet) {
			fw.println(str.getString());
		}
		fw.close();

		List<NoiseRemoval> trainingSetPure = trainingSet.subList(0, (int) (trainingSet.size() * 0.8));
		fw = new PrintWriter("CleanData\\Training set pure\\" + key + "_Raw_TrainingSetPure.csv");
		for (NoiseRemoval str : trainingSetPure) {
			fw.println(str.getString());
		}
		fw.close();

		List<NoiseRemoval> validationSetPure = trainingSet.subList((int) (trainingSet.size() * 0.8),
				trainingSet.size());
		fw = new PrintWriter("CleanData\\Validation set\\" + key + "_Raw_ValidationSetPure.csv");
		for (NoiseRemoval str : validationSetPure) {
			fw.println(str.getString());
		}
		fw.close();

	}

	public static String cleanString(String dirty){
		stopWords = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("src\\DataPreprocessing\\Support\\stopwordlist.txt"));
			while (scanner.hasNext()) {
				String key = scanner.next();
				stopWords.add(key);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
//		try {
//			corrector = new Spelling("src\\DataPreprocessing\\Support\\big.txt");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		try {
			classifier = CRFClassifier
					.getClassifier("src\\DataPreprocessing\\Support\\english.muc.7class.distsim.crf.ser.gz");
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		NoiseRemoval nr = new NoiseRemoval(" , ,"+dirty);
		nr.namedEntityRecognition();
		nr.cleanCrawlData();
		nr.keywordStemming();
//		nr.spellingCorrection();
		nr.stopWordRemoval();
		
		return nr.getString();
	}
	
	
	/*
	 * Main Function
	 */
	public static void main(String[] args) {

		ArrayList<String> keywords = new ArrayList<String>();
		stopWords = new ArrayList<String>();
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

		try {
			scanner = new Scanner(new File("src\\DataPreprocessing\\Support\\stopwordlist.txt"));
			while (scanner.hasNext()) {
				String key = scanner.next();
				stopWords.add(key);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			corrector = new Spelling("src\\DataPreprocessing\\Support\\big.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			classifier = CRFClassifier
					.getClassifier("src\\DataPreprocessing\\Support\\english.muc.7class.distsim.crf.ser.gz");
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
//		System.out.println("Keyword loading DONE");

		ArrayList<NoiseRemoval> noisyTweetList = null;

		// keywords.clear();
		// keywords.add("Entertainment");
		for (String key : keywords) {
			String fileName = "DividedDataSet\\Raw Data Sets\\" + key + "_Raw_Data.csv";

			noisyTweetList = new ArrayList<>();

			try {
				Scanner input = new Scanner(new File(fileName));
				input.nextLine();
				while (input.hasNext()) {
					noisyTweetList.add(new NoiseRemoval(input.nextLine()));
				}
				input.close();
				System.out.println("noisyTweetList size : " + noisyTweetList.size());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			// int count = 0;
			for (NoiseRemoval nr : noisyTweetList) {
				nr.namedEntityRecognition();
				nr.cleanCrawlData();
				nr.keywordStemming();
				nr.spellingCorrection();
				nr.stopWordRemoval();
				// System.out.println(count++ + " " + nr.getString());
			}
			System.out.println("Half way Done");

			BagOfWords bow = new BagOfWords(noisyTweetList);
			ArrayList<String> low = bow.getList();

			for (NoiseRemoval nr : noisyTweetList) {
				nr.outliersRemoval(low);
			}

			try {
				writeCleanDataToCSV(noisyTweetList, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(key + " completed");
		}

		// System.out.println(NoiseRemoval.getSynonym("teacher"));
	}

}
