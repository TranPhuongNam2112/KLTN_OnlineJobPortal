package com.datn.webcrawler;

import com.datn.webcrawler.service.DBServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class CrawlerFactory implements CrawlController.WebCrawlerFactory<MyWebCrawler> {

    private ComboPooledDataSource comboPooledDataSource;

    public CrawlerFactory(ComboPooledDataSource comboPooledDataSource) {
        this.comboPooledDataSource = comboPooledDataSource;
    }

	public MyWebCrawler newInstance() throws Exception {
		 return new MyWebCrawler(new DBServiceImpl(comboPooledDataSource));
	}


}
