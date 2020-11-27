package com.kayra.market.kmorms.cms.documentmodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "market")
public class WebPage {
	
	@Id
    private String id;
	
    @Indexed(direction = IndexDirection.ASCENDING)
    private String name;

}
