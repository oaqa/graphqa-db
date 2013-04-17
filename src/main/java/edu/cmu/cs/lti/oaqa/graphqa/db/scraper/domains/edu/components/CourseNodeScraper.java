package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.utils.DataSourceCrawlerUtils;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;
import edu.cmu.cs.lti.oaqa.graphqa.db.schema.edu.Course;

/**
 * Represents a scraper module for courses
 * 
 * @author Puneet Ravuri
 * 
 */
public class CourseNodeScraper {

	private List<Course> courses;

	/**
	 * Initializes the course node scraper
	 */
	public CourseNodeScraper() {
		courses = new ArrayList<Course>();
	}

	public void scrapeData(Graph g, Set<String> urls)
			throws GraphBuilderException {

		// For each webpage in the set, perform the same operation to identify
		// courses
		for (String url : urls) {

			System.out.println("URL being read: " + url);

			// If the webpage stores course information in a table, then
			// retrieve the content
			int courses = extractCoursesFromTables(url);

			// If no course was retrieved, then search for links on this page
			// that lead to course related pages. Then extract courses from
			// those pages
			if (courses == 0) {
				List<String> courseLinks = extractCourseLinksFromWebpage(url);

				for (int i = 0; i < courseLinks.size(); i++)
					extractCoursesFromTables(courseLinks.get(i));
			}

		} // For each url in the set

		System.out.println("Total courses extracted: " + courses.size());
		for (int i = 0; i < courses.size(); i++) {
			System.out.println("Code: " + courses.get(i).getCode() + ", Name: "
					+ courses.get(i).getName() + ", Prof: "
					+ courses.get(i).getProfessor() + ", Location: "
					+ courses.get(i).getLocation() + ", Start_Time: "
					+ courses.get(i).getStart_time() + ", End_Time: "
					+ courses.get(i).getEnd_time());
		}

		addCoursesToGraph(g);
	}

	/**
	 * This method will try to extract courses information from the web page
	 * assuming that the courses will be stored in a table format. Firstly, the
	 * course number will be identified in every row of the table using regular
	 * expressions. If considerable number of rows have course number, then the
	 * column index of the table corresponding to each course related
	 * information will be retrieved and finally the course details will be
	 * retrieved.
	 * 
	 * @param url
	 *            Webpage URL
	 * @return Number of courses retrieved
	 * @throws GraphBuilderException
	 *             Thrown when any issue during scraping
	 */
	private int extractCoursesFromTables(String url)
			throws GraphBuilderException {
		Document doc = null;
		int coursesRetrieved = 0;
		try {
			Connection c = Jsoup.connect(url);
			c.timeout(10000);
			doc = c.get();
		} catch (Exception e) {
			System.out.println("Could not read URL: " + url);
			System.out.println(e.getMessage());
			return 0;
			// throw new GraphBuilderException(e);
		}

		Elements tables = doc.getElementsByTag("table");

		// For every table in the webpage, check if the table contains
		// courses related information
		for (int j = 0; j < tables.size(); j++) {

			Elements rows = tables.get(j).getElementsByTag("tr");
			if (rows.size() <= 1)
				continue;

			boolean containsCourseCode = false;
			float count = 0;

			// For each row in the table
			for (int i = 0; i < rows.size(); i++) {
				Element row = rows.get(i);
				if (doesRowHaveCourseNumber(row))
					count++;
			}

			// If more than 50% of the rows in the table have course number
			// (identified by its regex), it will be assumed that the table
			// contains courses related information
			if (rows.size() > 1 && (count / rows.size()) > 0.5)
				containsCourseCode = true;

			// If the table doesnt have courses related information, ignore
			// and go to next table
			if (!containsCourseCode) {
				continue;
			}

			System.out.println("Table identified in URL: " + url);

			// Identify the indexes of columns in the table that contain
			// course specific information
			int codeColumnIndex = -1;
			int nameColumnIndex = -1;
			int profColumnIndex = -1;
			int startColumnIndex = -1;
			int endColumnIndex = -1;
			int roomColumnIndex = -1;
			int headColSize = 0;

			// Iterate from the first row to find the specific row that
			// describes its contents. When that row is identified, find
			// which column contains which specific information related to a
			// course
			for (int k = 0; k < rows.size() && codeColumnIndex == -1; k++) {
				Element head = rows.get(k);

				Elements headColumns = head.getElementsByTag("th");
				if (headColumns.size() == 0)
					headColumns = head.getElementsByTag("td");

				headColSize = headColumns.size();

				for (int i = 0; i < headColumns.size(); i++) {
					Element column = headColumns.get(i);
					String content = column.text().trim().toLowerCase();
					if (codeColumnIndex == -1 && content.contains("course")) {
						codeColumnIndex = i;
						continue;
					}

					if (codeColumnIndex != -1 && nameColumnIndex == -1
							&& content.contains("title")) {
						nameColumnIndex = i;
						continue;
					}

					if (codeColumnIndex != -1
							&& profColumnIndex == -1
							&& (content.contains("instructor")
									|| content.contains("teacher")
									|| content.contains("faculty") || content
										.contains("professor"))) {
						profColumnIndex = i;
						continue;
					}

					if (codeColumnIndex != -1
							&& startColumnIndex == -1
							&& (content.contains(" start") || content
									.contains(" begin"))) {
						startColumnIndex = i;
						continue;
					}

					if (codeColumnIndex != -1
							&& endColumnIndex == -1
							&& (content.contains(" finish") || content
									.contains(" end"))) {
						System.out.println(content);
						endColumnIndex = i;
						continue;
					}

					if (codeColumnIndex != -1
							&& roomColumnIndex == -1
							&& (content.contains(" room")
									|| content.contains("building") || content
										.contains("location"))) {
						roomColumnIndex = i;
						continue;
					}
				}
			}

			// If the course number column was not identified, then extraction
			// cannot be done. So, the table is ignored and continued.
			if (codeColumnIndex == -1)
				continue;

			// Once the column indexes are identified, collect course
			// information from the table
			for (int i = 0; i < rows.size(); i++) {
				Element row = rows.get(i);
				Elements columns = row.getElementsByTag("td");

				if (columns.size() == headColSize) {
					Course c = new Course();
					if (codeColumnIndex != -1)
						c.setCode(columns.get(codeColumnIndex).text().trim());

					if (nameColumnIndex != -1)
						c.setName(columns.get(nameColumnIndex).text().trim());

					if (profColumnIndex != -1)
						c.setProfessor(columns.get(profColumnIndex).text()
								.trim());

					if (startColumnIndex != -1)
						c.setStart_time(columns.get(startColumnIndex).text()
								.trim());

					if (endColumnIndex != -1)
						c.setEnd_time(columns.get(endColumnIndex).text().trim());

					if (roomColumnIndex != -1)
						c.setLocation(columns.get(roomColumnIndex).text()
								.trim());

					if (c.getCode().matches("[\\d-]+")) {
						courses.add(c);
						coursesRetrieved++;
					}
				}
			}
		} // For each table in the webpage

		return coursesRetrieved;
	}

