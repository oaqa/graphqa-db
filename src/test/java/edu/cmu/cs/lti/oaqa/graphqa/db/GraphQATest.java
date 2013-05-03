package edu.cmu.cs.lti.oaqa.graphqa.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import edu.cmu.cs.lti.oaqa.graphqa.db.KnowledgeGraphBuilder;
import edu.cmu.cs.lti.oaqa.graphqa.db.constants.GraphBuilderConstants;
import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.DataSourceCrawler;
import edu.cmu.cs.lti.oaqa.graphqa.db.schema.Schema;
import edu.cmu.cs.lti.oaqa.graphqa.db.scraper.DataScraper;

/**
 * A simple test driver to test the different components of data creation
 * pipeline
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class GraphQATest {

	public static void main(String[] args) {

		try {

			GraphQATest test = new GraphQATest();
			test.testCrawler("http://www.heinz.cmu.edu/", "urlStore.txt");
			test.testScraper(
					"C:\\My Data\\Software\\neo4j-community-1.8.1\\data\\heinznodes",
					"heinz_urlStore_map.txt");
			test.testGraphCreation("http://www.heinz.cmu.edu/",
					"C:\\My Data\\Classes\\Semester 3\\Software Engg 2\\Final\\data");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Tests the scraper utility
	 * 
	 * @param graphLocation
	 *            Location where graph will be created
	 * @param urlMap
	 *            File to which the URL map will be written
	 */
	public void testScraper(String graphLocation, String urlMap) {

		try {
			Graph g = new Neo4jGraph(graphLocation);
			Map<Schema, Set<String>> urls = new HashMap<Schema, Set<String>>();

			BufferedReader br = new BufferedReader(new FileReader(urlMap));

			String line = null;
			String[] content;
			while ((line = br.readLine()) != null) {
				content = line.split(" ");
				Schema sc = Schema.valueOf(content[0]);
				Set<String> urlSet = urls.get(sc);
				if (urlSet == null) {
					urlSet = new HashSet<String>();
					urlSet.add(content[1]);
					urls.put(sc, urlSet);
				} else {
					urlSet.add(content[1]);
				}
			}
			br.close();

			DataScraper s = new DataScraper();
			s.scrapeData(g, GraphBuilderConstants.Domain.edu, urls);
			g.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Tests the crawler+scraper utilities
	 * 
	 * @param domain
	 *            Domain from which data has to be extracted
	 * @param graphLocation
	 *            Location where graph will be created
	 */
	public void testGraphCreation(String domain, String graphLocation) {

		try {
			KnowledgeGraphBuilder k = new KnowledgeGraphBuilder();
			k.createGraph(domain, graphLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tests the crawler utility
	 * 
	 * @param domain
	 *            Domain from which data has to be extracted
	 * @param mapOutput
	 *            File to which the URL map will be written
	 */
	public void testCrawler(String domain, String mapOutput) {
		try {
			DataSourceCrawler c = new DataSourceCrawler();
			Map<Schema, Set<String>> urls = c.getLinks(domain);

			BufferedWriter bw = new BufferedWriter(new FileWriter(mapOutput));

			for (Schema s : urls.keySet()) {
				Set<String> urlList = urls.get(s);
				for (String str : urlList) {
					bw.write(s + ": " + str + "\n");
				}
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
