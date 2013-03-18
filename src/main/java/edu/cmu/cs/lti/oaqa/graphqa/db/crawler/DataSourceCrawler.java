package edu.cmu.cs.lti.oaqa.graphqa.db.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.cmu.cs.lti.oaqa.graphqa.db.schema.Schema;
import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.utils.DataSourceCrawlerUtils;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;
import edu.cmu.cs.lti.oaqa.graphqa.db.schema.SchemaUtils;

/**
 * Crawls the web site located at the input URL and returns the list of URLs
 * present in that domain
 * 
 * @author Puneet Ravuri
 * 
 */
public class DataSourceCrawler {

	private Map<Schema, Set<String>> urls;
	private Set<String> crawledURLs;

	public DataSourceCrawler() {
		urls = new HashMap<Schema, Set<String>>();
		SchemaUtils.populateCrawlerMap(urls);
		crawledURLs = new HashSet<String>();
	}

	/**
	 * Performs web crawling on the provided url and returns a map of schema
	 * element and their corresponding URLs
	 * 
	 * @param url
	 *            Input URL
	 * @return Map of URLs in the domain
	 * @throws GraphBuilderException
	 *             Thrown when there is any error during crawling
	 */
	public Map<Schema, Set<String>> getLinks(String url)
			throws GraphBuilderException {

		try {

			if (url == null || url.length() == 0)
				return urls;

			if (!url.startsWith("http://"))
				return urls;

			addLinksToCollection(DataSourceCrawlerUtils.getDomainName(url), url);
		} catch (Exception e) {
			throw new GraphBuilderException(e);
		}
		return urls;
	}

	/**
	 * 
	 * Performs web crawling on the provided url and stores a map of schema
	 * element and their corresponding URLs in the file provided
	 * 
	 * @param url
	 *            Input URL that will be crawled
	 * @param fileName
	 *            Output file where the map contents will be written
	 * @throws GraphBuilderException
	 *             Thrown when there is any error during crawling
	 */
	public void getLinks(String url, String fileName)
			throws GraphBuilderException {

		getLinks(url);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

			for (Schema s : urls.keySet()) {
				Set<String> urlList = urls.get(s);
				for (String str : urlList) {
					bw.write(s + " " + str + "\n");
				}
			}

			bw.close();
		} catch (IOException e) {
			throw new GraphBuilderException(e);
		}

	}

	private void addLinksToCollection(String domainURL, String url) {

		if (crawledURLs.contains(url))
			return;
		else
			crawledURLs.add(url);

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			return;
		}

		Elements elems = doc.getElementsByTag("a");

		for (int i = 0; i < elems.size(); i++) {
			Element elem = elems.get(i);
			String attr = elem.attr("href");
			String nextLink = new String();

			if (attr == null || attr.length() == 0)
				continue;
			else
				attr = attr.trim();

			if (attr.startsWith("http")) {

				String attrDomainName = DataSourceCrawlerUtils
						.getDomainName(attr);

				if (attrDomainName == null)
					continue;

				if (attrDomainName.contains(domainURL)) {
					nextLink = attr;
				} else
					continue;
			} else if (attr.startsWith("#")) {
				continue;
			} else {
				if (attr.charAt(0) == '/') {
					String rootURL = DataSourceCrawlerUtils.getRootURL(url);
					if (rootURL == null)
						continue;

					nextLink = rootURL + attr;
				} else if (attr.startsWith("../")) {
					String parentURL = DataSourceCrawlerUtils.getParentURL(url);
					if (parentURL == null)
						continue;

					parentURL = DataSourceCrawlerUtils.getParentURL(parentURL);
					nextLink = parentURL + "/"
							+ attr.subSequence(3, attr.length() - 1);

				} else {
					String parentURL = DataSourceCrawlerUtils.getParentURL(url);
					if (parentURL == null)
						continue;

					nextLink = parentURL + "/" + attr;
				}
			}

			if (nextLink.indexOf('?') != -1)
				continue;

			if (!DataSourceCrawlerUtils.addURLToValidSchemaSet(urls, nextLink)) {
				System.out.println("Crawling link: " + nextLink);
				addLinksToCollection(domainURL, nextLink);
			}
		}
	}

}
