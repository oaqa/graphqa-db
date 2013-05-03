package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewOffice {

	public static String getOffice(Document doc, String url) {
		String office = "";
		String[] whole = doc.html().split("<");
		Pattern pattern1 = Pattern
				.compile("([1-9]{1}[0-9]+\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Hall|([1-9]{1}[0-9]{2,3}\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Complex|([1-9]{1}[0-9]{2,3}\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Center[s]?");
		Pattern pattern2 = Pattern
				.compile("([A-Z]{1}[a-z]+\\W{1})*Complex(\\ [1-9]{1}[0-9]{2,3}){1}|([A-Z]{1}[a-z]+\\W{1})*Center[s]?(\\ [1-9]{1}[0-9]{2,3}){1}|([A-Z]{1}[a-z]+\\W{1})*Hall(\\ [1-9]{1}[0-9]{2,3}){1}");
		Pattern pattern3 = Pattern.compile("[A-Z]{3}\\ [1-9]{1}[0-9]{2,3}");
		Pattern pattern4 = Pattern.compile("[1-9]{1}[0-9]+SCRG");
		Pattern pattern5 = Pattern.compile("\\b[1-9][0-9]{2,3}\\ [A-Z]{3}\\b");
		boolean suspect = false;
		for (int ii = 0; ii < whole.length; ii++) {
			whole[ii] = whole[ii].replace("-", " ");
			whole[ii] = whole[ii].replace(" and ", " ");
			Matcher matcher = pattern1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9]{1}[9|0|1]{1}[0-9]{1}.*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern2.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern3.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern4.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}

			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern5.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
		}

		if ((office == "" || suspect == true)) {
			System.out.println("office oops");
			Elements newlinks = doc.getElementsByAttributeValueMatching("href",
					".*contact.*");
			// System.out.println(newlinks);
			int i = 0;
			while (i < newlinks.size()) {
				String href = newlinks.get(i).attr("href");
				if (href == null || href.length() == 0 || href.contains("us"))
					break;
				else
					href = href.trim();
				// System.out.println("old="+href);
				String newLink = "def";
				if (url.endsWith("/")) {
					newLink = url + href;
				} else {
					newLink = url + "/" + href;
				}
				// System.out.println("new="+newLink);

				if (newLink.indexOf("#") != newLink.lastIndexOf("#")) {
					break;
				}

				// need to decide if the content is in the page
				Document newdoc;

				try {
					Connection c = Jsoup.connect(newLink);
					c.timeout(10000);
					newdoc = c.get();
				} catch (Exception e) {
					System.out.println("Could not read URL: " + url);
					System.out.println(e.getMessage());
					return null;
				}

				office = getOfficeOnce(newdoc, newLink);

				i++;
			}
		}
		return office;
	}

	private static String getOfficeOnce(Document newdoc, String newLink) {
		String office = "";
		String[] whole = newdoc.html().split("<");
		Pattern pattern1 = Pattern
				.compile("([1-9]{1}[0-9]+\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Hall|([1-9]{1}[0-9]{2,3}\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Complex|([1-9]{1}[0-9]{2,3}\\ ){1}([A-Z]{1}[a-z]+\\W{1})*Center[s]?");
		Pattern pattern2 = Pattern
				.compile("([A-Z]{1}[a-z]+\\W{1})*Complex(\\ [1-9]{1}[0-9]{2,3}){1}|([A-Z]{1}[a-z]+\\W{1})*Center[s]?(\\ [1-9]{1}[0-9]{2,3}){1}|([A-Z]{1}[a-z]+\\W{1})*Hall(\\ [1-9]{1}[0-9]{2,3}){1}");
		Pattern pattern3 = Pattern.compile("[A-Z]{3}\\ [1-9]{1}[0-9]{2,3}");
		Pattern pattern4 = Pattern.compile("[1-9]{1}[0-9]+SCRG");
		boolean suspect = false;
		for (int ii = 0; ii < whole.length; ii++) {
			whole[ii] = whole[ii].replace("-", " ");
			whole[ii] = whole[ii].replace(" and ", " ");
			Matcher matcher = pattern1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9]{1}[9|0|1]{1}[0-9]{1}.*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern2.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern3.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					;
					System.out.println(tmpOffice);
				}
			}
			if (office.length() > 1 && suspect == false)
				continue;
			matcher = pattern4.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpOffice = (matcher.group());
				if (tmpOffice.matches(".*[1|2]{1}[0|9][9|0|1][0-9].*")) {
					suspect = true;
				} else {
					office = tmpOffice;
					System.out.println(tmpOffice);
				}
			}
		}
		return office;
	}
}
