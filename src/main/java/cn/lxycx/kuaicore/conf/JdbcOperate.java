package cn.lxycx.kuaicore.conf;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * JDBC操作
 * @author TIME
 *
 */
@Slf4j
public class JdbcOperate {

	private Connection con;
	private JdbcOperate() {}
	private JdbcOperate(Connection con) {
		this.con = con;
	}

	public static JdbcOperate by(Connection con) {
		return new JdbcOperate(con);
	}

	public List<String> findColumnLabel(String sql,Object ... params){
		List<String> fieldmap = new ArrayList();

		try {
			PreparedStatement st = con.prepareStatement(sql);
			for(int i=0;i<params.length;i++) {
				Object o = params[i];
				st.setObject(i+1, o);
			}

			ResultSet rs = st.executeQuery();
			//异步执行

			ResultSetMetaData md = rs.getMetaData();
			int colomn = md.getColumnCount();
			for(int i=1;i<=colomn;i++) {
				String name = md.getColumnLabel(i);//md.getColumnName(i);
				fieldmap.add(name);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return fieldmap;

	}

	public List<Map<String,Object>> find(String sql,Object ... params){

		List<Map<String,Object>> listData = new ArrayList<>();


		try {
			PreparedStatement st = con.prepareStatement(sql);
			for(int i=0;i<params.length;i++) {
				Object o = params[i];
				st.setObject(i+1, o);
			}

			ResultSet rs = st.executeQuery();
			//异步执行

			ResultSetMetaData md = rs.getMetaData();
			int colomn = md.getColumnCount();


			while(rs.next()) {
				Map<String,Object> map = new LinkedHashMap<String,Object>();
				//md = rs.getMetaData();
				for(int i=1;i<=colomn;i++) {
					String name = md.getColumnName(i);
					Object value = rs.getObject(i);
					map.put(name, value);
				}
				listData.add(map);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return listData;

	}



	/**
	 * 更新操作
	 * @param table
	 * @param maps
	 * @param wheres
	 * @return
	 * @throws SQLException
	 */
	public int update(String table,Map<String,Object> maps,String...wheres) throws SQLException {
		//try {
			String sql = "update "+table+ " set ";
			if(!maps.isEmpty()) {
				String p = "";
				List<Object> values = new ArrayList<>();
				for(String k:maps.keySet()) {
					p += ","+k+"= ? ";
					values.add(maps.get(k));
				}

				sql = sql+p.substring(1)+" where 1=1 ";

				if(wheres!=null) {
					for(String w:wheres) {
						sql += " and "+ w +"=?";
						values.add(maps.get(w));
					}

				}

				PreparedStatement st = con.prepareStatement(sql);
				int i=1;

				for(Object o:values) {
					st.setObject(i++, o);
				}

				log.info("sql:"+sql);
				log.info("param:"+values);

				return st.executeUpdate();
			}
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/
		log.info("无效的参数");
		return 0;
	}


	/**
	 * 插入
	 * @param table
	 * @param maps
	 * @return
	 * @throws SQLException
	 */
	public int insert(String table,Map<String,Object> maps) throws SQLException {

		//try {
			if(!maps.isEmpty()) {
				String f = "";
				String v = "";

				List<Object> values = new ArrayList<>();
				for(String k:maps.keySet()) {
					f += ","+k;
					v += ",?";
					values.add(maps.get(k));
				}

				String insert = "insert into "+table+ "("+f.substring(1)+") values("+v.substring(1)+")";
				PreparedStatement st = con.prepareStatement(insert);
				int i=1;

				for(Object o:values) {
					st.setObject(i++, o);
				}

				log.info("sql:"+insert);
				log.info("param:"+values);

				return st.executeUpdate();
			}
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/
		log.info("无效的参数");
		return 0;
	}


}
