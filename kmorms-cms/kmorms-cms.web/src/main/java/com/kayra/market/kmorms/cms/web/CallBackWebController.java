package com.kayra.market.kmorms.cms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kayra.market.kmorms.cms.documentmodel.Body;
import com.kayra.market.kmorms.cms.documentmodel.BodyModel;
import com.kayra.market.kmorms.cms.documentmodel.CallBackData;
import com.kayra.market.kmorms.cms.documentmodel.OriginalBody;
import com.kayra.market.kmorms.cms.service.CallBackService;

@RestController
@RequestMapping("/req")
public class CallBackWebController {

     
	public static final String PUB_OUTPUT = MediaType.APPLICATION_JSON_VALUE;
	public static final String PUB_INPUT = MediaType.APPLICATION_JSON_VALUE;
	
	
	@Autowired
	CallBackService callBackService;
	
	
	
	
	
	/*
	 * first Api
	 * 
	 */
	
	@RequestMapping(value = "/request" , method = RequestMethod.POST , produces = PUB_OUTPUT , consumes = PUB_INPUT)
	  public ResponseEntity<Object> body(@RequestBody BodyModel bodyModel){
		
	    if(bodyModel.getBody() != null) {
	    	
	    	return new ResponseEntity<Object>(callBackService.storeBody(bodyModel) , HttpStatus.OK);
	    				    	
	    }else {
			String message = "input is invalid";
	    	throw new Error(message);
		}	
		
	}
	
	
	/*
	 * first Api
	 * 
	 */
	
	@RequestMapping(value = "/requests" , method = RequestMethod.POST , produces = PUB_OUTPUT , consumes = PUB_INPUT)
	  public ResponseEntity<Object> bodyWithUrl(@RequestBody Body body){
		
	    if(body.getBody() != null) {
	    	
	    	return new ResponseEntity<Object>(callBackService.getBody(body) , HttpStatus.OK);
	    				    	
	    }else {
			String message = "input is invalid";
	    	throw new Error(message);
		}	
		
	}
	
	
	
	/*
	 * second Api
	 * 
	 */
	
	@RequestMapping(value = "/callback/{id}" , method = RequestMethod.POST , produces = PUB_OUTPUT , consumes = PUB_INPUT)
	  public ResponseEntity<Object> originalRequest(@PathVariable("id") String id , @RequestBody OriginalBody originalBody){
		
		if(id != null) {
			callBackService.storeOriginalRequest(originalBody);
			
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}else {
			String message = "input is invalid";
	    	throw new Error(message);
			
		}
		
		}
	

   /*
    * third api
    * 
    


	@RequestMapping(value = "/callback/{id}" , method = RequestMethod.PUT , produces = PUB_OUTPUT , consumes = PUB_INPUT)
   public ResponseEntity<Object> createCallBack(@PathVariable("id") String id , @RequestBody CallBackData callBackData ){
		
		if(id != null) {
			callBackService.createTheCallBack();
			
			
		}
		
		
		
		return null;
		
	}

   */








}
