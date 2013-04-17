package edu.cmu.cs.lti.oaqa.graphqa.db.schema.edu;

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
	private String phone2;
	private String office;
	private String office2;
	private String page;
	private String jobTitle;
	private String email;
	private String type;

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

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void addToGraph(String name, Graph g) { // add sth and modify sth
		Vertex v = g.addVertex(name);
		v.setProperty("name", this.name);
		v.setProperty("phone", this.phone);
		v.setProperty("office", this.office);
		v.setProperty("page", this.page);
		v.setProperty("jobTitle", this.jobTitle);
		v.setProperty("email", this.email);
		v.setProperty("type", "person");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOffice2() {
		return office2;
	}

	public void setOffice2(String office2) {
		this.office2 = office2;
	}

}
