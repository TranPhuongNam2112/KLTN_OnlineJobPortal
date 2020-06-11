package com.datn.webcrawler;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.datn.webcrawler.service.DBService;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyWebCrawler extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(MyWebCrawler.class);

	private static final Pattern FILTERS = Pattern.compile(
			".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
					"|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private final DBService dBService;

	public MyWebCrawler(DBService dBService) {
		this.dBService = dBService;
	}


	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		if (href.startsWith("https://www.timviecnhanh.com/") || href.startsWith("https://careerbuilder.vn/")) {
			return true;
		}

		return false;
	}

	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		int parentDocid = page.getWebURL().getParentDocid();

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Docid of parent page: {}", parentDocid);
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			if (page.getWebURL().toString().contains("https://careerbuilder.vn/")) {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				Element jobcategories = doc.select("body > main > section.search-result-list > div > div.row > div.col-lg-8.col-xl-9 > section").first();
				Elements cats = jobcategories.children();
				for (Element cat: cats) {
					if (cat.hasAttr("id")) {
						List<String> industry = new ArrayList<String>();
						String jobtype = null; 
						long years = 0;
						String sourceUrl = cat.child(0).child(1).child(0).select("a").attr("href");
						String jobtitle = cat.child(0).child(1).child(0).select("a").attr("title");
						try {
							Document post = Jsoup.connect(sourceUrl).get();
							Elements industries = post.select("#tab-1 > section > div.row > div:nth-child(1) > div > div.industry");
							if (industries.size() == 0) {
								industries = post.select("#info-career-desktop > ul > li:nth-child(5) > div > a");
								Elements industrynames = industries;
								jobtype = post.select("#info-career-desktop > ul > li:nth-child(2) > div").text();

								String experiencetype = post.select("#info-career-desktop > ul > li:nth-child(6) > div").text();
								System.out.println(experiencetype);
								if (experiencetype.contains("Không yêu cầu kinh nghiệm") || experiencetype.contains("Chưa"))
								{
									years = 0;
								}
								else {
									String[] strArray = experiencetype.split("\\s+");
									for (String str: strArray) {
										System.out.println(str);
									}
									if (strArray[0].contains("-")) {
										String[] yearArray = strArray[0].split("-");
										years = Long.parseLong(yearArray[1]);
									}
									else if (strArray[0].contains("Trên") || strArray[0].contains("Dưới"))
									{
										years = Long.parseLong(strArray[1]);
									}
									else {
										years = Long.parseLong(strArray[0]);
									}
								}								
								for (Element industryname: industrynames) {
									industry.add(industryname.text());
								}
							}
							else {
								Elements industrynames = industries.select("p").first().children();
								jobtype = post.select("#tab-1 > section > div.row > div:nth-child(2) > div > ul > li:nth-child(1) > p").first().text();

								for (Element industryname: industrynames) {
									industry.add(industryname.text());
								}
							}

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Long minSalary = null;
						Long maxSalary = null;
						List<String> salary = new ArrayList<String>();
						if (!cat.child(0).child(1).child(1).select("p.salary").text().contains("Cạnh tranh") && !cat.child(0).child(1).child(1).select("p.salary").text().contains("Dưới") && !cat.child(0).child(1).child(1).select("p.salary").text().contains("Trên")) {
							String line = cat.child(0).child(1).child(1).select("p.salary").text();
							String[] strArray = line.split(" ", 7);
							for (String string : strArray) {
								if (!string.equals("")) {
									System.out.println(string);
									salary.add(string);
								}
							}
							if (salary.get(1).contains(",") && !salary.get(4).contains(","))
							{

								try {
									minSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(1)).longValue())*1000000;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								maxSalary = Long.valueOf(salary.get(4))*1000000;
							}
							else if (!salary.get(1).contains(",") && salary.get(4).contains(",")) {
								try {
									maxSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(4)).longValue())*1000000;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								minSalary = Long.valueOf(salary.get(1))*1000000;				
							} else {
								try {
									minSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(1)).longValue())*1000000;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									maxSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(4)).longValue())*1000000;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else if (cat.child(0).child(1).child(1).select("p.salary").text().contains("Dưới")) {
							List<String> numbers= new ArrayList<String>();
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(cat.child(0).child(1).child(1).select("p.salary").text());
							while(m.find()) {
								numbers.add(m.group());
							}
							maxSalary = Long.valueOf(numbers.get(0))*1000000;
							minSalary = Long.valueOf(0);
						}
						else if(cat.child(0).child(1).child(1).select("p.salary").text().contains("Trên")) {
							List<String> numbers= new ArrayList<String>();
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(cat.child(0).child(1).child(1).select("p.salary").text());
							while(m.find()) {
								numbers.add(m.group());
							}
							minSalary = Long.valueOf(numbers.get(0))*1000000;
							maxSalary = Long.valueOf(0);
						} else
						{
							minSalary = Long.valueOf(0);
							maxSalary = Long.valueOf(0);
						}
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date date;
						java.sql.Date sqlDate = null;
						try {
							date = formatter.parse(cat.child(1).select("div.time > time").text());
							sqlDate = new java.sql.Date(date.getTime());  
							System.out.println(jobtype);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						String companyname = cat.child(0).child(1).child(1).select("p.company-name").text();
						String joblocation = cat.child(0).child(1).child(1).select("div.location > ul > li").text();


						System.out.println(cat.child(1).select("div.time > time").text());
						dBService.saveJobPost(jobtitle, jobtype, industry, minSalary, maxSalary, companyname, sourceUrl, sqlDate, 
								years, "", "");


						/*
							System.out.println(cat.child(0).child(1).child(0).select("a").attr("href"));
							System.out.println(cat.child(0).child(1).child(0).select("a").attr("title"));
							System.out.println(cat.child(0).child(1).child(1).select("p.company-name").text());
							System.out.println(cat.child(0).child(1).child(1).select("p.salary").text().substring(2));

							System.out.println(cat.child(0).child(1).child(1).select("div.location > ul > li").text());
							System.out.println(cat.child(1).select("div.time > time").text());
							System.out.println("------");
						 */

					}
				}

			}
			else {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				String jobposts = "#job_fields_list > div > div";
				do {
					Element job = doc.select(jobposts).first().child(0);
					System.out.println(job.select("a").attr("href"));
					String sourceUrl = job.select("div > a:nth-child(1)").attr("href");
					System.out.println(sourceUrl);
					Document jobpostdetails;
					try {
						long minSalary;
						long maxSalary;
						List<String> industries = new ArrayList<String>();
						List<String> salary = new ArrayList<String>();
						jobpostdetails = Jsoup.connect(sourceUrl).get();
						Element jobcategories = jobpostdetails.select("#left-content > article > div.row > div.col-xs-4.offset20.push-right-20 > ul > li:nth-child(5)").first();

						//industries
						Elements cats = jobcategories.select("a");

						//Job type
						String jobtype = jobpostdetails.select("#left-content > article > div.row > div:nth-child(2) > ul > li:nth-child(4)").text().substring(22);

						//Experience
						long years;
						String experiencetype = jobpostdetails.select("#left-content > article > div.row > div.col-xs-4.offset20.push-right-20 > ul > li:nth-child(2)").text().substring(15);
						System.out.println(experiencetype);
						if (experiencetype.contains("Không yêu cầu kinh nghiệm") || experiencetype.contains("Chưa"))
						{
							years = 0;
						}
						else {
							String[] strArray = experiencetype.split(" ");
							for (String str: strArray) {
								System.out.println(str);
							}
							if (strArray[0].contains("-")) {
								String[] yearArray = strArray[0].split("-",2);
								years = Long.parseLong(yearArray[1]);
							}
							else if (strArray[0].contains("Trên") || strArray[0].contains("Dưới"))
							{
								years = Long.parseLong(strArray[1]);
							}
							else years = Long.parseLong(strArray[0]);
						}



						String street_address = jobpostdetails.select("#left-content > article > div.block-info-company > div > table > tbody > tr:nth-child(2) > td:nth-child(2) > p").text();
						for (Element cat: cats) {
							industries.add(cat.text());
						}
						String line = job.select("div > div:nth-child(1)").first().text();
						System.out.println(line);
						if (line.contains("Trên") || line.contains("Dưới"))
						{
							String[] strArray = line.split("\\s+", 3);
							if (strArray[0].contains("Trên"))
							{
								minSalary = Long.parseLong(strArray[1])*1000000;
								maxSalary = 0;
							} else {
								maxSalary = Long.parseLong(strArray[1])*1000000;
								minSalary = 0;
							}
						}
						else {
							String[] strArray = line.split("\\s+", 2);
							System.out.println(strArray[0]);
							String[] str = strArray[0].split("-", 2);
							System.out.println(str[0]);
							for (String string: str) {
								salary.add(string);
							}
							minSalary = Long.parseLong(salary.get(0))*1000000;
							maxSalary = Long.parseLong(salary.get(1))*1000000;
						}
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String dateInString = job.select("div > div:nth-child(1)").first().nextElementSibling().nextElementSibling().text();
						java.util.Date date;
						java.sql.Date sqlDate;
						String companyname = job.select("div > a:nth-child(2)").attr("title");
						try {
							date = formatter.parse(dateInString);
							sqlDate = new java.sql.Date(date.getTime());  
							System.out.println(jobtype);
							for (String industry: industries) {
								System.out.println(industry);
							}
							dBService.saveJobPost(job.select("div > a:nth-child(1)").attr("title"), jobtype, industries, minSalary, maxSalary, companyname, 
									job.select("div > div:nth-child(1)").first().nextElementSibling().text(), 
									sqlDate, years, street_address, job.select("div > div:nth-child(1)").first().nextElementSibling().text());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


					//System.out.println(job.select("div > a:nth-child(1)").attr("title"));
					//System.out.println(job.select("div > a:nth-child(2)").attr("title"));
					//System.out.println(job.select("div > div:nth-child(1)").first().text());
					//System.out.println(job.select("div > div:nth-child(1)").first().nextElementSibling().text());	
					//System.out.println(job.select("div > div:nth-child(1)").first().nextElementSibling().nextElementSibling().text());	
					//System.out.println("---------------------------------------------");
					jobposts = jobposts.concat(" > div:nth-child(2)");

				} while (doc.select(jobposts).hasText());
			}
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			logger.debug("Text length: {}", text.length());
			logger.debug("Html length: {}", html.length());
			logger.debug("Number of outgoing links: {}", links.size());
		}

		logger.debug("=============");
	}
}
