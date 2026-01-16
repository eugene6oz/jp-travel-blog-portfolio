package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

	public static Connection getConnection() {
		
		Connection conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "jsl26";
		String pw = "1234";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, "1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
