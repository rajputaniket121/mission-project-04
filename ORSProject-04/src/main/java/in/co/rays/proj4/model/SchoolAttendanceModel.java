package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.rays.proj4.bean.SchoolAttendanceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class SchoolAttendanceModel {

    public Long addAttendance(SchoolAttendanceBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        SchoolAttendanceBean exist = findByStudentName(bean.getStudentName(), bean.getAttendanceDate());
        if (exist != null) {
            throw new DuplicateRecordException("Attendance for this student on this date already exists");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_school_attendance values(?,?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getStudentName());
            pstmt.setString(3, bean.getClassName());
            pstmt.setDate(4, new java.sql.Date(bean.getAttendanceDate().getTime()));
            pstmt.setString(5, bean.getAttendanceStatus());
            pstmt.setString(6, bean.getRemarks());
            pstmt.setString(7, bean.getCreatedBy());
            pstmt.setString(8, bean.getModifiedBy());
            pstmt.setTimestamp(9, bean.getCreatedDateTime());
            pstmt.setTimestamp(10, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Attendance Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateAttendance(SchoolAttendanceBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        SchoolAttendanceBean exist = findByStudentName(bean.getStudentName(), bean.getAttendanceDate());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Attendance for this student on this date already exists");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_school_attendance set student_name = ?, class_name = ?, attendance_date = ?, attendance_status = ?, remarks = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getStudentName());
            pstmt.setString(2, bean.getClassName());
            pstmt.setDate(3, new java.sql.Date(bean.getAttendanceDate().getTime()));
            pstmt.setString(4, bean.getAttendanceStatus());
            pstmt.setString(5, bean.getRemarks());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());
            pstmt.setLong(10, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Attendance updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteAttendance(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_school_attendance where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Attendance deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public SchoolAttendanceBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        SchoolAttendanceBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_school_attendance where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new SchoolAttendanceBean();
                bean.setId(rs.getLong(1));
                bean.setStudentName(rs.getString(2));
                bean.setClassName(rs.getString(3));
                bean.setAttendanceDate(rs.getDate(4));
                bean.setAttendanceStatus(rs.getString(5));
                bean.setRemarks(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDateTime(rs.getTimestamp(9));
                bean.setModifiedDateTime(rs.getTimestamp(10));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public SchoolAttendanceBean findByStudentName(String studentName, Date attendanceDate) throws ApplicationException {
        Connection conn = null;
        SchoolAttendanceBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_school_attendance where student_name = ? and attendance_date = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, studentName);
            pstmt.setDate(2, new java.sql.Date(attendanceDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new SchoolAttendanceBean();
                bean.setId(rs.getLong(1));
                bean.setStudentName(rs.getString(2));
                bean.setClassName(rs.getString(3));
                bean.setAttendanceDate(rs.getDate(4));
                bean.setAttendanceStatus(rs.getString(5));
                bean.setRemarks(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDateTime(rs.getTimestamp(9));
                bean.setModifiedDateTime(rs.getTimestamp(10));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByStudentName Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<SchoolAttendanceBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<SchoolAttendanceBean> search(SchoolAttendanceBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_school_attendance where 1 = 1");
        List<SchoolAttendanceBean> attendance = new ArrayList<SchoolAttendanceBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getStudentName() != null && bean.getStudentName().length() > 0) {
                sql.append(" and student_name like '" + bean.getStudentName() + "%'");
            }
            if (bean.getClassName() != null && bean.getClassName().length() > 0) {
                sql.append(" and class_name like '" + bean.getClassName() + "%'");
            }
            if (bean.getAttendanceDate() != null) {
                sql.append(" and attendance_date like '" + new java.sql.Date(bean.getAttendanceDate().getTime()) + "%'");
            }
            if (bean.getAttendanceStatus() != null && bean.getAttendanceStatus().length() > 0) {
                sql.append(" and attendance_status like '" + bean.getAttendanceStatus() + "%'");
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
                bean = new SchoolAttendanceBean();
                bean.setId(rs.getLong(1));
                bean.setStudentName(rs.getString(2));
                bean.setClassName(rs.getString(3));
                bean.setAttendanceDate(rs.getDate(4));
                bean.setAttendanceStatus(rs.getString(5));
                bean.setRemarks(rs.getString(6));
                bean.setCreatedBy(rs.getString(7));
                bean.setModifiedBy(rs.getString(8));
                bean.setCreatedDateTime(rs.getTimestamp(9));
                bean.setModifiedDateTime(rs.getTimestamp(10));
                attendance.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Attendance");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return attendance;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_school_attendance");
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