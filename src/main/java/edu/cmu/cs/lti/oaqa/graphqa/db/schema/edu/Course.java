package edu.cmu.cs.lti.oaqa.graphqa.db.schema.edu;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * Represents a course in .edu domain
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class Course {

	private String name;
	private String code;
	private String start_time;
	private String end_time;
	private String location;
	private String professor;

	public static String COURSE_NODE_TYPE = "course";
	public static String COURSE_NAME = "course_name";
	public static String COURSE_CODE = "course_code";
	public static String COURSE_START_TIME = "course_start_time";
	public static String COURSE_END_TIME = "course_end_time";
	public static String COURSE_LOCATION = "course_location";
	public static String COURSE_INSTRUCTOR = "course_instructor";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public void addToGraph(Vertex v) {
		v.setProperty("type", COURSE_NODE_TYPE);
		v.setProperty(COURSE_CODE, this.getCode());
		if (this.getName() != null)
			v.setProperty(COURSE_NAME, this.getName());
		if (this.getStart_time() != null)
			v.setProperty(COURSE_START_TIME, this.getStart_time());
		if (this.getEnd_time() != null)
			v.setProperty(COURSE_END_TIME, this.getEnd_time());
		if (this.getLocation() != null)
			v.setProperty(COURSE_LOCATION, this.getLocation());
		if (this.getProfessor() != null) {
			v.setProperty(COURSE_INSTRUCTOR, this.getProfessor());
		}
	}
}
