package com.kayra.market.kmorms.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayra.market.kmorms.cms.documentmodel.WebPage;
import com.kayra.market.kmorms.cms.repository.WebPageRepository;
import com.kayra.market.kmorms.cms.service.WebPageService;

@Service
public class WebPageServiceImpl implements WebPageService {
	
	@Autowired
	private WebPageRepository repository;

	@Override
	public WebPage save(final WebPage webPage) {
		return repository.save(webPage);
	}

	@Override
	public List<WebPage> fetchAllWebPages() {
		return repository.findAll();
	}

}
