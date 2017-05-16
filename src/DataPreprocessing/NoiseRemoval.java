package DataPreprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class NoiseRemoval {

	private String mainString = "";

	public NoiseRemoval(String str) {
		String[] words = str.split(",");
		str = "";
		for (int i = 2; i < words.length; i++) {
			str += words[i] + " ";
		}
		// str = str.toLowerCase();
		str = str.replaceAll("((www\\.[^\\s]+)|(https?://[^\\s]+)|(pic.twitter.[^\\s]+))", "");
		str = str.replaceAll("(@[^\\s]+)|(#[^\\s]+)", "");
		mainString = str.trim().replaceAll(" +", " ");
	}

	public String getString() {
		return mainString;
	}

	/**
	 * Outliers removal - To remove low frequent and high frequent words using
	 * Bag of words approach
	 */
	public void outliersRemoval(ArrayList<String> oList) {
		String[] words = mainString.replaceAll("(\\…+)|(\\•+)", " ").split("\\s+");
		String str = "";
		for (String stw : words) {
			if (!oList.contains(stw))
				str += stw + " ";
		}
		mainString = str.trim().replaceAll(" +", " ");
	}

	/**
	 * Keyword Stemming - To reduce inflected words to their stem, base or root
	 * form using porter stemming
	 */
	public void keywordStemming() {
		PotterStemmer stm = new PotterStemmer();
		String[] words = mainString.split("\\s+");
		String str = "";
		for (String wrd : words) {
			stm.add(wrd.toCharArray(), wrd.length());
			stm.stem();
			str += stm.toString() + " ";
		}

		mainString = str.trim().replaceAll(" +", " ");
	}

	/**
	 * Stop words removal - To remove most common words, such as the, is, at,
	 * which, and on
	 */
	public void stopWordRemoval() {
		// mainString = mainString.trim();
		String[] words = mainString.split("\\s+");
		// System.out.println(Arrays.toString(words));
		String str = "";
		for (String stw : words) {
			if (!DataPreprocessingDriver.stopWords.contains(stw))
				str += stw + " ";
		}

		mainString = str.trim().replaceAll(" +", " ");

	}

	/**
	 * Spelling Correction - To correct spellings using Edit distance method
	 */
	public void spellingCorrection() {
		String[] words = mainString.toLowerCase().split("\\s+");
		String str = "";
		for (String wrd : words) {
			str += DataPreprocessingDriver.corrector.correct(wrd) + " ";
		}
		str = str.replaceAll("\\?+", " ");
		mainString = str.trim().replaceAll(" +", " ");
	}

	public void cleanCrawlData() {
		String output = mainString;
		output = output.replaceAll("(\\…+)|(\\•+)", " ");
		output = output.replaceAll("(\\'[a-zA-Z]+)|(\\’[a-zA-Z]+)", "");
		output = output.replaceAll("\\p{Punct}+", " ");
		output = output.replaceAll("[0-9]+", "");
		mainString = output.trim().replaceAll(" +", " ");
	}

	/**
	 * Named Entity Recognition- For ranking result category and finding most
	 * appropriate
	 */
	public void namedEntityRecognition() {
		String output = DataPreprocessingDriver.classifier.apply(mainString);

		output = output.replaceAll("<PERSON>.+</PERSON>", "person");
		output = output.replaceAll("<MONEY>.+</MONEY>", "money");
		output = output.replaceAll("<DATE>.+</DATE>", "date");
		output = output.replaceAll("<ORGANIZATION>.+</ORGANIZATION>", "organization");
		output = output.replaceAll("<LOCATION>.+</LOCATION>", "location");
		output = output.replaceAll("<PERCENT>.+</PERCENT>", "percent");
		output = output.replaceAll("<DURATION>.+</DURATION>", "duration");
		output = output.replaceAll("\\?+", " ");
		mainString = output.trim().replaceAll(" +", " ");
	}

	/**
	 * Given a string word it will return the list of synonyms of the word
	 */
	public static ArrayList<String> getSynonym(String wordForm) {
		// ArrayList<String> synonym = new ArrayList<String>();
		File f = new File("WordNet\\2.1\\dict");
		System.setProperty("wordnet.database.dir", f.toString());
		// setting path for the WordNet Directory
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(wordForm);

		if (synsets.length > 0) {
			ArrayList<String> al = new ArrayList<String>();
			// add elements to al, including duplicates
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0; i < synsets.length; i++) {
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++) {
					// al.add(wordForms[j]);
					hs.add(wordForms[j]);
				}
			}
			al.addAll(hs);
			return al;
		} else {
			System.err.println("No synsets exist that contain the word form '" + wordForm + "'");
			return null;
		}
	}
}
