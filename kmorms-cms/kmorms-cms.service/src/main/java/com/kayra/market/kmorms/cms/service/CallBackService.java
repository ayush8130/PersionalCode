package com.kayra.market.kmorms.cms.service;



import com.kayra.market.kmorms.cms.documentmodel.Body;
import com.kayra.market.kmorms.cms.documentmodel.BodyModel;
import com.kayra.market.kmorms.cms.documentmodel.OriginalBody;

public interface CallBackService {

     public	BodyModel storeBody(BodyModel bodyModel);

	public Body getBody(Body body);

	public void storeOriginalRequest(OriginalBody originalBody);

	
	
	
}
