package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class NewEmail {
	public static String getEmail(Document doc, String url, String person) {
		String returnEmail = "";
		// if the url is not an official personal page, set the user id after
		// the tilde as the name in the Email address
		if (url.contains("~")) {
			String[] whole = doc.html().split("<");
			Pattern pattern1 = Pattern
					.compile("[a-zA-Z0-9]+[\\+]?\\@[.a-zA-Z]+(edu)");
			for (int ii = 0; ii < whole.length; ii++) {
				whole[ii] = whole[ii].replace("-", " ");
				whole[ii] = whole[ii].replace(" and ", " ");
				Matcher matcher = pattern1.matcher(whole[ii]);
				while (matcher.find()) {
					returnEmail = (matcher.group());
					System.out.println(returnEmail);
				}
			}
			if (returnEmail != "")
				return returnEmail;
			int b = url.indexOf("~") + 1;
			int en = 0;
			if (url.indexOf("/", b) == -1) {
				en = url.length();
			} else
				en = url.indexOf("/", b);
			String ID = (String) url.subSequence(b, en);
			returnEmail = ID + "@cs.cmu.edu";
		}
		// else it seems to be an official, use regex matching
		else {
			String[] whole = doc.html().split("<");
			Pattern pattern1 = Pattern
					.compile("[a-zA-Z0-9]+[\\+]?\\@[.a-zA-Z]+(edu)");
			for (int ii = 0; ii < whole.length; ii++) {
				whole[ii] = whole[ii].replace("-", " ");
				whole[ii] = whole[ii].replace(" and ", " ");
				Matcher matcher = pattern1.matcher(whole[ii]);
				while (matcher.find()) {
					returnEmail = (matcher.group());
					System.out.println(returnEmail);
				}
			}
		}
		return returnEmail;
	}
}
