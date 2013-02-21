package edu.cmu.cs.lti.oaqa.graphqa.db;

import java.util.Set;

import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.DataSourceCrawler;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;

/**
 * Generates a knowledge graph based on the data extracted from the URL provided
 * as a parameter.
 * 
 * @author Puneet Ravuri
 * 
 */
public class KnowledgeGraphBuilder {

	public static void main(String[] args) {

		if (args.length != 2) {
			usageString();
			return;
		}

		KnowledgeGraphBuilder builder = new KnowledgeGraphBuilder();
		try {
			builder.createGraph(args[0], args[1]);
		} catch (GraphBuilderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a knowledge graph of the content from the provided URL
	 * 
	 * @param url
	 *            URL of the website
	 * @param location
	 *            Location of graph db files
	 */
	public void createGraph(String url, String location)
			throws GraphBuilderException {

		// Crawl the URL to get the list of websites to visit
		System.out.println("Retrieving links from the web page...");
		DataSourceCrawler crawler = new DataSourceCrawler();
		Set<String> urls = crawler.getLinks(url);

		System.out.println("Number of URLs retrieved: " + urls.size());

		// TODO: Scrape the above websites to form the schema

	}

	private static void usageString() {
		System.out.println("Usage:- KnowledgeGraphBuilder <url> <location>\n"
				+ " where\n\t<url>: URL of the website\n\t"
				+ "<location>: Location to create the graph db files");
	}
}
