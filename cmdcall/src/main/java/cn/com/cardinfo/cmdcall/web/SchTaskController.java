package cn.com.cardinfo.cmdcall.web;

import cn.com.cardinfo.cmdcall.dbmodel.SchTask;
import cn.com.cardinfo.cmdcall.model.CreateMainTaskDTO;
import cn.com.cardinfo.cmdcall.model.CronRule;
import cn.com.cardinfo.cmdcall.model.FixedRule;
import cn.com.cardinfo.cmdcall.model.RuleType;
import cn.com.cardinfo.cmdcall.repo.SchTaskMongoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronSequenceGenerator;
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
    public ResponseEntity<String> createMainTask(@RequestBody CreateMainTaskDTO createMainTaskDTO){
        if(createMainTaskDTO.getRuleType()==null){
            throw new RuntimeException("rule type不能为空");
        }
        SchTask schTask = new SchTask();
        switch (createMainTaskDTO.getRuleType()){
            case fixed: {
                Long delay = createMainTaskDTO.getDelay();
                if (delay == null) {
                    throw new RuntimeException("delay不能为空");
                }
                FixedRule rule = new FixedRule();
                rule.setDelay(delay);
                schTask.setRule(rule);
                break;
            }
            case cron: {
                String cron = createMainTaskDTO.getCron();
                if (cron == null || !CronSequenceGenerator.isValidExpression(cron)) {
                    throw new RuntimeException("cron表达示不正确");
                }
                CronRule rule = new CronRule();
                rule.setCron(cron);
                schTask.setRule(rule);
                break;
            }
        }
        schTask.setDesc(createMainTaskDTO.getDesc());
        SchTask dbTask = schTaskMongoRepo.save(schTask);
        return getJSONResp(dbTask.getId(), HttpStatus.OK);
    }
}
