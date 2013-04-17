package edu.cmu.cs.lti.oaqa.graphqa.db.scraper.domains.edu.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.tinkerpop.blueprints.Graph;

import edu.cmu.cs.lti.oaqa.graphqa.db.schema.edu.Professor;

public class ScrapeHelper {
	public static Set<String> scrapeData(Graph g, String person, String url) {
		Set<String> pLinks = new HashSet<String>();

		// Extract prof nodes into graph
		if (url.length() > 120)
			return null;

		System.out.println("currently operating --> " + url);

		Document doc;// need to decide if the content is in the page

		try {
			Connection c = Jsoup.connect(url);
			c.timeout(10000);
			doc = c.get();
		} catch (Exception e) {
			System.out.println("Could not read URL: " + url);
			System.out.println(e.getMessage());
			return null;
		}

		Professor prof = new Professor();
		String def = "n/a";
		prof.setEmail(def);
		prof.setJobTitle(def);
		prof.setName(def);
		prof.setOffice(def);
		prof.setPage(def);
		prof.setPhone(def);
		prof.setPhone2(def);
		// ===============================================
		String email = NewEmail.getEmail(doc, url, person);
		prof.setEmail(email);

		// ===============================================

		String name = NewName.getName(doc, url, person);
		prof.setName(name);

		// ===============================================
		String page = "";
		if (url.contains("~")) {
			page = url;
		} else {
			Elements links = doc.getElementsByAttribute("href");
			for (int j = 0; j < links.size(); j++) {
				String tmpLink = links.get(j).attr("href");
				if (tmpLink.contains(".com"))
					continue; // other domain
				if (!tmpLink.contains("~"))
					continue;
				pLinks.add(tmpLink);
				System.out.println(tmpLink);
				System.out.println("========================");
			}
			System.out.println(pLinks.size());
		}

		// ===============================================

		Set<String> phones = new HashSet<String>();
		phones = NewPhones.getPhones(doc, url);
		if (phones != null) {
			// System.out.println(phones);
			// if (phones.size()>0){
			// countTotal++;
			// }

			Iterator<String> ite = phones.iterator();
			int tmp = 0;
			while (ite.hasNext() && tmp < 2) {
				if (tmp == 0)
					prof.setPhone(ite.next());
				else
					prof.setPhone2(ite.next());
				tmp++;
				// System.out.println(ite.next());
			}
		}
		// ===============================================

		String titleStr = NewTitle.getTitle(doc);
		if (titleStr != null) {
			// countTotal++;
			prof.setJobTitle(titleStr);
		}

		Set<String> officeSet = new HashSet<String>();
		officeSet = NewOffice.getOffice(doc, url);
		if (officeSet != null) {
			// System.out.println(officeSet);
			if (officeSet.size() > 0) {
			}

			Iterator<String> ite = officeSet.iterator();
			int tmp = 0;
			while (ite.hasNext() && tmp < 2) {
				if (tmp == 0)
					prof.setOffice(ite.next());
				else
					prof.setOffice2(ite.next());
				tmp++;
				// System.out.println(ite.next());
			}
		}

		prof.addToGraph(name, g);
		System.out.println("done");
		return pLinks;

	}

}
