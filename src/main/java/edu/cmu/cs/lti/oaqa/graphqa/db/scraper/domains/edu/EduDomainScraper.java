package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu;

import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.schema.Schema;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.DomainScraper;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components.CourseNodeScraper;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components.ProfessorNodeScraper;

/**
 * Domain for .edu domain
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class EduDomainScraper extends DomainScraper {

	@Override
	public void scrapeData(Graph g, Map<Schema, Set<String>> urls)
			throws GraphBuilderException {

		ProfessorNodeScraper profNodes = new ProfessorNodeScraper();
		profNodes.scrapeData(g, urls.get(Schema.professor));

		CourseNodeScraper courseNodes = new CourseNodeScraper();
		courseNodes.scrapeData(g, urls.get(Schema.course));

	}

}
