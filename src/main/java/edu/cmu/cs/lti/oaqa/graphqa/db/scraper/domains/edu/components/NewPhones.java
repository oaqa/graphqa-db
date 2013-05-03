package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewPhones {

	public static Set<String> getPhones(Document doc, String url) {
		Set<String> phones = new HashSet<String>();
		String[] whole = doc.html().split("<");

		if (phones.size() != 0)
			return phones;

		Pattern pattern1 = Pattern
				.compile("\\(?[1-9]\\d{2}[\\)\\ ]{1}[.-]?\\ {1}[1-9]\\d{2}[.-]?\\ ?\\d{4}");
		Pattern patternPlus1 = Pattern
				.compile("[\\+1]{1}[\\W]{1}\\(?[1-9]\\d{2}\\)?.{1}[1-9]\\d{2}.{1}\\d{4}");
		Pattern pattern2 = Pattern
				.compile("\\(?[1-9]\\d{2}\\)?[.-]{1}\\ ?[1-9]\\d{2}[.-]?\\ ?\\d{4}");
		Pattern pattern3 = Pattern
				.compile("\\(?[1-9]\\d{2}\\)?\\ {1}[1-9]\\d{2}[.-]{1}\\d{4}");
		for (int ii = 0; ii < whole.length; ii++) {
			Matcher matcher = pattern1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = patternPlus1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = pattern2.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = pattern3.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
		}

		if (phones.size() == 0) {
			Elements phonelinks = doc.getElementsByAttributeValueMatching(
					"href", ".*contact.*");
			// System.out.println(phonelinks);
			int i = 0;
			while (i < phonelinks.size()) {
				String href = phonelinks.get(i).attr("href");
				if (href == null || href.length() == 0)
					continue;
				else
					href = href.trim();
				// System.out.println(href);
				String newLink = "def";
				if (url.endsWith("/")) {
					newLink = url + href;
				} else {
					newLink = url + "/" + href;
				}
				// System.out.println(newLink);

				if (newLink.indexOf("#") != newLink.lastIndexOf("#")) {
					return null;
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

				phones = getPhonesOnce(newdoc, newLink);

				i++;
			}
		}

		return phones;

	}

	private static Set<String> getPhonesOnce(Document newdoc, String newLink) {
		Set<String> phones = new HashSet<String>();
		String[] whole = newdoc.html().split("<");

		if (phones.size() != 0)
			return phones;

		Pattern pattern1 = Pattern
				.compile("\\(?[1-9]\\d{2}[\\)\\ ]{1}[.-]?\\ {1}[1-9]\\d{2}[.-]?\\ ?\\d{4}");
		Pattern patternPlus1 = Pattern
				.compile("[\\+1]{1}[\\W]{1}\\(?[1-9]\\d{2}\\)?.{1}[1-9]\\d{2}.{1}\\d{4}");
		Pattern pattern2 = Pattern
				.compile("\\(?[1-9]\\d{2}\\)?[.-]{1}\\ ?[1-9]\\d{2}[.-]?\\ ?\\d{4}");
		Pattern pattern3 = Pattern
				.compile("\\(?[1-9]\\d{2}\\)?\\ {1}[1-9]\\d{2}[.-]{1}\\d{4}");
		for (int ii = 0; ii < whole.length; ii++) {
			Matcher matcher = pattern1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = patternPlus1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = pattern2.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
			if (phones.size() != 0)
				continue;
			matcher = pattern3.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpPhone = (matcher.group());
				System.out.println(tmpPhone);
				phones.add(tmpPhone);
			}
		}
		return phones;
	}

}
