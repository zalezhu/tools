package cn.com.cardinfo.cmdcall.repo;

import cn.com.cardinfo.cmdcall.dbmodel.SchTask;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Zale on 16/8/29.
 */
public interface SchTaskMongoRepo extends MongoRepository<SchTask,String>{
}
