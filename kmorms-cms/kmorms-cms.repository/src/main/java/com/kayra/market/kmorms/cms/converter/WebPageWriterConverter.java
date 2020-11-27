package com.kayra.market.kmorms.cms.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kayra.market.kmorms.cms.documentmodel.WebPage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component
public class WebPageWriterConverter implements Converter<WebPage, DBObject> {

    @Override
    public DBObject convert(final WebPage page) {
        final DBObject dbObject = new BasicDBObject();
        dbObject.put("name", page.getName());
        dbObject.removeField("_class");
        return dbObject;
    }

}