	private List<String> extractCourseLinksFromWebpage(String url)
			throws GraphBuilderException {

		List<String> courseLinks = new ArrayList<String>();

		Document doc = null;
		try {
			Connection c = Jsoup.connect(url);
			c.timeout(10000);
			doc = c.get();
		} catch (Exception e) {
			System.out.println("Could not read URL: " + url);
			System.out.println(e.getMessage());
			return courseLinks;
			// throw new GraphBuilderException(e);
		}

		Elements links = doc.getElementsByTag("a");

		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);

			// The link should have the string "course" in its text
			// if (!link.text().toLowerCase().contains("course"))
			// continue;

			String href = link.attr("href");
			if (href == null || href.length() == 0)
				continue;
			else
				href = href.trim();

			if (href.startsWith("http")) {
				courseLinks.add(href);
			} else if (href.startsWith("#")) {
				continue;
			} else {
				if (href.charAt(0) == '/') {
					String rootURL = DataSourceCrawlerUtils.getRootURL(url);
					if (rootURL == null)
						continue;
					courseLinks.add(rootURL + href);
				} else if (href.startsWith("../")) {
					String parentURL = DataSourceCrawlerUtils.getParentURL(url);
					if (parentURL == null)
						continue;
					parentURL = DataSourceCrawlerUtils.getParentURL(parentURL);
					courseLinks.add(parentURL + "/"
							+ href.subSequence(3, href.length() - 1));

				} else {
					String parentURL = DataSourceCrawlerUtils.getParentURL(url);
					if (parentURL == null)
						continue;
					courseLinks.add(parentURL + "/" + href);
				}
			}
		}

		return courseLinks;
	}

	/**
	 * Uses regex to find whether a table row contains a course number
	 * 
	 * @param row
	 *            Table row element
	 * @return true If any column in the row contains course number, false
	 *         otherwise
	 */
	private boolean doesRowHaveCourseNumber(Element row) {

		Elements columns = row.getElementsByTag("td");

		boolean courseCodePresent = false;
		for (int i = 0; i < columns.size(); i++) {
			Element column = columns.get(i);
			String content = column.text().trim();

			if (content.matches("[\\d-]+")) {
				courseCodePresent = true;
				break;
			}
		}

		return courseCodePresent;
	}

	private void addCoursesToGraph(Graph g) {

		for (int i = 0; i < courses.size(); i++) {
			Course c = courses.get(i);

			Vertex v = getCourseNode(g, c.getCode());
			if (v == null)
				v = g.addVertex(null);
			System.out
					.println("Adding course " + c.getName() + " to the graph");

			c.addToGraph(v);

			if (c.getProfessor() != null) {

				Vertex profNode = getCourseInstructorNode(g, c.getProfessor());

				if (profNode == null)
					continue;

				System.out.println("Adding edge for course " + c.getName()
						+ " and prof " + c.getProfessor());
				g.addEdge(null, profNode, v, "teach");
			}
		}

	}

	private Vertex getCourseNode(Graph g, String courseCode) {

		for (Vertex v : g.getVertices("type", Course.COURSE_NODE_TYPE)) {
			if (((String) v.getProperty(Course.COURSE_CODE))
					.equalsIgnoreCase(courseCode))
				return v;
		}
		return null;
	}

	private Vertex getCourseInstructorNode(Graph g, String profName) {
		System.out.println(profName);
		for (Vertex v : g.getVertices()) {
			String profNodeName = (String) v.getProperty("name");
			System.out.println(profNodeName);
			if (profNodeName != null
					&& (profNodeName.contains(profName) || profName
							.contains(profNodeName)))
				return v;
		}

		return null;
	}
}
