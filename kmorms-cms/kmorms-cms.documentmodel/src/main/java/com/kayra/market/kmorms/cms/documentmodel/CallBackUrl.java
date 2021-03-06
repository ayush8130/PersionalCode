package com.kayra.market.kmorms.cms.documentmodel;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonView;
import com.views.json.Views;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CallBackUrl {

	@Field
	@JsonView({Views.Data.class})
	private String url;
	
	
}
