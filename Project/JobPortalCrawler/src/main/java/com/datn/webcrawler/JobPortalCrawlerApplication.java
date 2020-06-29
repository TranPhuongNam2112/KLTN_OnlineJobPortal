package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


@SpringBootApplication
@EnableScheduling
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
		

		SpringApplication.run(JobPortalCrawlerApplication.class, args);
	}
}
