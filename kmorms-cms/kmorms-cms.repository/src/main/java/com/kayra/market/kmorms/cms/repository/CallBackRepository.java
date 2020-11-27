package com.kayra.market.kmorms.cms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kayra.market.kmorms.cms.documentmodel.BodyModel;
import com.kayra.market.kmorms.cms.documentmodel.OriginalBody;

public interface CallBackRepository extends MongoRepository<BodyModel , String> {

	public void insert(OriginalBody originalBody);

}
