package com.kayra.market.kmorms.cms.service;

import java.util.List;

import com.kayra.market.kmorms.cms.documentmodel.WebPage;

public interface WebPageService {
	
	WebPage save(final WebPage webPage);
	
	List<WebPage> fetchAllWebPages();

}
