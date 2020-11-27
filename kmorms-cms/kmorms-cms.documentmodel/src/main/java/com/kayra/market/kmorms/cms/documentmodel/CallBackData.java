package com.kayra.market.kmorms.cms.documentmodel;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.views.json.Status;
import com.views.json.Views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "CallBack")
public class CallBackData {

	@Field
	@JsonView({Views.Data.class})
	private String detail;
	
	@Field
	@JsonView({Views.Data.class})
	@JsonSerialize
	private Status status;
	
}