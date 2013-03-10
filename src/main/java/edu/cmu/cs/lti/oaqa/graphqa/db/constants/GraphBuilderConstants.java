package edu.cmu.cs.lti.oaqa.graphqa.db.constants;

/**
 * Container of constants used by the graph builder module
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class GraphBuilderConstants {

	/**
	 * Represents domain of the website
	 * 
	 */
	public static enum Domain {
		com, edu
	};

	/**
	 * Represents the schema whose elements are used as keys by the crawler
	 * 
	 */
	public static enum Schema {
		professor, course, department, program, others
	}
}
