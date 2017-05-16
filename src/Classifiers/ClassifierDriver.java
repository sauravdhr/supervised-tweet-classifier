package Classifiers;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * provides the main function to apply all the three classifiers
 */
public class ClassifierDriver {

	public static void writeAccuracy() throws Exception{
		NaiveBayesClassifier nb = new NaiveBayesClassifier();
		SVMClassifier sv = new SVMClassifier();
		RuleBasedClassifier rbc = new RuleBasedClassifier();
		
		nb.loadModel("FeatureExtraction\\nbModel.arff");
		sv.loadModel("FeatureExtraction\\svmModel.arff");
		rbc.initialize();
		rbc.makeModel();
		rbc.loadModel();
		ArrayList<String> keywords = new ArrayList<String>();
		Scanner scanner = null;

		scanner = new Scanner(new File("categories.txt"));
		while (scanner.hasNext()) {
			String key = scanner.next();
			keywords.add(key);
		}
		scanner.close();

		PrintWriter fw = new PrintWriter("FeatureExtraction\\Accuracy.csv");
		fw.println("Catagoty, SVM, Naive, Rule");

		double count, svm, naive, rule;
		for (String key : keywords) {
			System.out.println("Writing for : "+ key);
			String fileName = "CleanData\\Testing set\\" + key + "_Raw_TestingSet.csv";
			Scanner input = new Scanner(new File(fileName));
			count = svm = naive =rule = 0;
			while (input.hasNext()) {
				count++;
				String line = input.nextLine();
				nb.inputString(line);
				nb.makeInstance();
				sv.inputString(line);
				sv.makeInstance();
				if(key.equals(nb.predictValue()))
					naive++;
				if(key.equals(sv.predictValue()))
					svm++;
				if(key.equals(rbc.predict(line)))
					rule++;
//				if((key.equals(nb.predictValue()))&&(key.equals(sv.predictValue()))&&(key.equals(rbc.predict(line))))
//					fw.println("Correct : "+line);
			}
			input.close();
			fw.println(key + "," + String.format("%.2f", svm*100.0/count)+"," + String.format("%.2f", naive*100.0/count)+","+String.format("%.2f", rule*100.0/count));
//			System.out.println(key + ", "+count+" ," + svm + ", " + naive + ", "+rule);
		}
		fw.close();	
		System.out.println("Writing done");
	}
	
	public static void main(String[] args) {

//		NaiveBayesClassifier nbClassifier = new NaiveBayesClassifier();
//		nbClassifier.loadDataset("FeatureExtraction\\training.arff");
//		nbClassifier.evaluate();
//		nbClassifier.learn();
//		nbClassifier.saveModel("FeatureExtraction\\nbModel.arff");
		
//		nbClassifier.load("test.txt");
//		nbClassifier.loadModel("FeatureExtraction\\nbModel.arff");
//		nbClassifier.makeInstance();
//		nbClassifier.classify();
		
//		SVMClassifier svm = new SVMClassifier();
//		svm.loadDataset("FeatureExtraction\\training.arff");
//		svm.evaluate();
//		svm.learn();
//		svm.saveModel("FeatureExtraction\\svmModel.arff");
		
//		svm.load("test.txt");
//		svm.loadModel("FeatureExtraction\\svmModel.arff");
//		svm.load("test.txt");
//		svm.makeInstance();
////		svm.classify();
//		System.out.println(svm.predictValue());
		
		
		try {
			writeAccuracy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
