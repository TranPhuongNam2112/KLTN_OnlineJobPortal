package com.datn.webcrawler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("https://www.timviecnhanh.com/tuyen-nv-kinh-doanh-quan-thu-duc-quan-tan-binh-ho-chi-minh-4479259.html").get();
		String desc;
		System.out.println(doc.select("#left-content > article > table > tbody > tr:nth-child(1) > td:nth-child(2) > p").first().text());
		
		
	}
	
}
