package com.zale.shortlink.web.controller;

import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.cardsmart.esbsdk.util.DubboInvoker;
import com.cardsmart.inf.entity.RespEntity;
import com.zale.shortlink.entity.Page;
import com.zale.shortlink.mongodb.entity.ShortLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zale on 16/8/24.
 */
@RestController
public class ShortLinkController extends BaseController{
    @Autowired
    private DubboInvoker dubboInvoker;

    @RequestMapping(value = "/rest/shortlinks",method = RequestMethod.GET)
    public ResponseEntity<Page<ShortLink>> getShortLinks(@RequestParam(value = "iDisplayStart", required = false, defaultValue = DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
            @RequestParam(value = "iDisplayLength", required = false, defaultValue = DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
            @RequestParam(value = "slink", required = false) String slink,
            @RequestParam(value="llink",required = false)String llink)
            throws InvocationTargetException, IOException, InstantiationException, ParseException, IllegalAccessException {
        Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(slink)) {
            params.put("slink", slink);
        }
        if(StringUtils.isNotEmpty(llink)) {
            params.put("llink", llink);
        }
        params.put("startRecord", startRecord);
        params.put("maxRecords", maxRecords);
        RespEntity<Page<ShortLink>> rstMap = dubboInvoker.invoke("SLK_002","1.0",params,RespEntity.class);
        if("0000".equals(rstMap.getKey())){
            return getJSONResp( rstMap.getExt(), HttpStatus.OK);
        }else{
            throw new RuntimeException(rstMap.getMsg());
        }

    }

    @RequestMapping(value = "/rest/shortlink",method = RequestMethod.POST)
    public ResponseEntity<String> createShortLinks(@RequestBody Map shortLink)
            throws InvocationTargetException, IOException, InstantiationException, ParseException, IllegalAccessException,
            java.text.ParseException {
        Map<String, Object> params = new HashMap<>();
        String url = (String) shortLink.get("llink");
        if(StringUtils.isEmpty(url)){
            throw new RuntimeException("长裢不能为空");
        }
        if(url.indexOf("http")!=0){
            url="http://"+url;
        }
        params.put("url",url);
        if(StringUtils.isNotEmpty((String) shortLink.get("expireDate"))) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            params.put("expireDate", df.parse((String) shortLink.get("expireDate")).getTime());
        }
        RespEntity<String> rst = dubboInvoker.invoke("SLK_003","1.0",params,RespEntity.class);
        if("0000".equals(rst.getKey())){
            return getJSONResp( rst.getExt(), HttpStatus.OK);
        }else{
            throw new RuntimeException(rst.getMsg());
        }
    }

    @RequestMapping(value = "/rest/shortlink/{slink}",method = RequestMethod.PUT)
    public ResponseEntity<String> updateShortLinks(@PathVariable("slink") String slink,@RequestBody Map shortLink)
            throws InvocationTargetException, IOException, InstantiationException, ParseException, IllegalAccessException,
            java.text.ParseException {
        Map<String, Object> params = new HashMap<>();
        if(slink==null){
            throw new RuntimeException("长裢不能为空");
        }
        params.put("slink", slink);
        String url = (String) shortLink.get("llink");

        if(url!=null){
            if(url.indexOf("http")!=0){
                url="http://"+url;
            }
            params.put("llink",url);
        }
        if(StringUtils.isNotEmpty((String) shortLink.get("expireDate"))) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            params.put("expireDate", df.parse((String) shortLink.get("expireDate")).getTime());
        }
        RespEntity<String> rst = dubboInvoker.invoke("SLK_005","1.0",params,RespEntity.class);
        if("0000".equals(rst.getKey())){
            return getJSONResp( rst.getMsg(), HttpStatus.OK);
        }else{
            throw new RuntimeException(rst.getMsg());
        }
    }

    @RequestMapping(value="/rest/shortlink/{slink}",method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteShortLink(@PathVariable("slink") String slink)
            throws InvocationTargetException, IOException, InstantiationException, ParseException, IllegalAccessException {
        Map<String, Object> params = new HashMap<>();
        if(slink==null){
            throw new RuntimeException("长裢不能为空");
        }
        params.put("slink", slink);
        RespEntity<String> rst = dubboInvoker.invoke("SLK_006","1.0",params,RespEntity.class);
        if("0000".equals(rst.getKey())){
            return getJSONResp( rst.getMsg(), HttpStatus.OK);
        }else{
            throw new RuntimeException(rst.getMsg());
        }
    }
}
