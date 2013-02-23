package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.schema;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * Represents a professor in a .edu domain
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class Professor {

	private String name;
	private String phone;
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Vertex addToGraph(Graph g) {
		Vertex v = g.addVertex(null);
		v.setProperty("name", this.name);
		v.setProperty("phone", this.phone);
		v.setProperty("email", this.email);

		return v;
	}
}
