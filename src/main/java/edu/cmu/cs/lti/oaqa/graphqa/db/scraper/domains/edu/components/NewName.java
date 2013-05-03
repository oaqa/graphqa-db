package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import org.jsoup.nodes.Document;

public class NewName {

	public static String getName(Document doc, String url, String person) {
		// the regex of name is hard, but the name is likely to appear in the
		// title field of the webpage and it may be in the anchor text of a link
		String title = "";
		String returnTitle = "";
		try {
			title = doc.select("title").first().text();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("no such field");
			e.printStackTrace();
		}
		// System.out.println("name = "+title);
		int begin = 0;
		int end = title.indexOf("home");
		if (end == -1) {
			end = title.indexOf("Home");
		}
		if (end == -1) {
			end = title.indexOf("Web");
		}
		if (title.contains(",")) {
			end = title.indexOf(",");
			title = (String) title.subSequence(begin, end);
		}
		if (title.contains("-")) {
			end = title.indexOf("-");
			title = (String) title.subSequence(begin, end);
		}
		if (title.contains("|")) {
			begin = title.indexOf("|") + 1;
			end = title.length() - 1;
		}
		if (end == -1) {
			end = title.indexOf("ResumŽ");
		}
		if (end == -1)
			end = title.length();
		// if there is "Welcome to" in title
		if (title.indexOf("elcome") != -1) {
			begin = title.indexOf("elcome") + 10;
		}
		// System.out.println("begin = "+begin);
		// System.out.println("end = "+end);
		if (title.substring(begin, end).contains("'s")) {
			end = title.indexOf("'");
		}
		String name = title.substring(begin, end);
		if (url.contains("~") || url.contains("user")) {
			// if the page is a personal page, then search for title field and
			// trim the title and save it
			String[] names = name.split(" ");
			if (names.length > 4)
				name = "N/A";
			returnTitle = name;
		} else {
			// if the page is an official page, then it is hard to use regex to
			// match the name, use anchor text instead
			returnTitle = person;
		}
		return returnTitle;
	}
}
