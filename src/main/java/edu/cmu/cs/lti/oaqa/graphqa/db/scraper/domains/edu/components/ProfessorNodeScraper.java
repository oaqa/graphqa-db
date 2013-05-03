package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;

/**
 * Represents a scraper module for professors
 * 
 * @author Wenyi Wang
 * 
 */
public class ProfessorNodeScraper {

	private static Map<String, Set<String>> pLinks = new HashMap<String, Set<String>>();

	public void scrapeData(Graph g, Set<String> urls)
			throws GraphBuilderException {
		Map<String, String> s = new HashMap<String, String>();

		for (String url : urls) {
			int b = 0;
			int e = url.indexOf("edu") + 3;
			String domain = url.substring(b, e);
			System.out.println(domain);
			// use getURL method to return a list of urls(these are offical
			// pages) on a list page
			s = getURLs(domain, url);
			if (s == null)
				continue;

			for (Entry<String, String> entry : s.entrySet()) {
				String person = entry.getKey();
				String urlValue = entry.getValue();
				Set<String> tmpLinkSet = ScrapeHelper.scrapeData(g, person,
						urlValue);
				if (tmpLinkSet == null)
					continue;
				pLinks.put(person, tmpLinkSet);
			}
		}

		System.out.println("unique personal links");
		Set<String> checkDup = new HashSet<String>();
		for (Entry<String, Set<String>> entry : pLinks.entrySet()) {
			String person = entry.getKey();
			Set<String> urlSet = entry.getValue();
			for (String l : urlSet) {
				if (checkDup.contains(l))
					continue;
				checkDup.add(l);
				ScrapeHelper.scrapeData(g, person, l);
			}
		}
		System.out.println("unique personal links: " + checkDup.size());
	}

	// get the official pages in a list page
	private static Map<String, String> getURLs(String domain, String url) {
		int count = 0;
		Map<String, String> s = new HashMap<String, String>();
		Document doc = null;
		try {
			Connection c = Jsoup.connect(url);
			c.timeout(10000);
			doc = c.get();
		} catch (Exception e) {
			System.out.println("Could not read URL: " + url);
			System.out.println(e.getMessage());
			return null;
		}
		Elements links = doc.getElementsByAttribute("href");
		for (int j = 0; j < links.size(); j++) {
			String tmpName = links.get(j).text();
			String[] tmp = tmpName.split("\\ ");
			if (tmp.length > 3 || tmp.length < 2)
				continue;
			boolean flag = false;
			for (String x : tmp) {
				if (x.matches("[A-Z]+")) {
					flag = true;
					break;
				}
			}
			if (flag == true)
				continue;
			String tmpLink = links.get(j).attr("href");
			if (tmpLink.contains(".com"))
				continue; // other domain
			if (!tmpLink.contains("http://")) {
				tmpLink = domain + tmpLink;
			}
			if (tmpLink.contains("id") || tmpLink.contains("Person")) {
				System.out.println(tmpName);
				System.out.println(tmpLink);
				s.put(tmpName, tmpLink);
				count++;
			}
		}
		System.out.println(count + "========================");
		System.out.println(s);
		return s;
	}

}
