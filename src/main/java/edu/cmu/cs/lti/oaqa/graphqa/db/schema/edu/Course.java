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

	public Vertex addToGraph(Graph g) {
		Vertex v = g.addVertex(null);
		v.setProperty("name", this.name);
		v.setProperty("code", this.code);
		v.setProperty("start_time", this.start_time);
		v.setProperty("end_time", this.end_time);
		v.setProperty("location", this.location);
		v.setProperty("professor", this.professor);

		return v;
	}

}
