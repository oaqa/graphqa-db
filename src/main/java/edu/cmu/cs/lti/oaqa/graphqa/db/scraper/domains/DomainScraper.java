package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains;

import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.schema.SchemaConstants.Schema;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;

/**
 * Domain scraper template
 * 
 * @author Puneet Ravuri, Wenyi Wang
 * 
 */
public abstract class DomainScraper {

	/**
	 * Scrapes the provided URLs and creates graph database
	 * 
	 * @param g
	 *            Graph
	 * @param urls
	 *            Provided input URLs
	 * @throws GraphBuilderException
	 */
	public abstract void scrapeData(Graph g, Map<Schema, Set<String>> urls)
			throws GraphBuilderException;
}
