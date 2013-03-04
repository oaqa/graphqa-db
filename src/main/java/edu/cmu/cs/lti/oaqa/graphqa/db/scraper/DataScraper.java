package edu.cmu.cs.lti.oaqa.graphqa.db.scraper;

import java.util.Set;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.constants.GraphBuilderConstants.Domain;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.com.ComDomainScraper;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.EduDomainScraper;

/**
 * Performs the data scraping and populates the graph database
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class DataScraper {

	/**
	 * Scrapes the provided URLs and creates graph database
	 * 
	 * @param g
	 *            Graph database
	 * @param d
	 *            Domain of the website
	 * @param urls
	 *            Set of URLs to be scraped
	 * @throws GraphBuilderException
	 */
	public void scrapeData(Graph g, Domain d, Set<String> urls)
			throws GraphBuilderException {

		switch (d) {
		case com:
			ComDomainScraper comScraper = new ComDomainScraper();
			comScraper.scrapeData(g, urls);
			break;
		case edu:
			EduDomainScraper eduScraper = new EduDomainScraper();
			eduScraper.scrapeData(g, urls);
			break;
		}
	}

}
