package Classifiers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FeatureVector {

	ArrayList<String[]> index;

	public FeatureVector(String key) {
		String fileName = "CleanData\\Training Set\\" + key + "_Raw_TrainingSet.csv";
		Scanner input;
		index = new ArrayList<>();
		String[] featureVector;

		try {
			input = new Scanner(new File(fileName));
			while (input.hasNextLine()) {
				featureVector = input.nextLine().split(" ");
				if (featureVector.length > 0)
					index.add(featureVector);
				System.out.println(Arrays.toString(featureVector));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
