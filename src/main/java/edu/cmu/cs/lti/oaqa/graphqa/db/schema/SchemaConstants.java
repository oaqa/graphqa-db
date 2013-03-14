package edu.cmu.cs.lti.oaqa.graphqa.db.schema;

/**
 * Constants used for representing schema elements
 * 
 * @author Puneet Ravuri
 * 
 */
public class SchemaConstants {

	/**
	 * Represents the schema whose elements are used as keys by the crawler
	 * 
	 */
	public static enum Schema {
		professor, course, department, program, others
	}

	// Synonyms for professor entity used by crawler
	public static String PROFESSOR_ENTITY_SYNONYM_PROFESSOR = "professor";
	public static String PROFESSOR_ENTITY_SYNONYM_TEACHER = "teacher";
	public static String PROFESSOR_ENTITY_SYNONYM_FACULTY = "faculty";

	// Synonyms for course entity used by crawler
	public static String COURSE_ENTITY_SYNONYM_COURSE = "course";

}
