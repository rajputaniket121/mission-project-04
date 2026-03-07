package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.rays.proj4.bean.ExpenseReportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class ExpenseReportModel {

    public Long addExpense(ExpenseReportBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        ExpenseReportBean exist = findBySubmittedBy(bean.getSubmittedBy(), bean.getSubmittedDate());
        if (exist != null) {
            throw new DuplicateRecordException("Expense Report for this submitter on this date already exists");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_expense_report values(?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getSubmittedBy());
            pstmt.setDate(3, new java.sql.Date(bean.getSubmittedDate().getTime()));
            pstmt.setDouble(4, bean.getTotalAmount());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Expense Report Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateExpense(ExpenseReportBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        ExpenseReportBean exist = findBySubmittedBy(bean.getSubmittedBy(), bean.getSubmittedDate());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Expense Report for this submitter on this date already exists");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_expense_report set submitted_by = ?, submitted_date = ?, total_amount = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getSubmittedBy());
            pstmt.setDate(2, new java.sql.Date(bean.getSubmittedDate().getTime()));
            pstmt.setDouble(3, bean.getTotalAmount());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDateTime());
            pstmt.setTimestamp(7, bean.getModifiedDateTime());
            pstmt.setLong(8, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Expense Report updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteExpense(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_expense_report where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Expense Report deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public ExpenseReportBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        ExpenseReportBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_expense_report where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ExpenseReportBean();
                bean.setId(rs.getLong(1));
                bean.setSubmittedBy(rs.getString(2));
                bean.setSubmittedDate(rs.getDate(3));
                bean.setTotalAmount(rs.getLong(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public ExpenseReportBean findBySubmittedBy(String submittedBy, Date submittedDate) throws ApplicationException {
        Connection conn = null;
        ExpenseReportBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_expense_report where submitted_by = ? and submitted_date = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, submittedBy);
            pstmt.setDate(2, new java.sql.Date(submittedDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ExpenseReportBean();
                bean.setId(rs.getLong(1));
                bean.setSubmittedBy(rs.getString(2));
                bean.setSubmittedDate(rs.getDate(3));
                bean.setTotalAmount(rs.getLong(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findBySubmittedBy Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<ExpenseReportBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<ExpenseReportBean> search(ExpenseReportBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_expense_report where 1 = 1");
        List<ExpenseReportBean> expense = new ArrayList<ExpenseReportBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getSubmittedBy() != null && bean.getSubmittedBy().length() > 0) {
                sql.append(" and submitted_by like '" + bean.getSubmittedBy() + "%'");
            }
            if (bean.getSubmittedDate() != null) {
                sql.append(" and submitted_date like '" + new java.sql.Date(bean.getSubmittedDate().getTime()) + "%'");
            }
            if (bean.getTotalAmount() > 0) {
                sql.append(" and total_amount = " + bean.getTotalAmount());
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
                bean = new ExpenseReportBean();
                bean.setId(rs.getLong(1));
                bean.setSubmittedBy(rs.getString(2));
                bean.setSubmittedDate(rs.getDate(3));
                bean.setTotalAmount(rs.getLong(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
                expense.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Expense Report");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return expense;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_expense_report");
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