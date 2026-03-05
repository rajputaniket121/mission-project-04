package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.PortfolioBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class PortfolioModel {

    public Long addPortfolio(PortfolioBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        PortfolioBean exist = findByPortfolioName(bean.getPortfolioName());
        if (exist != null) {
            throw new DuplicateRecordException("Portfolio Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_portfolio values(?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getPortfolioName());
            pstmt.setDouble(3, bean.getTotalValue());
            pstmt.setDate(4, new java.sql.Date(bean.getCreatedDate().getTime()));
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Portfolio Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updatePortfolio(PortfolioBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        PortfolioBean exist = findByPortfolioName(bean.getPortfolioName());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Portfolio Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_portfolio set portfolio_name = ?, total_value = ?, created_date = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getPortfolioName());
            pstmt.setDouble(2, bean.getTotalValue());
            pstmt.setDate(3, new java.sql.Date(bean.getCreatedDate().getTime()));
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDateTime());
            pstmt.setTimestamp(7, bean.getModifiedDateTime());
            pstmt.setLong(8, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Portfolio updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deletePortfolio(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_portfolio where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Portfolio deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public PortfolioBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        PortfolioBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_portfolio where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new PortfolioBean();
                bean.setId(rs.getLong(1));
                bean.setPortfolioName(rs.getString(2));
                bean.setTotalValue(rs.getLong(3));
                bean.setCreatedDate(rs.getDate(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public PortfolioBean findByPortfolioName(String portfolioName) throws ApplicationException {
        Connection conn = null;
        PortfolioBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_portfolio where portfolio_name = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, portfolioName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new PortfolioBean();
                bean.setId(rs.getLong(1));
                bean.setPortfolioName(rs.getString(2));
                bean.setTotalValue(rs.getLong(3));
                bean.setCreatedDate(rs.getDate(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByPortfolioName Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<PortfolioBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<PortfolioBean> search(PortfolioBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_portfolio where 1 = 1");
        List<PortfolioBean> portfolio = new ArrayList<PortfolioBean>();
        

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getPortfolioName() != null && bean.getPortfolioName().length() > 0) {
                sql.append(" and portfolio_name like '" + bean.getPortfolioName() + "%'");
            }
            if (bean.getTotalValue() > 0) {
                sql.append(" and total_value = " + bean.getTotalValue());
            }
            if (bean.getCreatedDate() != null) {
                sql.append(" and created_date like '" + new java.sql.Date(bean.getCreatedDate().getTime()) + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }
        
        System.out.println(sql.toString());
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new PortfolioBean();
                bean.setId(rs.getLong(1));
                bean.setPortfolioName(rs.getString(2));
                bean.setTotalValue(rs.getLong(3));
                bean.setCreatedDate(rs.getDate(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
                portfolio.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Portfolio");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        System.out.println(portfolio);
        return portfolio;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_portfolio");
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