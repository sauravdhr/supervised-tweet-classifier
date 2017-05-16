//from url=https://github.com/Jefferson-Henrique/GetOldTweets-java

package DataScraping;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class to getting tweets based on username and optional time constraints
 * 
 * @author Jefferson Henrique
 */
public class TweetManager {

	private static final HttpClient defaultHttpClient = HttpClients.createDefault();

	static {
		Logger.getLogger("org.apache.http").setLevel(Level.OFF);
	}

	/**
	 * @param username
	 *            A specific username (without @)
	 * @param since
	 *            Lower bound date (yyyy-mm-dd)
	 * @param until
	 *            Upper bound date (yyyy-mm-dd)
	 * @param scrollCursor
	 *            (Parameter used by Twitter to do pagination of results)
	 * @return JSON response used by Twitter to build its results
	 * @throws Exception
	 */
	private static String getURLResponse(String username, String since, String until, String querySearch,
			String scrollCursor) throws Exception {
		String appendQuery = "";
		if (username != null) {
			appendQuery += "from:" + username;
		}
		if (since != null) {
			appendQuery += " since:" + since;
		}
		if (until != null) {
			appendQuery += " until:" + until;
		}
		if (querySearch != null) {
			appendQuery += " " + querySearch;
		}

		String url = String.format("https://twitter.com/i/search/timeline?f=realtime&q=%s&src=typd&max_position=%s",
				URLEncoder.encode(appendQuery, "UTF-8"), scrollCursor);

		HttpGet httpGet = new HttpGet(url);
		HttpEntity resp = defaultHttpClient.execute(httpGet).getEntity();

		return EntityUtils.toString(resp);
	}

	public static List<Tweet> getTweets(TwitterCriteria criteria) {
		List<Tweet> results = new ArrayList<>();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			String refreshCursor = null;
			int count = 0;
			outerLace: while (true) {

				JSONObject json = new JSONObject(getURLResponse(criteria.getUsername(), criteria.getSince(),
						criteria.getUntil(), criteria.getQuerySearch(), refreshCursor));
				refreshCursor = json.getString("min_position");
				Document doc = Jsoup.parse((String) json.get("items_html"));
				Elements tweets = doc.select("div.js-stream-tweet");
				// System.out.println(doc.);
				if (tweets.size() == 0) {
					break;
				}

				for (Element tweet : tweets) {
					String userName = tweet.getElementsByClass("username").get(0).text().replaceAll("@", "");

					String tweetText = tweet.select("p.js-tweet-text").text().replaceAll("[^\\u0000-\\uFFFF]", "");
					// tweetText.replaceAll(",", " ");

					long dateMs = Long.valueOf(tweet.select("small.time span.js-short-timestamp").attr("data-time-ms"));
					Date date = new Date(dateMs);
					String dateTime = df.format(date);

					Tweet newTweet = new Tweet(tweetText, userName, dateTime, criteria.getQuerySearch());
					results.add(newTweet);
					System.out.println(count++);
					if (criteria.getMaxTweets() > 0 && results.size() >= criteria.getMaxTweets()) {
						break outerLace;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

}