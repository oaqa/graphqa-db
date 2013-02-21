package edu.cmu.cs.lti.oaqa.graphqa.db.crawler;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.cmu.cs.lti.oaqa.graphqa.db.crawler.utils.DataSourceCrawlerUtils;
import edu.cmu.cs.lti.oaqa.graphqa.db.exception.GraphBuilderException;

/**
 * Crawls the web site located at the input URL and returns the list of URLs
 * present in that domain
 * 
 * @author Puneet Ravuri
 * 
 */
public class DataSourceCrawler {

	/**
	 * Returns the list of URLs present in the input URL domain by crawling
	 * 
	 * @param url
	 *            Input URL
	 * @return Set of URLs in the domain
	 * @throws GraphBuilderException
	 *             Thrown when there is any error during crawling
	 */
	public Set<String> getLinks(String url) throws GraphBuilderException {
		Set<String> links = new HashSet<String>();

		try {

			if (url == null || url.length() == 0)
				return links;

			if (!url.startsWith("http://"))
				return links;

			addLinksToCollection(links,
					DataSourceCrawlerUtils.getDomainName(url), url);
		} catch (Exception e) {
			throw new GraphBuilderException(e);
		}
		return links;
	}

	private void addLinksToCollection(Set<String> links, String domainURL,
			String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			// System.out.println("Ignoring IOException while connecting to "
			// + url + ": " + e.getMessage());
			System.out.println("Removing new link: " + url);
			links.remove(url);
			return;
		}

		Elements elems = doc.getElementsByTag("a");

		for (int i = 0; i < elems.size(); i++) {
			Element elem = elems.get(i);
			String attr = elem.attr("href");
			String nextLink = new String();

			// System.out.println("In URL " + url + ", href found: " + attr);

			if (attr == null || attr.length() == 0)
				continue;

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

			if (!links.contains(nextLink)) {
				links.add(nextLink);
				System.out.println("Adding new link: " + nextLink);
				addLinksToCollection(links, domainURL, nextLink);
			}
		}
	}

}
