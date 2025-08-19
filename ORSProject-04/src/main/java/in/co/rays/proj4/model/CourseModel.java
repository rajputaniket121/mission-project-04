package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.util.JDBCDataSource;

public class CourseModel {
	
	public Long addCourse(CourseBean bean) throws SQLException {
		Connection conn = null;
		Long pk = 0l;
		CourseBean exist = findByName(bean.getName());
		if(exist!=null) {
			throw new RuntimeException("Course already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_course values(?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getDuration());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDateTime());
			pstmt.setTimestamp(8, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New Course Added " + i);
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

	public void updateCourse(CourseBean bean) throws SQLException {
		Connection conn = null;
		CourseBean exist = findByName(bean.getName());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new RuntimeException("Course already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_course set name = ?, description = ?, duration = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getDuration());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDateTime());
			pstmt.setTimestamp(7, bean.getModifiedDateTime());
			pstmt.setLong(8, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Course updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteCourse(Long id) throws SQLException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_course where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Course deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public CourseBean findByPk(Long id) throws SQLException {
		Connection conn = null;
		CourseBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_course where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setDuration(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
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

	public CourseBean findByName(String name) throws SQLException {
		Connection conn = null;
		CourseBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_course where name = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setDuration(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
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

	public List<CourseBean> list() throws SQLException {
		return search(null, 0, 0);
	}

	public List<CourseBean> search(CourseBean bean, int pageNo, int pageSize) throws SQLException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_course where 1 = 1");
		List<CourseBean> courseList = new ArrayList<CourseBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getName()!= null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getDescription()!= null && bean.getDescription().length() > 0) {
				sql.append(" and description like '" + bean.getDescription() + "%'");
			}
			if (bean.getDuration()!= null && bean.getDuration().length() > 0) {
				sql.append(" and duration like '" + bean.getDuration() + "%'");
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
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setDuration(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				bean.setModifiedDateTime(rs.getTimestamp(8));
				courseList.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return courseList;
	}

	public Long getNextPk() throws SQLException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_course");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1) + 1l;
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

}
