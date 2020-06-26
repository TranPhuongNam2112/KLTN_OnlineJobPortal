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

		if (href.startsWith("https://www.timviecnhanh.com/") 
			|| href.startsWith("https://careerbuilder.vn/")
			|| href.startsWith("https://jobsgo.vn/")
			|| href.startsWith("https://timviec365.vn/")	
				) {
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
						String companyimageUrl = cat.select("div.figure > div.image > a > img").attr("src");
						String desc = null;
						try {
							Document post = Jsoup.connect(sourceUrl).get();
							Elements industries = post.select("#tab-1 > section > div.row > div:nth-child(1) > div > div.industry");
							if (industries.size() == 0) {
								industries = post.select("#info-career-desktop > ul > li:nth-child(5) > div > a");
								Elements industrynames = industries;
								jobtype = post.select("#info-career-desktop > ul > li:nth-child(2) > div").text();
								Element description = post.select("\"body > main > section.search-result-list-detail > div > div > div.col-lg-7.col-xl-8 > div > section > div.template-200 > div.left-col > div:nth-child(2) > div > ul").first();
								desc = description.children().text();

								String experiencetype = post.select("#info-career-desktop > ul > li:nth-child(6) > div").text();
								//System.out.println(experiencetype);
								if (experiencetype.contains("Không yêu cầu kinh nghiệm") || experiencetype.contains("Chưa"))
								{
									years = 0;
								}
								else {
									String[] strArray = experiencetype.split("\\s+");
							
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
								desc = post.select("#tab-1 > section > div:nth-child(3) > h3").first().children().text();
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
									//System.out.println(string);
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
							//System.out.println(jobtype);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						String companyname = cat.child(0).child(1).child(1).select("p.company-name").text();
						String joblocation = cat.child(0).child(1).child(1).select("div.location > ul > li").text();


						//System.out.println(cat.child(1).select("div.time > time").text());
						dBService.saveJobPost(jobtitle, jobtype, industry, minSalary, maxSalary, companyname, sourceUrl, sqlDate, 
								years, "", joblocation, companyimageUrl, "CareerBuilder", desc);


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
			else if(page.getWebURL().toString().contains("https://jobsgo.vn/")) {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				Elements joblist = doc.select("div.job-list.brows-employer-list");
				for (Element ele : joblist.select("div.item div.brows-job-list div.brows-job-position")) {
					
					List<String> industries = new ArrayList<String>();
						String jobtype = null; 
						String companyimageUrl = null;
						String street_address = null;
						long years = 0;
						
						String sourceUrl =  ele.select(" div.h3 a").attr("href");
						System.out.println(sourceUrl);
						String jobtitle = ele.select(" div.h3 a").attr("title");
						System.out.println(jobtitle);
						
						String city_province = ele.select("p.font-12 span ").first().text();
						System.out.println("City Provinces"+city_province);
						String description = null;
						
						try {
							Document post = Jsoup.connect(sourceUrl).get();
						
							Elements cats = post.select("div.box-jobs-info div.list a");
							for (Element cat: cats) {
								industries.add(cat.text());
								System.out.println("Industries:"+ industries);
							}
							companyimageUrl = post.select("div.profile-cover div.profile-thumb img").attr("abs:src");
							System.out.println("Company Image"+ companyimageUrl);
							jobtype = post.select("div.box-jobs-info p a").last().text();
							post.select("div.box-jobs-info div.data.giaphv p b").remove();
							post.select("div.box-jobs-info div.data.giaphv p a").remove();
							street_address = post.select("div.box-jobs-info div.data.giaphv p").text().replace("()", " ");
							System.out.println("Street Address"+ street_address);
							System.out.println("JobType"+jobtype);
							//experiences year start
							String experiencetype = post.select("div.panel-body div.content-group div.box-jobs-info p").last().text();

								if (experiencetype.contains("Không yêu cầu"))
								{
									years = 0;
									System.out.println(years);
								}
								else {
									String[] strArray = experiencetype.split("\\s+");
									for (String str: strArray) {
										System.out.println("Years"+ str);
									}
									if (strArray[0].contains("-")) {
										String[] yearArray = strArray[0].split("-");
										years = Long.parseLong(yearArray[1]);
										System.out.println(years);
									}
									else if (strArray[0].contains("Trên") || strArray[0].contains("Dưới"))
									{
										years = Long.parseLong(strArray[1]);
										System.out.println(years);
									}
									else {
										years = Long.parseLong(strArray[0]);
										System.out.println(years);
									}
									description = post.select("body > div.page-container > div.page-content > div > div > div.row > div.col-sm-8.pr0.job-detail-col-1 > div > div > div:nth-child(2) > div").first().children().text();
								}								

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
						//salary start
						Long minSalary= null;
						Long maxSalary= null;
						List<String> salary = new ArrayList<String>();	
//						List<String> industries = new ArrayList<String>();
						String line = ele.select("p.font-12 span.brows-job-sallery").text();
						System.out.println("line0"+line);
						if (!line.contains("Thỏa thuận") 
								&& !line.contains("Từ") 
								&& !line.contains("Đến")) {
							String[] strArray = line.split(" ", 5);
							for (String string : strArray) {
								if (!string.equals("")) {
									System.out.println(string);
									salary.add(string);
								}
							}
							try {
								minSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(0)).longValue())*1000000;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								maxSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(2)).longValue())*1000000;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (line.contains("Đến")) {
							List<String> numbers= new ArrayList<String>();
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(line);
							while(m.find()) {
								numbers.add(m.group());
							}
							maxSalary = Long.valueOf(numbers.get(0))*1000000;
							minSalary = Long.valueOf(0);
						}
						else if(line.contains("Từ")) {
							List<String> numbers= new ArrayList<String>();
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(line);
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
						//salary end
						//expirationdate start
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date date;
						java.sql.Date sqlDate = null;
						try {
							date = formatter.parse(ele.select("p.font-12 span.mrg-l-10").text());
							sqlDate = new java.sql.Date(date.getTime());  
							System.out.println(sqlDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						//expirationdate end
						String companyname = ele.select("p.font-13 a").attr("title");
						System.out.println(companyname);
					

						System.out.println(ele.select("p.font-12 span.mrg-l-10").text());
						dBService.saveJobPost(jobtitle, jobtype, industries, minSalary, maxSalary, companyname, sourceUrl, sqlDate, 
								years, street_address, city_province,companyimageUrl, "jobsgo", description);
					
				}
			}
			else if (page.getWebURL().toString().contains("https://timviec365.vn/")) {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				Elements joblist = doc.select("div.main_cate div.item_cate");
				for (Element ele : joblist.select("div.center_cate")) {
					
					List<String> industries = new ArrayList<String>();
						String companyimageUrl = null;
						String jobtype = null; 
						String street_address = null;
						long years = 0;
						java.sql.Date sqlDate = null;
						Long minSalary= null;
						Long maxSalary= null;
						String sourceUrl = "https://timviec365.vn"+ ele.select("p:eq(0) a").attr("href");
						System.out.println("hihi"+ sourceUrl);
						String jobtitle = ele.select("p:eq(0) a").attr("title");
						System.out.println(jobtitle);
						
						String city_province = ele.select("p:eq(2) span").text();
						System.out.println("City Provinces : "+city_province);
						String description = null;
						
						try {
							Document post = Jsoup.connect(sourceUrl).get();
							Elements cats = post.select("div.box_tomtat div.form_control.ic8 span");
							for (Element cat: cats) {
								industries.add(cat.text());
								System.out.println("Industries:"+ industries);
							}
							//expirationdate start
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							java.util.Date date;
							
							try {
								date = formatter.parse(post.select("div.right_tit p span").last().text());
								sqlDate = new java.sql.Date(date.getTime());  
								System.out.println(sqlDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							//expirationdate end
							companyimageUrl = post.select("div.box_tit_detail div.img_detail img").first().attr("data-src");
							System.out.println("Company URL :"+ companyimageUrl);
							jobtype = post.select("div.box_tomtat div.form_control.ic2 span").first().text();
							street_address = post.select("div.right_tit p:eq(3)").text().replace("Địa chỉ công ty:", "");
							System.out.println("Street Address:"+ street_address);
							System.out.println("JobType"+jobtype);
							description = post.select("body > section > div > div.left_detail > div.content_detail > div.tt_td > div.box_mota").text().substring(18);
							//experiences year start
							String experiencetype = post.select("div.box_tomtat div.form_control.ic3 span").text();

								if (experiencetype.contains("Không yêu cầu"))
								{
									years = 0;
									System.out.println(years);
								}
								else {
									String[] strArray = experiencetype.split("\\s+");
									for (String str: strArray) {
										System.out.println("Years"+ str);
									}
									if (strArray[0].contains("-")) {
										String[] yearArray = strArray[0].split("-");
										years = Long.parseLong(yearArray[1]);
										System.out.println(years);
									}
									else if (strArray[0].contains("Hơn"))
									{
										years = Long.parseLong(strArray[1]);
										System.out.println(years);
									}
									else {
										years = Long.parseLong(strArray[0]);
										System.out.println(years);
									}
								}			
								//salary start
								
								List<String> salary = new ArrayList<String>();	
								String line = post.select("div.right_tit p.lv_luong span").text();
								System.out.println("line0"+line);
								if (!line.contains("Thỏa thuận") 
										&& !line.contains("Trên")) {
									String[] strArray = line.split(" ", 4);
									for (String string : strArray) {
										if (!string.equals("")) {
											System.out.println(string);
											salary.add(string);
										}
									}
									try {
										minSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(0)).longValue())*1000000;
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									try {
										maxSalary = new Long(NumberFormat.getNumberInstance(Locale.FRANCE).parse(salary.get(2)).longValue())*1000000;
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else if (line.contains("Trên")) {
									List<String> numbers= new ArrayList<String>();
									Pattern p = Pattern.compile("\\d+");
									Matcher m = p.matcher(line);
									while(m.find()) {
										numbers.add(m.group());
									}
									maxSalary = Long.valueOf(numbers.get(0))*1000000;
									minSalary = Long.valueOf(0);
								}
								 else
								{
									minSalary = Long.valueOf(0);
									maxSalary = Long.valueOf(0);
								}
								//salary end

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						

						String companyname = ele.select("p:eq(1) a").attr("title");
						System.out.println(companyname);
					
						System.out.println(ele.select("p.font-12 span.mrg-l-10").text());
						dBService.saveJobPost(jobtitle, jobtype, industries, minSalary, maxSalary, companyname, sourceUrl, sqlDate, 
								years, street_address, city_province, companyimageUrl, "timviec365", description);
					
				}	
			}
			//topcv start
			else if(page.getWebURL().toString().contains("https://www.topcv.vn/")) {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				Elements joblist = doc.select("div.job-list.search-result div.result-job-hover");
				for (Element ele : joblist.select("div.col-sm-8")) {
					
					List<String> industries = new ArrayList<String>();
						String jobtype = null; 
						String companyimageUrl = null;
						String street_address = null;
						long years = 0;
						java.sql.Date sqlDate = null;
						Long minSalary= null;
						Long maxSalary= null;
						String sourceUrl = ele.select("h4 a").attr("href");
						System.out.println("hihi"+ sourceUrl);
						String jobtitle = ele.select("h4 a span").text();
						System.out.println(jobtitle);
						
						String city_province = ele.select("div#row-result-info-job div:eq(1) span").text().replace("Khu vực: ", "");
						System.out.println("City Provinces : "+city_province);
						//salary start
						
						List<String> salary = new ArrayList<String>();	
						String line = ele.select("div#row-result-info-job div.salary").text();
						System.out.println("line0"+line);
						if (line.contains("Đăng nhập để xem")) {
							minSalary = Long.valueOf(0);
							maxSalary = Long.valueOf(0);
						}
						else if (!line.contains("Thỏa thuận") 
								&& !line.contains("Trên")
								&& !line.contains("Tới")
								) {
							String[] strArray = line.split("\\s+", 2);
							System.out.println("line1"+strArray[0]);
							String[] str = strArray[0].split("-", 2);
							System.out.println("line2"+str[0]);
							System.out.println("line3"+str[1]);
							for (String string: str) {
								salary.add(string);
							}
							minSalary = Long.parseLong(salary.get(0))*1000000;
							System.out.println(minSalary);
							maxSalary = Long.parseLong(salary.get(1))*1000000;
							System.out.println(maxSalary);
						} else if (line.contains("Trên") || line.contains("Tới")) {
							String[] strArray = line.split("\\s+", 3);
							if (strArray[0].contains("Trên"))
							{
								minSalary = Long.parseLong(strArray[1])*1000000;
								maxSalary = Long.valueOf(0);
							} else {
								maxSalary = Long.parseLong(strArray[1])*1000000;
								minSalary = Long.valueOf(0);
							}
						}
						 else
						{
							minSalary = Long.valueOf(0);
							maxSalary = Long.valueOf(0);
						}
						String description = null;
						//salary end
						try {
							Document post = Jsoup.connect(sourceUrl).get();
							description = post.select("#col-job-left > div:nth-child(2)").first().text();
							Elements cats = post.select("div#col-job-left div:eq(10)");
							for (Element cat: cats.select("span")) {
								industries.add(cat.text());
								System.out.println("Industries:"+ industries);
							}
							//expirationdate start
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							java.util.Date date;
							
							try {
								date = formatter.parse(post.select("div#row-job-title div.job-deadline").text().replace("Hạn nộp hồ sơ: ",""));
								sqlDate = new java.sql.Date(date.getTime());  
								System.out.println(sqlDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							//expirationdate end
							jobtype = post.select("div#box-info-job div.job-info-item:eq(1) span").text();
							street_address = post.select("div#row-job-title div.col-sm-9 div.text-dark-gray").first().text();
							companyimageUrl = post.select("div#row-job-title div.company-logo-wraper a img").attr("abs:src");
							System.out.println("Street Address:"+ street_address);
							System.out.println("JobType"+jobtype);
							//experiences year start
							String experiencetype = post.select("div#box-info-job div.job-info-item:eq(4) span ").text();

								if (experiencetype.contains("Không yêu cầu"))
								{
									years = 0;
									System.out.println(years);
								}
								else {
									String[] strArray = experiencetype.split("\\s+");
									for (String str: strArray) {
										System.out.println("Years"+ str);
									}
									 if (strArray[0].contains("Dưới"))
									{
										years = Long.parseLong(strArray[1]);
										System.out.println(years);
									}
									else {
										years = Long.parseLong(strArray[0]);
										System.out.println(years);
									}
								}			
								

						} catch (IOException e1) {
							e1.printStackTrace();
						}
						

						String companyname = ele.select("div.row-company a").text();
						System.out.println(companyname);

						//System.out.println(ele.select("p.font-12 span.mrg-l-10").text());
						dBService.saveJobPost(jobtitle, jobtype, industries, minSalary, maxSalary, companyname, sourceUrl, sqlDate, 
								years, street_address, city_province,companyimageUrl, "topcv", description);
					
				}
			}
			//topCv end
			//timviecnhanh
			else {
				Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
				String jobposts = "#job_fields_list > div > div";
				do {
					Element job = doc.select(jobposts).first().child(0);
					//System.out.println(job.select("a").attr("href"));
					String sourceUrl = job.select("a").attr("href");
					//System.out.println(sourceUrl);
					String description;
					Document jobpostdetails;
					try {
						long minSalary;
						long maxSalary;
						List<String> industries = new ArrayList<String>();
						List<String> salary = new ArrayList<String>();
						jobpostdetails = Jsoup.connect(sourceUrl).get();
						Element jobcategories = jobpostdetails.select("#left-content > article > div.row > div.col-xs-4.offset20.push-right-20 > ul > li:nth-child(5)").first();
						description = jobpostdetails.select("#left-content > article > table > tbody > tr:nth-child(1) > td:nth-child(2) > p").first().text();
						//industries
						Elements cats = jobcategories.select("a");

						//Job type
						String jobtype = jobpostdetails.select("#left-content > article > div.row > div:nth-child(2) > ul > li:nth-child(4)").text().substring(22);

						//Experience
						long years;
						String experiencetype = jobpostdetails.select("#left-content > article > div.row > div.col-xs-4.offset20.push-right-20 > ul > li:nth-child(2)").text().substring(15);
						//System.out.println(experiencetype);
						if (experiencetype.contains("Không yêu cầu kinh nghiệm") || experiencetype.contains("Chưa"))
						{
							years = 0;
						}
						else {
							String[] strArray = experiencetype.split(" ");
							
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
						//System.out.println(line);
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
							//System.out.println(strArray[0]);
							String[] str = strArray[0].split("-", 2);
							//System.out.println(str[0]);
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
						String companyimageUrl = job.select("a > span > img").attr("src");
						try {
							date = formatter.parse(dateInString);
							sqlDate = new java.sql.Date(date.getTime());  
							//System.out.println(jobtype);
							
							dBService.saveJobPost(job.select("div > a:nth-child(1)").attr("title"), jobtype, industries, minSalary, maxSalary, companyname, 
									sourceUrl, sqlDate, years, street_address, job.select("div > div:nth-child(1)").first().nextElementSibling().text(), companyimageUrl, "timviecnhanh", description);
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
