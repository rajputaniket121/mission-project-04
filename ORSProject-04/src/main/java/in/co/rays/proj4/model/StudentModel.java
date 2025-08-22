package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class StudentModel {
	
	public Long addStudent(StudentBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		Long pk = 0l;
		
		CollegeModel collegeModel = new CollegeModel();
		CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());
		
		StudentBean exist = findByEmail(bean.getEmail());
		if(exist!=null) {
			throw new DuplicateRecordException("Email Id already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_student values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getGender());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDateTime());
			pstmt.setTimestamp(13, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New Student Added " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in add Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updateStudent(StudentBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		StudentBean exist = findByEmail(bean.getEmail());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Email Id already Exist");
		}
		CollegeModel collegeModel = new CollegeModel();
		CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());
		
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_student set first_name = ?, last_name = ? , dob = ? , gender = ? , mobile_no = ? , email = ? , college_id = ?, college_name = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getGender());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getCollegeId());
			pstmt.setString(8, bean.getCollegeName());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDateTime());
			pstmt.setTimestamp(12, bean.getModifiedDateTime());
			pstmt.setLong(13, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Student updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Update Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteStudent(Long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_student where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Student deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in delete Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public StudentBean findByPk(Long id) throws ApplicationException {
		Connection conn = null;
		StudentBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_student where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StudentBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));;
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));;
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));;
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDateTime(rs.getTimestamp(12));
				bean.setModifiedDateTime(rs.getTimestamp(13));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in FindByPk Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public StudentBean findByEmail(String email) throws ApplicationException {
		Connection conn = null;
		StudentBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_student where email = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StudentBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));;
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));;
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));;
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDateTime(rs.getTimestamp(12));
				bean.setModifiedDateTime(rs.getTimestamp(13));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in FindByEmail Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<StudentBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<StudentBean> search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_student where 1 = 1");
		List<StudentBean> studentList = new ArrayList<StudentBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getFirstName()!= null && bean.getFirstName().length() > 0) {
				sql.append(" and first_name like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName()!= null && bean.getLastName().length() > 0) {
				sql.append(" and last_name like '" + bean.getLastName() + "%'");
			}
			if (bean.getDob()!= null) {
				sql.append(" and dob like '" + bean.getDob() + "%'");
			}
			if (bean.getGender()!= null && bean.getGender().length() > 0) {
				sql.append(" and gender like '" + bean.getGender() + "%'");
			}
			if (bean.getMobileNo()!= null && bean.getMobileNo().length() > 0) {
				sql.append(" and mobile_no like '" + bean.getMobileNo() + "%'");
			}
			if (bean.getEmail()!= null && bean.getEmail().length() > 0) {
				sql.append(" and email like '" + bean.getEmail() + "%'");
			}
			if (bean.getCollegeId()!= null && bean.getCollegeId() > 0) {
				sql.append(" and college_id like '" + bean.getCollegeId() + "%'");
			}
			if (bean.getCollegeName()!= null && bean.getCollegeName().length() > 0) {
				sql.append(" and college_name like '" + bean.getCollegeName() + "%'");
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
				bean = new StudentBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));;
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));;
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));;
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDateTime(rs.getTimestamp(12));
				bean.setModifiedDateTime(rs.getTimestamp(13));
				studentList.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Student");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return studentList;
	}
	
	public Long getNextPk() throws DatabaseException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt=conn.prepareStatement("select max(id) from st_student");
			ResultSet rs =  pstmt.executeQuery();
			while(rs.next()) {
				pk = rs.getLong(1);
			}
			rs.close();
			pstmt.close();
			
		}catch (Exception e) {
			throw new DatabaseException("Exception : Exception In Getting pk");
		}
		finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk+1;
	}

}
