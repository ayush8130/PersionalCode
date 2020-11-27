package com.kayra.market.kmorms.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayra.market.kmorms.cms.documentmodel.Body;
import com.kayra.market.kmorms.cms.documentmodel.BodyModel;
import com.kayra.market.kmorms.cms.documentmodel.CallBackUrl;
import com.kayra.market.kmorms.cms.documentmodel.OriginalBody;
import com.kayra.market.kmorms.cms.repository.CallBackRepository;
import com.kayra.market.kmorms.cms.service.CallBackService;

@Service
public class CallBackServiceImpl implements CallBackService {

	@Autowired
	CallBackRepository callBackRepository;
	
	
	@Override
	public BodyModel storeBody(BodyModel bodyModel) {
		return callBackRepository.insert(bodyModel);
		
	}


	@Override
	public Body getBody(Body body) {
		
		String url = "/callback/102";		
		CallBackUrl call = new CallBackUrl();		
		call.setUrl(url);
		
		body.setUrl(call);
		
		
		return body;
	}


	@Override
	public void storeOriginalRequest(OriginalBody originalBody) {
		callBackRepository.insert(originalBody);
		
	}

}
