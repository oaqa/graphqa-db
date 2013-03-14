package edu.cmu.cs.lti.oaqa.graphqa.db.scraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.constants.GraphBuilderConstants.Domain;
import edu.cmu.cs.lti.oaqa.graphqa.db.schema.SchemaConstants.Schema;
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
	 *             Thrown when any error during scraping and construction of
	 *             graph
	 */
	public void scrapeData(Graph g, Domain d, Map<Schema, Set<String>> urls)
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

	/**
	 * Retrieves the URLs from the provided file, scrapes them and creates graph
	 * database. The file should have URLs in the form of key-value pairs where
	 * key is the schema entity and value is a URL. There can be multiple values
	 * mapped to a single URL
	 * 
	 * @param g
	 *            Graph database
	 * @param d
	 *            Domain of the website
	 * @param fileName
	 *            Location from where URL map will be picked
	 * @throws GraphBuilderException
	 *             Thrown when any error during scraping and construction of
	 *             graph
	 */
	public void scrapeData(Graph g, Domain d, String fileName)
			throws GraphBuilderException {

		Map<Schema, Set<String>> urls = new HashMap<Schema, Set<String>>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer sToken = new StringTokenizer(line, " ");
				String keyStr = sToken.nextToken();
				String valueStr = sToken.nextToken();

				if (urls.containsKey(Schema.valueOf(keyStr))) {
					Set<String> urlSet = urls.get(Schema.valueOf(keyStr));
					urlSet.add(valueStr);
				} else {
					Set<String> urlSet = new HashSet<String>();
					urlSet.add(valueStr);
					urls.put(Schema.valueOf(keyStr), urlSet);
				}
			}
			br.close();
		} catch (IOException e) {
			throw new GraphBuilderException(e);
		}

		scrapeData(g, d, urls);
	}

}
