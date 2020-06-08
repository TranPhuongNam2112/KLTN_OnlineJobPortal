package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.datn.webcrawler.CrawlerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class JobPortalCrawlerApplication {
	static int findLastIndex(String str, Character x) 
	{ 
		int index = -1; 
		for (int i = 0; i < str.length(); i++) 
			if (str.charAt(i) == x) 
				index = i; 
		return index; 
	} 
	public static String insertString( 
			String originalString, 
			String stringToBeInserted, 
			int index) 
	{ 

		String newString = new String(); 

		for (int i = 0; i < originalString.length(); i++) { 
			newString += originalString.charAt(i); 
			if (i == index) { 
				newString += stringToBeInserted; 
			} 
		} 
		return newString; 
	} 
	private static final Logger logger = LoggerFactory.getLogger(JobPortalCrawlerApplication.class);

	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = "/tmp/crawler4j/";

		CrawlConfig config1 = new CrawlConfig();
		CrawlConfig config2 = new CrawlConfig();

		config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
		config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

		config1.setPolitenessDelay(1000);
		config2.setPolitenessDelay(2000);

		config1.setMaxPagesToFetch(50);
		config2.setMaxPagesToFetch(100);

		PageFetcher pageFetcher1 = new PageFetcher(config1);
		PageFetcher pageFetcher2 = new PageFetcher(config2);

		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

		CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
		CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);


		Document timvn = Jsoup.connect("https://www.timviecnhanh.com/viec-lam/nganh-nghe").get();
		Elements categories = timvn.select("#province-content");

		for (Element category:categories) {
			Elements joblinks = category.select("li > a");
			for (Element joblink: joblinks) {
				controller1.addSeed(joblink.attr("href"));
				if (Jsoup.connect(joblink.attr("href")+"?page="+2).get().select("#job_fields_list > div > div > div.list-job-field-has-thumbnail.col-xs-6.offset20 > div > a.bold.title-link-visited > span").hasText()) {
					int i = 2;
					do {
						controller1.addSeed(joblink.attr("href")+"?page="+i);
						i++;
					} while (Jsoup.connect(joblink.attr("href")+"?page="+i).get().select("#job_fields_list").size() !=0);
				}
			}
		}

		Document doc = Jsoup.connect("https://careerbuilder.vn/tim-viec-lam.html").get();
		Element jobcategories = doc.select("body > main > section.find-jobsby-categories.cb-section > div > div > div.col-xl-9 > div.row.list-of-working-positions").first();
		Elements cats = jobcategories.children();
		for (Element cat: cats) {
			Elements types = cat.child(1).select("li").select("a");
			for (Element type: types) {
				controller2.addSeed(type.attr("href"));
				if (Jsoup.connect(insertString(type.attr("href"), "-trang-"+2, findLastIndex(type.attr("href"), '-') -1)).get().select("body > main > section.search-result-list > div > div.row > div.col-lg-8.col-xl-9 > section").hasText()) {
					int i =2;
					do {
						controller2.addSeed(insertString(type.attr("href"), "-trang-"+i, findLastIndex(type.attr("href"), '-') -1));
						i++;
					} while (Jsoup.connect(insertString(type.attr("href"), "-trang-"+i, findLastIndex(type.attr("href"), '-') -1)).get().select("body > main > section.search-result-list > div > div.row > div.col-lg-8.col-xl-9 > section").size() !=0);
				}

			}			
		}
		

		
		ComboPooledDataSource pool = new ComboPooledDataSource();
		pool.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
		pool.setUser("root");
		pool.setPassword("1234");
		pool.setMaxPoolSize(5);
		pool.setMinPoolSize(5);
		pool.setInitialPoolSize(5);

		ComboPooledDataSource pool1 = new ComboPooledDataSource();
		pool1.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool1.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
		pool1.setUser("root");
		pool1.setPassword("1234");
		pool1.setMaxPoolSize(7);
		pool1.setMinPoolSize(7);
		pool1.setInitialPoolSize(7);

		controller1.startNonBlocking(new CrawlerFactory(pool1), 5);
		controller2.startNonBlocking(new CrawlerFactory(pool1), 7);

		controller1.waitUntilFinish();
		logger.info("Crawler 1 is finished.");

		controller2.waitUntilFinish();
		logger.info("Crawler 2 is finished.");
	}
}
