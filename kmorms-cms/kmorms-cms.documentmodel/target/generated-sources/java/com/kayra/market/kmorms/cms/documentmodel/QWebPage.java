package com.kayra.market.kmorms.cms.documentmodel;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWebPage is a Querydsl query type for WebPage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebPage extends EntityPathBase<WebPage> {

    private static final long serialVersionUID = 1636106276L;

    public static final QWebPage webPage = new QWebPage("webPage");

    public final StringPath id = createString("id");

    public final StringPath name = createString("name");

    public QWebPage(String variable) {
        super(WebPage.class, forVariable(variable));
    }

    public QWebPage(Path<? extends WebPage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebPage(PathMetadata metadata) {
        super(WebPage.class, metadata);
    }

}

