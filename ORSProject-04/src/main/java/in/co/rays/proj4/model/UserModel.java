package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.co.rays.proj4.util.JDBCDataSource;

public class UserModel {
	
	
	
	
	
	public Long getNextPk() throws SQLException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt=conn.prepareStatement("select max(id) from st_user");
			ResultSet rs =  pstmt.executeQuery();
			while(rs.next()) {
				pk = rs.getLong(1)+1l;
			}
			rs.close();
			pstmt.close();
			
		}catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		}
		finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	
}
