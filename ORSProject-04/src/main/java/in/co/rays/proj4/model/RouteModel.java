package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.RouteBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * JDBC Implementation of RouteModel
 */
public class RouteModel {
    
    /**
     * Add a Route
     * 
     * @param bean
     * @return
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public Long addRoute(RouteBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        RouteBean exist = findByRouteCode(bean.getRouteCode());
        if (exist != null) {
            throw new DuplicateRecordException("Route Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_route values(?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getRouteCode());
            pstmt.setString(3, bean.getSource());
            pstmt.setString(4, bean.getDestination());
            pstmt.setDouble(5, bean.getDistance());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Route Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Update a Route
     * 
     * @param bean
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public void updateRoute(RouteBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        RouteBean exist = findByRouteCode(bean.getRouteCode());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Route Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_route set route_code = ?, source = ?, destination = ?, distance = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getRouteCode());
            pstmt.setString(2, bean.getSource());
            pstmt.setString(3, bean.getDestination());
            pstmt.setDouble(4, bean.getDistance());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            pstmt.setLong(9, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Route updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Delete a Route
     * 
     * @param id
     * @throws ApplicationException
     */
    public void deleteRoute(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_route where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Route deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Find Route by PK
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    public RouteBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        RouteBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_route where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RouteBean();
                bean.setId(rs.getLong(1));
                bean.setRouteCode(rs.getString(2));
                bean.setSource(rs.getString(3));
                bean.setDestination(rs.getString(4));
                bean.setDistance(rs.getDouble(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Find Route by Route Code
     * 
     * @param routeCode
     * @return
     * @throws ApplicationException
     */
    public RouteBean findByRouteCode(String routeCode) throws ApplicationException {
        Connection conn = null;
        RouteBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_route where route_code = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, routeCode);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RouteBean();
                bean.setId(rs.getLong(1));
                bean.setRouteCode(rs.getString(2));
                bean.setSource(rs.getString(3));
                bean.setDestination(rs.getString(4));
                bean.setDistance(rs.getDouble(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByRouteCode Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Get List of Route
     * 
     * @return
     * @throws ApplicationException
     */
    public List<RouteBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Search Route with pagination
     * 
     * @param bean
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApplicationException
     */
    public List<RouteBean> search(RouteBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_route where 1 = 1");
        List<RouteBean> routeList = new ArrayList<RouteBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getRouteCode() != null && bean.getRouteCode().length() > 0) {
                sql.append(" and route_code like '" + bean.getRouteCode() + "%'");
            }
            if (bean.getSource() != null && bean.getSource().length() > 0) {
                sql.append(" and source like '" + bean.getSource() + "%'");
            }
            if (bean.getDestination() != null && bean.getDestination().length() > 0) {
                sql.append(" and destination like '" + bean.getDestination() + "%'");
            }
            if (bean.getDistance() > 0) {
                sql.append(" and distance = " + bean.getDistance());
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
                bean = new RouteBean();
                bean.setId(rs.getLong(1));
                bean.setRouteCode(rs.getString(2));
                bean.setSource(rs.getString(3));
                bean.setDestination(rs.getString(4));
                bean.setDistance(rs.getDouble(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
                routeList.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Route");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return routeList;
    }

    /**
     * Get Next Primary Key
     * 
     * @return
     * @throws DatabaseException
     */
    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_route");
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