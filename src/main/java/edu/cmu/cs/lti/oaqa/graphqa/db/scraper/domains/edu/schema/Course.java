package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.schema;

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
	private String timing;
	private String location;

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

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Vertex addToGraph(Graph g) {
		Vertex v = g.addVertex(null);
		v.setProperty("name", this.name);
		v.setProperty("code", this.code);
		v.setProperty("timing", this.timing);
		v.setProperty("location", this.location);

		return v;
	}

}
