package com.zale.shortlink.mongodb.repo;

import com.zale.shortlink.mongodb.entity.ShortLink;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
/**
 * Created by Zale on 16/8/22.
 */
public interface ShortLinkMongoRepo extends MongoRepository<ShortLink,String> {
    List<ShortLink> findBySlinkLikeAndLlinkLike(String slink,String llink, Pageable pageable);
    Integer countBySlinkLikeAndLlinkLike(String slink,String llink);
}
