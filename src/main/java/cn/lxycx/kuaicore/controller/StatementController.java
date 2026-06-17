package cn.lxycx.kuaicore.controller;

import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.service.MiddleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/***
 * 报表管理
 */
@Slf4j
@Deprecated
@RestController
@RequestMapping("statement")
public class StatementController {

    @Autowired
    MiddleService middleService;
    @Autowired
    HttpServletRequest request;



//-----------------------------------查询操作-----------------------------------------------------------

    @RequestMapping("/{alias}/getList")
    public String getList(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getList(request,alias,"",param,order_by);
        return retJson.toJson();
    }
    @RequestMapping("/{alias}/getPage/{page}/{limit}")
    public String getPage(@PathVariable String alias,@PathVariable int page,@PathVariable int limit
            , @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getPage(request,alias,"",page,limit,param,order_by);
        return retJson.toJson();
    }

    @RequestMapping("/{alias}/getByOne")
    public String getByOne(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        log.info("getByOne");
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getByOne(request,alias,"",param,order_by);
        return retJson.toJson();
    }

}
