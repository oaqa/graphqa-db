package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import org.jsoup.nodes.Document;

public class NewTitle {

	public static String getTitle(Document doc) {
		String title = "unknown";
		// String[] whole = doc.html().split("<p>");
		String[] whole = doc.html().split(">");
		for (int ii = 0; ii < whole.length; ii++) {
			if (whole[ii].contains("Associate")) {
				int b = 0;
				b = whole[ii].indexOf("Associate");
				int e = whole[ii].indexOf("<", b + 10);
				if (b >= e)
					break;
				String tmp = whole[ii].substring(b, e);
				String[] t = tmp.split(" ");
				if (t.length < 8) {
					title = whole[ii].substring(b, e);
				}
				if (title.contains("Special")) {
					// for kai-min chang & jin qin in LTI
					title = "Research Associate (Special Faculty)";
				} else if (title.contains("Editor")) {// for rose
					continue;
				}
				// System.out.println("title = "+title);
			} else if (whole[ii].contains("Professor")) {
				int b = 0;
				b = whole[ii].indexOf("Professor");
				int e = b + 9;
				title = whole[ii].substring(b, e);
				// System.out.println("title = "+title);
				break;
			}

			// Fink is a scientist http://www.cs.cmu.edu/~eugene/
		}
		return title;
	}
}
