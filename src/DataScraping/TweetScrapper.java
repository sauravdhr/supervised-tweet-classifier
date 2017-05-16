package DataScraping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TweetScrapper {

	public static void writeToCSV(List<Tweet> tweetList, String keyword) throws Exception {
		// TODO Auto-generated method stub
		PrintWriter pw = new PrintWriter("ScrappedData\\" + keyword + "_Raw_Data.csv");
		pw.println("User handle,Date of tweet,Tweet Data");
		for (Tweet twt : tweetList) {
			pw.println(twt);
		}
		pw.close();
		System.out.println("Write done for : " + keyword);
	}

	public static void main(String[] args) {

		TwitterCriteria criteria = null;

		ArrayList<String> keywords = new ArrayList<String>();
		// reading keywords from catagories.txt
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("categories.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (scanner.hasNext()) {
			String key = scanner.next();
			keywords.add(key);
		}
		scanner.close();

		for (String key : keywords) {
			System.out.println("Keyword : " + key);

			criteria = TwitterCriteria.create().setUsername(null).setQuerySearch(key).setMaxTweets(5000);

			// tweetList = TweetManager.getTweets(criteria);
			List<Tweet> tweetList = TweetManager.getTweets(criteria);
			System.out.println("Loading one for : " + key);

			try {
				writeToCSV(tweetList, key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("All tasks finished.");
	}

}
