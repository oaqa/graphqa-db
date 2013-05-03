package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class NewTitle {

	public static String getTitle(Document doc) {
		String title = "unknown";
		// cannot use space as a split
		String[] whole = doc.html().split("<");
		Pattern pattern1 = Pattern.compile("A[a-z]+\\ Professor");
		Pattern pattern2 = Pattern.compile("\\bProfessor\\b");
		Pattern pattern3 = Pattern
				.compile("\\b([A-Z][a-z]+\\ ){1,2}Scientist\\b");
		Pattern pattern4 = Pattern.compile("\\bResearch\\ A{1}[a-z]+\\b");
		// Pattern pattern5 = Pattern.compile("\\b([A-Z][a-z]+\\ )*Faculty\\b");
		for (int ii = 0; ii < whole.length; ii++) {
			Matcher matcher = pattern1.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpTitle = (matcher.group());
				System.out.println(tmpTitle);
				title = tmpTitle;
			}
			if (title != "unknown")
				continue;
			matcher = pattern2.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpTitle = (matcher.group());
				System.out.println(tmpTitle);
				title = tmpTitle;
			}
			if (title != "unknown")
				continue;
			matcher = pattern3.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpTitle = (matcher.group());
				System.out.println(tmpTitle);
				title = tmpTitle;
			}
			if (title != "unknown")
				continue;
			matcher = pattern4.matcher(whole[ii]);
			while (matcher.find()) {
				String tmpTitle = (matcher.group());
				System.out.println(tmpTitle);
				title = tmpTitle;
			}
		}
		return title;
	}
}
