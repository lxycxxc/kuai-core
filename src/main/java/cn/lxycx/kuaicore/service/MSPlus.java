package cn.lxycx.kuaicore.service;

import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.util.KuaiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 操作增删改
 */
@Service
public class MSPlus {
    @Lazy
    @Autowired
    MiddleService middleService;
    @Autowired
    HttpServletRequest request;



    public RetJson getPage(String alias, Integer page, Integer limit, Map<String, Object> wheres) {
        RetJson ret =  middleService.getPage( request,  alias,"",  page,  limit, KuaiUtil.toWhere(wheres), null) ;
        return ret;
    }
    public RetJson getPage(String alias, Integer page, Integer limit, Map<String, Object> wheres, Map<String, Object> order_by) {
        RetJson ret = middleService.getPage( request,  alias,"",  page,  limit, KuaiUtil.toWhere(wheres), order_by) ;
        return ret;
    }

    public RetJson getPage(String alias, String field, Integer page, Integer limit, Map<String, Map<String, Object>> wheres, Map<String, Object> order_by) {
        RetJson ret = middleService.getPage( request,alias,"",  page,  limit, wheres, order_by) ;
        return ret;
    }



//---------------------------------------------------------------------------------------------------------


    // 查询列表
    public RetJson getList(String alias, Map<String, Object> wheres){
        return getList(alias,  KuaiUtil.toWhere(wheres),  null);
    }

    public RetJson getList(String alias, Map<String, Map<String, Object>> wheres, Map<String,Object> order_by){

        RetJson ret = middleService.getList( request,alias,"",  wheres,  order_by);
        /*List<Mapp> mappList = new ArrayList<>();
        list.forEach(new Consumer<Map<String, Object>>() {  @Override  public void accept(Map<String, Object> map) {  mappList.add(Mapp.by(map)); }});*/
        return ret;
    }


//--------------------------------------------------------------------------------------------------------------

    //查询单个
    public RetJson getByOne(String alias, Map<String, Object> wheres){
        return getByOne( alias, KuaiUtil.toWhere(wheres), null);
    }

    public RetJson getByOne(String alias, Map<String, Map<String, Object>> wheres, Map<String,Object> order_by){
        return  middleService.getByOne( request,alias, "", wheres, order_by);
    }


//-------------------------------------------------------------------------------------------------------------

    public RetJson deleteByOneSimple(String alias, Map<String, Object> wheres) {
        return middleService.deleteByOne(request,alias, KuaiUtil.toWhere(wheres));
    }

    public RetJson deleteByOne(String alias, Map<String, Map<String, Object>> wheres) {
        return middleService.deleteByOne(request,alias,wheres);
    }




//-------------------------------------------------------------------------------------------------------------


    public RetJson deleteSimple(String alias, Map<String, Object> wheres) {
        return middleService.delete(request,alias, KuaiUtil.toWhere(wheres));
    }

    public RetJson delete(String alias, Map<String, Map<String, Object>> wheres) {
        return middleService.delete(request,alias,wheres);
    }



//-------------------------------------------------------------------------------------------------------------


    public RetJson updateSimple(String alias, Map<String, Object> set_data, Map<String, Object> wheres) {
        return middleService.update( request,alias, set_data,  KuaiUtil.toWhere(wheres));
    }

    public RetJson update(String alias, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres) {
        return middleService.update( request,alias, set_data,  wheres);
    }



//-------------------------------------------------------------------------------------------------------------

    public RetJson updatePlus(String alias, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres) {
        return middleService.updatePlus(request, alias, set_data,  wheres);
    }
//-------------------------------------------------------------------------------------------------------------

    public RetJson insert(String alias, Map<String, Object> set_data) {
        return middleService.insert( request,alias, set_data);
    }
    //-------------------------------------------------------------------------------------------------------------
}
