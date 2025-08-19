package in.co.rays.proj4.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class JDBCDataSource {
	private static JDBCDataSource jds = null;
	private static ComboPooledDataSource cpds = null;
	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.proj4.bundle.system");
	
	
	private JDBCDataSource() {
		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(rb.getString("driver"));
			cpds.setJdbcUrl(rb.getString("jdbcurl"));
			cpds.setUser(rb.getString("user"));
			cpds.setPassword(rb.getString("password"));
			cpds.setInitialPoolSize(Integer.parseInt(rb.getString("initialpoolsize")));
			cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireincrement")));
			cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxpoolsize")));	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static JDBCDataSource getInstance() {
		if(jds == null) {
			jds = new JDBCDataSource();
		}
		return jds;
	}
	
	public static Connection getConnection() throws SQLException {
		try {
			getInstance();
			return JDBCDataSource.cpds.getConnection();
		}catch(SQLException exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
	public static void closeConnection(Connection conn) throws SQLException {
		closeConnection(conn, null, null);
	}
	
	public static void closeConnection(Connection conn,Statement stmt) throws SQLException {
		closeConnection(conn, stmt, null);
	}
	
	public static void closeConnection(Connection conn,Statement stmt,ResultSet rs) throws SQLException {
		if(conn!=null) {
			conn.close();
		}
		if(stmt!=null) {
			stmt.close();
		}
		if(rs!=null) {
			rs.close();
		}
	}

}
