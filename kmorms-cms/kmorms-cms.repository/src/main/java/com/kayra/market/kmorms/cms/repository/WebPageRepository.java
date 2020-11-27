package com.kayra.market.kmorms.cms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.kayra.market.kmorms.cms.documentmodel.WebPage;

public interface WebPageRepository extends MongoRepository<WebPage, String>, QuerydslPredicateExecutor<WebPage> {
	
    @Query("{ 'name' : ?0 }")
    List<WebPage> findWebPagesByName(String name);

    @Query("{ 'name' : { $regex: ?0 } }")
    List<WebPage> findWebPagesByRegexpName(String regexp);

    List<WebPage> findByName(String name);

    List<WebPage> findByNameStartingWith(String regexp);

    List<WebPage> findByNameEndingWith(String regexp);

    @Query(value = "{}", fields = "{name : 1}")
    List<WebPage> findNameAndId();

    
}
