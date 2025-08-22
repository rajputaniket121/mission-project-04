package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.util.JDBCDataSource;

public class MarksheetModel {
	
	public Long addMarksheet(MarksheetBean bean) throws SQLException {
		Connection conn = null;
		Long pk = 0l;
		MarksheetBean exist = findByRollNO(bean.getRollNo());
		if(exist!=null) {
			throw new RuntimeException("Marksheet already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_marksheet values(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getRollNo());
			pstmt.setLong(3, bean.getStudentId());
			pstmt.setString(4, bean.getName());
			pstmt.setInt(5, bean.getPhysics());
			pstmt.setInt(6, bean.getChemistry());
			pstmt.setInt(7, bean.getMaths());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDateTime());
			pstmt.setTimestamp(11, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New Marksheet Added " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updateMarksheet(MarksheetBean bean) throws SQLException {
		Connection conn = null;
		MarksheetBean exist = findByRollNO(bean.getRollNo());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new RuntimeException("Marksheet already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_marksheet set roll_no = ?, student_id = ? , name = ?, physics = ?, chemistry = ?, maths = ? , created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getRollNo());
			pstmt.setLong(2, bean.getStudentId());
			pstmt.setString(3, bean.getName());
			pstmt.setInt(4, bean.getPhysics());
			pstmt.setInt(5, bean.getChemistry());
			pstmt.setInt(6, bean.getMaths());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDateTime());
			pstmt.setTimestamp(10, bean.getModifiedDateTime());
			pstmt.setLong(11, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Marksheet updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteMarksheet(Long id) throws SQLException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Marksheet deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public MarksheetBean findByPk(Long id) throws SQLException {
		Connection conn = null;
		MarksheetBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_marksheet where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDateTime(rs.getTimestamp(10));
				bean.setModifiedDateTime(rs.getTimestamp(11));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public MarksheetBean findByRollNO(String rollNo) throws SQLException {
		Connection conn = null;
		MarksheetBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_marksheet where roll_no = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rollNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDateTime(rs.getTimestamp(10));
				bean.setModifiedDateTime(rs.getTimestamp(11));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<MarksheetBean> list() throws SQLException {
		return search(null, 0, 0);
	}

	public List<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize) throws SQLException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_marksheet where 1 = 1");
		List<MarksheetBean> marksheetList = new ArrayList<MarksheetBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getRollNo()!= null && bean.getRollNo().length() > 0) {
				sql.append(" and roll_no like '" + bean.getRollNo() + "%'");
			}
			if (bean.getStudentId() > 0) {
				sql.append(" and student_id = " + bean.getStudentId());
			}
			if (bean.getName()!= null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getPhysics() > 0) {
				sql.append(" and physics = " + bean.getPhysics());
			}
			if (bean.getChemistry() > 0) {
				sql.append(" and chemistry = " + bean.getChemistry());
			}
			if (bean.getMaths() > 0) {
				sql.append(" and maths = " + bean.getMaths());
			}
		}
		
		if(pageSize>0) {
			pageNo = (pageNo-1) * pageSize;
			sql.append(" limit "+pageNo+", "+pageSize);
		}

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDateTime(rs.getTimestamp(10));
				bean.setModifiedDateTime(rs.getTimestamp(11));
				marksheetList.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return marksheetList;
	}

	public Long getNextPk() throws DatabaseException  {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_marksheet");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception in Marksheet getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk+1l;
	}

}
