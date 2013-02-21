package edu.cmu.cs.lti.oaqa.graphqa.db.crawler.utils;

/**
 * Utility methods used by the crawler
 * 
 * @author Puneet Ravuri
 * 
 */
public class DataSourceCrawlerUtils {

	/**
	 * Returns the domain name of the provided URL
	 * 
	 * @param url
	 *            URL provided
	 * @return Domain Name
	 */
	public static String getDomainName(String url) {

		if (url == null || url.length() == 0)
			return null;

		int beginIndex = 0;
		if (url.startsWith("http")) {
			if (url.startsWith("http://www.") || url.startsWith("https://www.")) {
				beginIndex = url.indexOf("www.") + 4;
				if (beginIndex == 3)
					return null;
			} else {
				beginIndex = url.indexOf("//") + 2;
				if (beginIndex == 1)
					return null;
			}
		} else
			return null;

		int endIndex = url.indexOf('/', beginIndex);
		if (endIndex == -1)
			endIndex = url.length();

		return url.substring(beginIndex, endIndex);
	}

	/**
	 * Returns the parent URL of the provided URL. The returned parent URL will
	 * be used to join with the resource in the same location
	 * 
	 * @param url
	 *            Input URL
	 * @return Parent URL of the provided URL
	 */
	public static String getParentURL(String url) {

		if (url == null || url.length() == 0)
			return null;

		if (!url.startsWith("http"))
			return null;

		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}

		int endIndex = url.lastIndexOf('/');
		if (endIndex == 6 || endIndex == 7)
			return url;
		else
			return url.substring(0, endIndex);

	}

	/**
	 * Returns the root URL of the provided URL.
	 * 
	 * @param url
	 *            Input URL
	 * @return Root URL for the input
	 */
	public static String getRootURL(String url) {

		if (url == null || url.length() == 0)
			return null;

		int begin = 0;
		if (url.startsWith("http")) {
			begin = url.indexOf("//") + 2;
			if (begin == 1)
				return null;

		} else
			return null;

		int endIndex = url.indexOf('/', begin);
		if (endIndex == -1)
			endIndex = url.length();

		return url.substring(0, endIndex);
	}

	/**
	 * Test driver for the utility methods
	 * 
	 * @param args
	 *            cmd line arguments
	 */
	public static void main(String[] args) {
		System.out.println(getDomainName("http://www.cs.cmu.edu/index.html"));
		System.out
				.println(getParentURL("http://www.cs.cmu.edu/photos/index.html"));
	}
}
