package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.InsuranceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class InsuranceModel {

    public Long addInsurance(InsuranceBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        InsuranceBean exist = findByInsuranceCode(bean.getInsuranceCode());
        if (exist != null) {
            throw new DuplicateRecordException("Insurance Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_insurance values(?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getInsuranceCode());
            pstmt.setString(3, bean.getCarName());
            pstmt.setDate(4, new java.sql.Date(bean.getExpiryDate().getTime()));
            pstmt.setString(5, bean.getProviderName());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Insurance Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateInsurance(InsuranceBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        InsuranceBean exist = findByInsuranceCode(bean.getInsuranceCode());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Insurance Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_insurance set insurance_code = ?, car_name = ?, expiry_date = ?, provider_name = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getInsuranceCode());
            pstmt.setString(2, bean.getCarName());
            pstmt.setDate(3, new java.sql.Date(bean.getExpiryDate().getTime()));
            pstmt.setString(4, bean.getProviderName());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            pstmt.setLong(9, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Insurance updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteInsurance(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_insurance where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Insurance deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public InsuranceBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        InsuranceBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_insurance where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new InsuranceBean();
                bean.setId(rs.getLong(1));
                bean.setInsuranceCode(rs.getString(2));
                bean.setCarName(rs.getString(3));
                bean.setExpiryDate(rs.getDate(4));
                bean.setProviderName(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public InsuranceBean findByInsuranceCode(String insuranceCode) throws ApplicationException {
        Connection conn = null;
        InsuranceBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_insurance where insurance_code = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, insuranceCode);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new InsuranceBean();
                bean.setId(rs.getLong(1));
                bean.setInsuranceCode(rs.getString(2));
                bean.setCarName(rs.getString(3));
                bean.setExpiryDate(rs.getDate(4));
                bean.setProviderName(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByInsuranceCode Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<InsuranceBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<InsuranceBean> search(InsuranceBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_insurance where 1 = 1");
        List<InsuranceBean> insurance = new ArrayList<InsuranceBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getInsuranceCode() != null && bean.getInsuranceCode().length() > 0) {
                sql.append(" and insurance_code like '" + bean.getInsuranceCode() + "%'");
            }
            if (bean.getCarName() != null && bean.getCarName().length() > 0) {
                sql.append(" and car_name like '" + bean.getCarName() + "%'");
            }
            if (bean.getExpiryDate() != null) {
                sql.append(" and expiry_date like '" + new java.sql.Date(bean.getExpiryDate().getTime()) + "%'");
            }
            if (bean.getProviderName() != null && bean.getProviderName().length() > 0) {
                sql.append(" and provider_name like '" + bean.getProviderName() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new InsuranceBean();
                bean.setId(rs.getLong(1));
                bean.setInsuranceCode(rs.getString(2));
                bean.setCarName(rs.getString(3));
                bean.setExpiryDate(rs.getDate(4));
                bean.setProviderName(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
                insurance.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Insurance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return insurance;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_insurance");
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