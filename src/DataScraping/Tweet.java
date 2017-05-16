//from url=https://github.com/Jefferson-Henrique/GetOldTweets-java

package DataScraping;

public class Tweet {
	private String text;
	private String username;

	private String dateTime;

	private String searchKey;

	public Tweet(String text, String username, String dateTime, String searchKey) {
		super();
		this.text = text;
		this.username = username;
		this.dateTime = dateTime;
		this.searchKey = searchKey;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return username + "," + dateTime + "," + text;
	}

}
