package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.NGOBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class NGOModel {

    public Long addNGO(NGOBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        NGOBean exist = findByNgoName(bean.getNgoName());
        if (exist != null) {
            throw new DuplicateRecordException("NGO Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_ngo values(?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getNgoName());
            pstmt.setString(3, bean.getCity());
            pstmt.setString(4, bean.getFocusArea());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New NGO Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateNGO(NGOBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        NGOBean exist = findByNgoName(bean.getNgoName());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("NGO Name already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_ngo set ngo_name = ?, city = ?, focus_area = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getNgoName());
            pstmt.setString(2, bean.getCity());
            pstmt.setString(3, bean.getFocusArea());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDateTime());
            pstmt.setTimestamp(7, bean.getModifiedDateTime());
            pstmt.setLong(8, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("NGO updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteNGO(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_ngo where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("NGO deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public NGOBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        NGOBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_ngo where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new NGOBean();
                bean.setId(rs.getLong(1));
                bean.setNgoName(rs.getString(2));
                bean.setCity(rs.getString(3));
                bean.setFocusArea(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public NGOBean findByNgoName(String ngoName) throws ApplicationException {
        Connection conn = null;
        NGOBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_ngo where ngo_name = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, ngoName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new NGOBean();
                bean.setId(rs.getLong(1));
                bean.setNgoName(rs.getString(2));
                bean.setCity(rs.getString(3));
                bean.setFocusArea(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByNgoName NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<NGOBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<NGOBean> search(NGOBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_ngo where 1 = 1");
        List<NGOBean> ngo = new ArrayList<NGOBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getNgoName() != null && bean.getNgoName().length() > 0) {
                sql.append(" and ngo_name like '" + bean.getNgoName() + "%'");
            }
            if (bean.getCity() != null && bean.getCity().length() > 0) {
                sql.append(" and city like '" + bean.getCity() + "%'");
            }
            if (bean.getFocusArea() != null && bean.getFocusArea().length() > 0) {
                sql.append(" and focus_area like '" + bean.getFocusArea() + "%'");
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
                bean = new NGOBean();
                bean.setId(rs.getLong(1));
                bean.setNgoName(rs.getString(2));
                bean.setCity(rs.getString(3));
                bean.setFocusArea(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDateTime(rs.getTimestamp(7));
                bean.setModifiedDateTime(rs.getTimestamp(8));
                ngo.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search NGO");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return ngo;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_ngo");
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