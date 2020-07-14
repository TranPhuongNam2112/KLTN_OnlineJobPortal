package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("https://www.careerlink.vn/").get();
		Elements description = doc.select("body > div.top-page > div.jumbotron.jumbotron-background-cover > div.container > div.overlay > div > div").first().children();

		
	}
}
