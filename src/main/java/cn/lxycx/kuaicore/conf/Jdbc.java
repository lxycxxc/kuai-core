package cn.lxycx.kuaicore.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

@Component
@Data
public class Jdbc {

	@Autowired
	private Environment env;

	//将根据区划和年份已经匹配到的DataSource存入map
	private static Set<String> provinceMap;

	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.driver-class-name}")
	private  String driver;
	private static Connection conn;

	@Autowired
	public void init(){

		try {
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取连接对象
	 * @throws SQLException
	 * */
	public Connection getConn() throws SQLException{
		//Context c = new InitialContext();
		//DataSource ds = c;
		if(conn==null||conn.isClosed()){
			 conn = DriverManager.getConnection(url, username, password);
		}
		return conn;
	}

	/**
	 * 获取连接对象
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * */
	public static Connection getConn(String url, String username, String password,String driver) throws SQLException, ClassNotFoundException{
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}



	/**
	 * 关闭链接
	 * @param conn
	 * 创建时间：2017年9月30日
	 *
	 */
	public static void closeConn(Connection conn){
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (Exception e) {
			//closeConn(conn);
		}
	}


	/**
	 * 更新操作
	 * @param sql
	 * @param param
	 * @return
	 */
	public boolean update(String sql,Object...param){



		return true;
	}

}
