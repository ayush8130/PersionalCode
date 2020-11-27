package com.kayra.market.kmorms.cms.documentmodel;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonView;
import com.views.json.Views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "CallBack")
public class OriginalBody {

	@Field
	@JsonView({Views.Data.class})
	private String originalBody;
	
	
	
}
