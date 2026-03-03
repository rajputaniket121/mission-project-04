package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.LockerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class LockerModel {

    public Long addLocker(LockerBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        LockerBean exist = findByLockerNumber(bean.getLockerNumber());
        if (exist != null) {
            throw new DuplicateRecordException("Locker Number already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_locker values(?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getLockerNumber());
            pstmt.setString(3, bean.getLockerType());
            pstmt.setDouble(4, bean.getAnnualFee());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Locker Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateLocker(LockerBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        LockerBean exist = findByLockerNumber(bean.getLockerNumber());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Locker Number already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_locker set locker_number = ?, locker_type = ?, annual_fee = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getLockerNumber());
            pstmt.setString(2, bean.getLockerType());
            pstmt.setDouble(3, bean.getAnnualFee());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDateTime());
            pstmt.setTimestamp(7, bean.getModifiedDateTime());
            pstmt.setLong(8, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Locker updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteLocker(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_locker where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Locker deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public LockerBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        LockerBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_locker where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new LockerBean();
                bean.setId(rs.getLong(1));
                bean.setLockerNumber(rs.getString(2));
                bean.setLockerType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public LockerBean findByLockerNumber(String lockerNumber) throws ApplicationException {
        Connection conn = null;
        LockerBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_locker where locker_number = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, lockerNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new LockerBean();
                bean.setId(rs.getLong(1));
                bean.setLockerNumber(rs.getString(2));
                bean.setLockerType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByLockerNumber Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<LockerBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<LockerBean> search(LockerBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_locker where 1 = 1");
        List<LockerBean> locker = new ArrayList<LockerBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getLockerNumber() != null && bean.getLockerNumber().length() > 0) {
                sql.append(" and locker_number like '" + bean.getLockerNumber() + "%'");
            }
            if (bean.getLockerType() != null && bean.getLockerType().length() > 0) {
                sql.append(" and locker_type like '" + bean.getLockerType() + "%'");
            }
            if (bean.getAnnualFee() != null && bean.getAnnualFee() > 0) {
                sql.append(" and annual_fee = " + bean.getAnnualFee());
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
                bean = new LockerBean();
                bean.setId(rs.getLong(1));
                bean.setLockerNumber(rs.getString(2));
                bean.setLockerType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
                locker.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Locker");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return locker;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_locker");
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