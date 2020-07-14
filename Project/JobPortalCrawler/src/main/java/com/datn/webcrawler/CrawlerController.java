package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Component
public class CrawlerController {
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
	private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);


	@Scheduled(cron = "0 37 20 * * ?")
	public static void CrawlScheduler() throws Exception {
		System.out.println("Start crawling");
		String crawlStorageFolder = "/tmp/crawler4j/";
		int MAX_PAGES_TO_SEARCH = 150;
		CrawlConfig config1 = new CrawlConfig();
		CrawlConfig config2 = new CrawlConfig();
		CrawlConfig config3 = new CrawlConfig();
		CrawlConfig config4 = new CrawlConfig();

		config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
		config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");
		config3.setCrawlStorageFolder(crawlStorageFolder + "/crawler3");
		config4.setCrawlStorageFolder(crawlStorageFolder + "/crawler4");

		config1.setPolitenessDelay(1000);
		config2.setPolitenessDelay(2000);
		config3.setPolitenessDelay(3000);
		config4.setPolitenessDelay(4000);

		config1.setMaxPagesToFetch(50);
		config2.setMaxPagesToFetch(100);
		config3.setMaxPagesToFetch(MAX_PAGES_TO_SEARCH);
		config4.setMaxPagesToFetch(MAX_PAGES_TO_SEARCH);


		PageFetcher pageFetcher1 = new PageFetcher(config1);
		PageFetcher pageFetcher2 = new PageFetcher(config2);
		PageFetcher pageFetcher3 = new PageFetcher(config3);
		PageFetcher pageFetcher4 = new PageFetcher(config4);

		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

		CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
		CrawlController controller2 = new CrawlController(config2, pageFetcher2, robotstxtServer);
		CrawlController controller3 = new CrawlController(config3, pageFetcher3, robotstxtServer);
		CrawlController controller4 = new CrawlController(config4, pageFetcher4, robotstxtServer);

//		for (int i = 1; i <= MAX_PAGES_TO_SEARCH; i++) {
//			controller3.addSeed("https://jobsgo.vn/viec-lam-trang-" + i + ".html");
//		}
//		
//		for (int j = 1; j <= MAX_PAGES_TO_SEARCH; j++) {
//			controller4.addSeed("https://timviec365.vn/tin-tuyen-dung-viec-lam.html?page="+j);
//		}

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
		

//		
//		Document doc = Jsoup.connect("https://careerbuilder.vn/tim-viec-lam.html").get();
//		Element jobcategories = doc.select("body > main > section.find-jobsby-categories.cb-section > div > div > div.col-xl-9 > div.row.list-of-working-positions").first();
//		Elements cats = jobcategories.children();
//		
//		for (Element cat: cats) {
//			Elements types = cat.child(1).select("li").select("a");
//			for (Element type: types) {
//				controller2.addSeed(type.attr("href"));
//				if (Jsoup.connect(insertString(type.attr("href"), "-trang-"+2, findLastIndex(type.attr("href"), '-') -1)).get().select("body > main > section.search-result-list > div > div.row > div.col-lg-8.col-xl-9 > section").hasText()) {
//					int i =2;
//					do {
//						controller2.addSeed(insertString(type.attr("href"), "-trang-"+i, findLastIndex(type.attr("href"), '-') -1));
//						i++;
//					} while (Jsoup.connect(insertString(type.attr("href"), "-trang-"+i, findLastIndex(type.attr("href"), '-') -1)).get().select("body > main > section.search-result-list > div > div.row > div.col-lg-8.col-xl-9 > section").size() !=0);
//				}
//
//			}			
//		}
//		
		ComboPooledDataSource pool = new ComboPooledDataSource();
		pool.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8");
		pool.setUser("root");
		pool.setPassword("123456789");
		pool.setMaxPoolSize(10);
		pool.setMinPoolSize(10);
		pool.setInitialPoolSize(10);
		
		ComboPooledDataSource pool1 = new ComboPooledDataSource();
		pool1.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool1.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8");
		pool1.setUser("root");
		pool1.setPassword("1234");
		pool1.setMaxPoolSize(10);
		pool1.setMinPoolSize(10);
		pool1.setInitialPoolSize(10);
		
		ComboPooledDataSource pool3 = new ComboPooledDataSource();
		pool3.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool3.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8");
		pool3.setUser("root");
		pool3.setPassword("1234");
		pool3.setMaxPoolSize(10);
		pool3.setMinPoolSize(10);
		pool3.setInitialPoolSize(10);
		
		ComboPooledDataSource pool4 = new ComboPooledDataSource();
		pool4.setDriverClass("com.mysql.cj.jdbc.Driver");
		pool4.setJdbcUrl("jdbc:mysql://localhost:3306/jobportal?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8");
		pool4.setUser("root");
		pool4.setPassword("1234");
		pool4.setMaxPoolSize(10);
		pool4.setMinPoolSize(10);
		pool4.setInitialPoolSize(10);

		controller1.startNonBlocking(new CrawlerFactory(pool), 7);
		//controller2.startNonBlocking(new CrawlerFactory(pool1), 7);
		//controller3.startNonBlocking(new CrawlerFactory(pool3), 7);
		//controller4.startNonBlocking(new CrawlerFactory(pool4), 7);

		pool.close();
		System.out.println("Crawler finished");
	}
	
	
}
