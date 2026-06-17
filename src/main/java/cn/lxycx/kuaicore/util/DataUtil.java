package cn.lxycx.kuaicore.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * 数据结构化处理工具
 * @author RUNTIME
 * 时间：2018年6月10日
 */
public class DataUtil {

	private String menuIdstr; //主键一般为ID
	private String pidstr;  //上级主键
	private String liststr; //存放下级列表

	/**
	 * 初始化
	 * @param menuIdstr 主键一般为ID
	 * @param pidstr 上级主键
	 * @param liststr 存放下级列表
	 */
	public DataUtil(String menuIdstr, String pidstr, String liststr) {
		this.menuIdstr = menuIdstr;
		this.pidstr = pidstr;
		this.liststr = liststr;
	}


	/**
	 * 树形结构菜单生成
	 * @param list
	 * @return
	 */
	public  JSONArray treeMenuList(JSONArray list, String parentId) {
		JSONArray rights = new JSONArray();
		for (int i=0;i<list.size();i++) {
			JSONObject department= list.getJSONObject(i);
			String menuId = department.get(menuIdstr)+"";
			String pid = department.get(pidstr)+"";
			if (parentId.equals(pid)) {
				JSONArray list1 = treeMenuList(list, menuId);
				if(!list1.isEmpty()) {
					department.put(liststr, list1);
				}
				rights.add(department);
			}
		}
		return rights;
	}

	/**
	 * 树形结构菜单生成
	 * @param list
	 * @return
	 */
	public  JSONArray treeMenuList(JSONArray list, String parentId,Callback callback) {
		JSONArray rights = new JSONArray();
		for (int i=0;i<list.size();i++) {
			JSONObject department= list.getJSONObject(i);
			String menuId = department.get(menuIdstr)+"";
			String pid = department.get(pidstr)+"";
			if (parentId.equals(pid)) {
				JSONArray list1 = treeMenuList(list, menuId,callback);
				if(!list1.isEmpty()) {
					department.put(liststr, list1);
				}
				rights.add(callback.run(department));
			}
		}
		return rights;
	}


	/**
     * 树形结构菜单生成
     * @param list
     * @return
     */
    public  List<Map<String,Object>> treeMenuList(List<Map<String,Object>> list, String parentId) {
        List<Map<String,Object>> rights = new ArrayList<Map<String,Object>>();
        for (Map<String,Object> department : list) {
        	String menuId = department.get(menuIdstr)+"";
            String pid = department.get(pidstr)+"";
            if (parentId.equals(pid)) {
                List<Map<String,Object>> list1 = treeMenuList(list, menuId);
                if(!list1.isEmpty()) {
                	department.put(liststr, list1);
                }
                rights.add(department);
            }
        }
        return rights;
    }


    /**
     * 树形结构菜单生成
     * @param list
     * @return
     */
    public  List<Map<String,Object>> treeMenuList(List<Map<String,Object>> list, String parentId,Callback callback) {
        List<Map<String,Object>> rights = new ArrayList<Map<String,Object>>();
        for (Map<String,Object> department : list) {
        	String menuId = department.get(menuIdstr)+"";
            String pid = department.get(pidstr)+"";
            if (parentId.equals(pid)) {
                List<Map<String,Object>> list1 = treeMenuList(list, menuId,callback);
                if(!list1.isEmpty()) {
                	department.put(liststr, list1);
                }
                rights.add(callback.run(department));
            }
        }
        return rights;
    }


    public static interface Callback{
    	public Map<String,Object> run(Map<String, Object> map);
    }




    public static List<Map<String,Object>> jsonToList(JSONArray array, Map<String,String> name, String proid){
    	List<Map<String,Object>> list = new ArrayList<>();
    	int len = array.size();
    	for(int i = 0;i<len;i++){
    		JSONObject obj = array.getJSONObject(i);
    		Map<String,Object> data = new LinkedHashMap<String, Object>();
    		data.put("proid", proid);
    		for(String k:name.keySet()){
    			Object v = obj.get(name.get(k));
    			data.put(k, v!=null&&!"null".equals(v.toString())?v:"");
    		}
    		list.add(data);
    	}
    	return list;
    }



	/**
	 * 将Map中驼峰命名的键转换为下划线命名
	 * @param camelMap 驼峰命名的Map
	 * @return 下划线命名的Map
	 */
	public static <T> Map<String, T> camelToSnake(Map<String, T> camelMap) {
		Map<String, T> snakeCaseMap = new LinkedHashMap<>();
		if(camelMap!=null){
			for (Map.Entry<String, T> entry : camelMap.entrySet()) {
				String key = entry.getKey();
				String snakeCaseKey = toSnakeCase(key);
				T value = entry.getValue();
				if(value instanceof Map){
					Map<String, Object> newvalue = camelToSnake((Map<String, Object>) value);
					snakeCaseMap.put(snakeCaseKey, (T) newvalue);
				}else{
					snakeCaseMap.put(snakeCaseKey,value);
				}
			}
		}

		return snakeCaseMap;
	}

	/**
	 * 将驼峰命名的字符串转换为下划线命名
	 *
	 * @param camelCase 驼峰命名的字符串
	 * @return 下划线命名的字符串
	 */
	public static String toSnakeCase(String camelCase) {
		if (camelCase == null || camelCase.isEmpty()) {
			return camelCase;
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < camelCase.length(); i++) {
			char c = camelCase.charAt(i);
			// 判断是否为大写字母且不是字符串的第一个字符
			if (Character.isUpperCase(c) && i > 0) {
				// 在大写字母前插入下划线，并将大写字母转换为小写
				result.append('_').append(Character.toLowerCase(c));
			} else {
				// 直接添加字符
				result.append(c);
			}
		}
		// 如果第一个字符是大写，将其转换为小写
		if (Character.isUpperCase(camelCase.charAt(0))) {
			return result.charAt(0) + result.substring(1);
		}
		return result.toString();
	}


}
