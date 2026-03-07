package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.InvestorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class InvestorModel {

    public Long addInvestor(InvestorBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        InvestorBean exist = findByInvestorName(bean.getInvestorName());
        if (exist != null) {
            throw new DuplicateRecordException("Investor Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_investor values(?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getInvestorName());
            pstmt.setLong(3, bean.getInvestmentAmount());
            pstmt.setString(4, bean.getInvestmentType());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Investor Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateInvestor(InvestorBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        InvestorBean exist = findByInvestorName(bean.getInvestorName());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Investor Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_investor set investor_name = ?, investment_amount = ?, investment_type = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getInvestorName());
            pstmt.setLong(2, bean.getInvestmentAmount());
            pstmt.setString(3, bean.getInvestmentType());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDateTime());
            pstmt.setTimestamp(7, bean.getModifiedDateTime());
            pstmt.setLong(8, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Investor updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteInvestor(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_investor where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Investor deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public InvestorBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        InvestorBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_investor where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new InvestorBean();
                bean.setId(rs.getLong(1));
                bean.setInvestorName(rs.getString(2));
                bean.setInvestmentAmount(rs.getLong(3));
                bean.setInvestmentType(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public InvestorBean findByInvestorName(String investorName) throws ApplicationException {
        Connection conn = null;
        InvestorBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_investor where investor_name = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, investorName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new InvestorBean();
                bean.setId(rs.getLong(1));
                bean.setInvestorName(rs.getString(2));
                bean.setInvestmentAmount(rs.getLong(3));
                bean.setInvestmentType(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByInvestorName Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<InvestorBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<InvestorBean> search(InvestorBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_investor where 1 = 1");
        List<InvestorBean> investor = new ArrayList<InvestorBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getInvestorName() != null && bean.getInvestorName().length() > 0) {
                sql.append(" and investor_name like '" + bean.getInvestorName() + "%'");
            }
            if (bean.getInvestmentAmount() > 0) {
                sql.append(" and investment_amount = " + bean.getInvestmentAmount());
            }
            if (bean.getInvestmentType() != null && bean.getInvestmentType().length() > 0) {
                sql.append(" and investment_type like '" + bean.getInvestmentType() + "%'");
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
                bean = new InvestorBean();
                bean.setId(rs.getLong(1));
                bean.setInvestorName(rs.getString(2));
                bean.setInvestmentAmount(rs.getLong(3));
                bean.setInvestmentType(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
                investor.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Investor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return investor;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_investor");
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