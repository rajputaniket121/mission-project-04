package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class PatientModel {

	public Long addPatient(PatientBean bean) throws ApplicationException,DuplicateRecordException {
		Connection conn = null;
		Long pk = 0l;
		PatientBean exist = findByName(bean.getName());
		if(exist!=null) {
			throw new DuplicateRecordException("Patient already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			pk = getNextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_patient values(?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setDate(3, new java.sql.Date(bean.getDateOfVisit().getTime()));
			pstmt.setString(4, bean.getMobile());
			pstmt.setString(5, bean.getDecease());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDateTime());
			pstmt.setTimestamp(9, bean.getModifiedDateTime());
			int i = pstmt.executeUpdate();
			System.out.println("New Patient Added " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in add Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void updatePatient(PatientBean bean) throws ApplicationException,DuplicateRecordException{
		Connection conn = null;
		PatientBean exist = findByName(bean.getName());
		if(exist!=null && exist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Patient already Exist");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_patient set name = ?, date_of_visit = ?, mobile = ?, decease = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setDate(2, new java.sql.Date(bean.getDateOfVisit().getTime()));
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getDecease());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDateTime());
			pstmt.setTimestamp(8, bean.getModifiedDateTime());
			pstmt.setLong(9, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Patient updated " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in Update Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void deletePatient(Long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_patient where id = ?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Patient deleted " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in delete Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public PatientBean findByPk(Long id) throws ApplicationException {
		Connection conn = null;
		PatientBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_patient where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in FindByPk Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public PatientBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		PatientBean bean = null;
		StringBuffer sql = new StringBuffer("select * from st_patient where name = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in findByName Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<PatientBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<PatientBean> search(PatientBean bean, int pageNo, int pageSize) throws ApplicationException {
		Connection conn = null;
		StringBuffer sql = new StringBuffer("select * from st_patient where 1 = 1");
		List<PatientBean> patient = new ArrayList<PatientBean>();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = "+bean.getId());
			}
			if (bean.getName()!= null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getDateOfVisit()!= null && bean.getDateOfVisit().getTime() > 0) {
				sql.append(" and date_of_visit like '" + bean.getDateOfVisit() + "%'");
			}
			if (bean.getMobile()!= null && bean.getMobile().length() > 0) {
				sql.append(" and mobile = " + bean.getMobile());
			}
			if (bean.getDecease()!= null && bean.getDecease().length() > 0) {
				sql.append(" and decease like '" + bean.getDecease() + "%'");
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
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDateTime(rs.getTimestamp(8));
				bean.setModifiedDateTime(rs.getTimestamp(9));
				patient.add(bean);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return patient;
	}

	public Long getNextPk() throws DatabaseException {
		Connection conn = null;
		Long pk = 0l;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_patient");
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
		return pk + 1l;
	}
}
