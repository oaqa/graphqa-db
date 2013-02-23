package edu.cmu.cs.lti.oaqa.graphqa.db;

import java.util.Set;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import edu.cmu.cs.lti.oaqa.graphqa.db.constants.GraphBuilderConstants;
import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.DataSourceCrawler;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.DataScraper;

/**
 * Generates a knowledge graph based on the data extracted from the URL provided
 * as a parameter.
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class KnowledgeGraphBuilder {

	/**
	 * Test driver for KnowledgeGraphBuilder and entry point for command line
	 * version of the graph builder
	 * 
	 * @param args
	 *            Command line args
	 */
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

		// Check the domain support
		if (!(url.contains(".edu") || url.contains(".com"))) {
			throw new GraphBuilderException("Website domain is not supported");
		}

		// Crawl the URL to get the list of websites to visit
		System.out.println("Retrieving links from the web page...");
		DataSourceCrawler crawler = new DataSourceCrawler();
		Set<String> urls = crawler.getLinks(url);
		System.out.println("Number of URLs retrieved: " + urls.size());

		// Create a graph in the provided location
		Graph g = new Neo4jGraph(location);

		// Scrape the content from the provided URLs
		DataScraper scraper = new DataScraper();
		if (url.contains(".edu")) {
			scraper.scrapeData(g, GraphBuilderConstants.Domain.edu, urls);
		} else if (url.contains(".com")) {
			scraper.scrapeData(g, GraphBuilderConstants.Domain.com, urls);
		} else {
			throw new GraphBuilderException("Website domain is not supported");
		}

		// Close the graph
		g.shutdown();
	}

	private static void usageString() {
		System.out.println("Usage:- KnowledgeGraphBuilder <url> <location>\n"
				+ " where\n\t<url>: URL of the website\n\t"
				+ "<location>: Location to create the graph db files");
	}
}
