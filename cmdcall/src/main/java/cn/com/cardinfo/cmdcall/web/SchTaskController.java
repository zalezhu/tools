package cn.com.cardinfo.cmdcall.web;

import cn.com.cardinfo.cmdcall.dbmodel.SchTask;
import cn.com.cardinfo.cmdcall.model.CreateSchTaskDTO;
import cn.com.cardinfo.cmdcall.repo.SchTaskMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zale on 16/8/29.
 */
@RestController("/sch")
public class SchTaskController extends BaseController{
    @Autowired
    private SchTaskMongoRepo schTaskMongoRepo;

    @RequestMapping(path="/tasks",method = RequestMethod.POST)
    public ResponseEntity<String> createSchTask(@RequestBody CreateSchTaskDTO createSchTaskDTO){

    }
}
