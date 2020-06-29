package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test {
	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("https://www.topcv.vn/viec-lam/phu-ta-nha-khoa/77751.html").get();
		Element description = doc.select("#col-job-left > div:nth-child(2)").first();
	
		System.out.println(description.html());
	}
}
