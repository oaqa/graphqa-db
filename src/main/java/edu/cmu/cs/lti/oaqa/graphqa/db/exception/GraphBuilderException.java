package edu.cmu.cs.lti.oaqa.graphqa.db.exception;

/**
 * Exception class for graph builder module
 * 
 * @authors Puneet Ravuri, Wenyi Wang
 * 
 */
public class GraphBuilderException extends Exception {

	private static final long serialVersionUID = -8701721354852131493L;

	public GraphBuilderException() {
		super();
	}

	public GraphBuilderException(Exception e) {
		super(e);
	}

	public GraphBuilderException(String msg) {
		super(msg);
	}

}
