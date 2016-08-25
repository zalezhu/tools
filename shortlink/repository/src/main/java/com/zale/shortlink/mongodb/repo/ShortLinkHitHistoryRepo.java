package com.zale.shortlink.mongodb.repo;

import com.zale.shortlink.mongodb.entity.ShortLink;
import com.zale.shortlink.mongodb.entity.ShortLinkHitHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Zale on 16/8/25.
 */
public interface ShortLinkHitHistoryRepo extends MongoRepository<ShortLinkHitHistory,String> {
}
