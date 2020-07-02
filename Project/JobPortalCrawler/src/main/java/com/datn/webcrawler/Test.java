package com.datn.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("https://careerbuilder.vn/viec-lam/tiep-thi-truc-tuyen-c37-vi.html").get();
		Element description = doc.select("body > main > section.search-result-list > div > div > div.col-lg-8.col-custom-xxl-9 > div.main-slide > div > div > div > div.jobs-side-list").first();
	
		Elements cats = description.children();
		System.out.println(cats);
	}
}
