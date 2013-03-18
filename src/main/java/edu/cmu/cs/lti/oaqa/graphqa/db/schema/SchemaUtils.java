package edu.cmu.cs.lti.oaqa.graphqa.db.schema;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.cmu.cs.lti.oaqa.graphqa.db.schema.Schema;

/**
 * Provides utility methods for schema related operations
 * 
 * @author Puneet Ravuri
 * 
 */
public class SchemaUtils {

	/**
	 * Populates the map that crawler uses with the corresponding schema
	 * elements. All the schema elements will be used as keys by the crawler
	 * 
	 * @param urls
	 *            Map that crawler uses
	 */
	public static void populateCrawlerMap(Map<Schema, Set<String>> urls) {

		urls.put(Schema.professor, new HashSet<String>());
		urls.put(Schema.course, new HashSet<String>());
	}

}
