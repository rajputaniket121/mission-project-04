package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class CollegeModel {
	
	public Long addCollege(CollegeBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		Long pk = 0l;
		CollegeBean exist = findByName(bean.getName());
		if(exist!=null) {
			throw new DuplicateRecordException("College already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_college values(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDateTime());
			pstmt.setTimestamp(10, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New College Added " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in add College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updateCollege(CollegeBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		CollegeBean exist = findByName(bean.getName());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new DuplicateRecordException("College already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_college set name = ?, address = ?,state = ?, city = ? , phone_no = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDateTime());
			pstmt.setTimestamp(9, bean.getModifiedDateTime());
			pstmt.setLong(10, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("College updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Update College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deleteCollege(Long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("College deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in delete College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public CollegeBean findByPk(Long id) throws ApplicationException {
		Connection conn = null;
		CollegeBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_college where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDateTime(rs.getTimestamp(9));
				bean.setModifiedDateTime(rs.getTimestamp(10));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in FindByPk College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public CollegeBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		CollegeBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_college where name = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDateTime(rs.getTimestamp(9));
				bean.setModifiedDateTime(rs.getTimestamp(10));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in FindByPk College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<CollegeBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<CollegeBean> search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_college where 1 = 1");
		List<CollegeBean> collegeList = new ArrayList<CollegeBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getName()!= null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getAddress()!= null && bean.getAddress().length() > 0) {
				sql.append(" and address like '" + bean.getAddress() + "%'");
			}
			if (bean.getState()!= null && bean.getState().length() > 0) {
				sql.append(" and state like '" + bean.getState() + "%'");
			}
			if (bean.getCity()!= null && bean.getCity().length() > 0) {
				sql.append(" and city like '" + bean.getCity() + "%'");
			}
			if (bean.getPhoneNo()!= null && bean.getPhoneNo().length() > 0) {
				sql.append(" and phone_no like '" + bean.getPhoneNo() + "%'");
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
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDateTime(rs.getTimestamp(9));
				bean.setModifiedDateTime(rs.getTimestamp(10));
				collegeList.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search College");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return collegeList;
	}

	public Long getNextPk() throws DatabaseException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_college");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getLong(1) + 1l;
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception In Getting pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

}
