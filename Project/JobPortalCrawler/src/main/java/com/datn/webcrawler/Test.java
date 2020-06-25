package com.datn.webcrawler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect("https://careerbuilder.vn/vi/tim-viec-lam/nhan-vien-viet-content-marketing.35B439BC.html").get();
		Element description = doc.select("#tab-1 > section > div:nth-child(3) > h3").first();
		System.out.println(description.nextElementSiblings().text());
		Elements details = description.nextElementSiblings();
		List<String> desc = new ArrayList<String>();
		for (Element detail: details) {
			desc.add(detail.text());
		}
		String job = "";

		for (String de: desc) {
			System.out.println(de);
			job.concat(de);
		}
		System.out.println("--------------------------");

		System.out.println(job);
		
	}
}
