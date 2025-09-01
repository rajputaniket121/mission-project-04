package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class SubjectModel {
	
	public Long addSubject(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		Long pk = 0l;
		SubjectBean exist = findByName(bean.getName());
		if(exist!=null) {
			throw new DuplicateRecordException("Subject already Exist");
		}
		CourseModel model = new CourseModel();
		CourseBean courseBean =  model.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_subject values(?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setLong(3, bean.getCourseId());
			pstmt.setString(4, bean.getCourseName());
			pstmt.setString(5, bean.getDescription());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDateTime());
			pstmt.setTimestamp(9, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New Subject Added " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Add Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updateSubject(SubjectBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		SubjectBean exist = findByName(bean.getName());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Subject already Exist");
		}
		CourseModel model = new CourseModel();
		CourseBean courseBean =  model.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_subject set name = ?, course_id = ?, course_name = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setLong(2, bean.getCourseId());
			pstmt.setString(3, bean.getCourseName());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDateTime());
			pstmt.setTimestamp(8, bean.getModifiedDateTime());
			pstmt.setLong(9, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Subject updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Update Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteSubject(Long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_subject where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Subject deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in delete Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public SubjectBean findByPk(Long id) throws ApplicationException {
		Connection conn = null;
		SubjectBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_subject where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in findByPk Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public SubjectBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		SubjectBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_subject where name = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in findByName Subject");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<SubjectBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<SubjectBean> search(SubjectBean bean, int pageNo, int pageSize) throws  ApplicationException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_subject where 1 = 1");
		List<SubjectBean> subjectList = new ArrayList<SubjectBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getName()!= null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getCourseId()!= null && bean.getCourseId() > 0) {
				sql.append(" and course_id like '" + bean.getCourseId() + "%'");
			}
			if (bean.getCourseName()!= null && bean.getCourseName().length() > 0) {
				sql.append(" and course_name like '" + bean.getCourseName() + "%'");
			}
			if (bean.getDescription()!= null && bean.getDescription().length() > 0) {
				sql.append(" and description like '" + bean.getDescription() + "%'");
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
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
				subjectList.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return subjectList;
	}

	public Long getNextPk() throws  DatabaseException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_subject");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception In Getting pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk  + 1l;
	}

}
