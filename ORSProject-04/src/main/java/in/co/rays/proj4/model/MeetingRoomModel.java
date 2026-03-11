package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.MeetingRoomBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class MeetingRoomModel {

    public Long addMeetingRoom(MeetingRoomBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        MeetingRoomBean exist = findByRoomCode(bean.getRoomCode());
        if (exist != null) {
            throw new DuplicateRecordException("Room Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_meeting_room values(?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getRoomCode());
            pstmt.setString(3, bean.getRoomName());
            pstmt.setInt(4, bean.getCapacity());
            pstmt.setString(5, bean.getRoomStatus());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Meeting Room Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateMeetingRoom(MeetingRoomBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        MeetingRoomBean exist = findByRoomCode(bean.getRoomCode());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Room Code already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_meeting_room set room_code = ?, room_name = ?, capacity = ?, room_status = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getRoomCode());
            pstmt.setString(2, bean.getRoomName());
            pstmt.setInt(3, bean.getCapacity());
            pstmt.setString(4, bean.getRoomStatus());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            pstmt.setLong(9, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Meeting Room updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteMeetingRoom(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_meeting_room where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Meeting Room deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public MeetingRoomBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        MeetingRoomBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_meeting_room where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new MeetingRoomBean();
                bean.setId(rs.getLong(1));
                bean.setRoomCode(rs.getString(2));
                bean.setRoomName(rs.getString(3));
                bean.setCapacity(rs.getInt(4));
                bean.setRoomStatus(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public MeetingRoomBean findByRoomCode(String roomCode) throws ApplicationException {
        Connection conn = null;
        MeetingRoomBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_meeting_room where room_code = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, roomCode);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new MeetingRoomBean();
                bean.setId(rs.getLong(1));
                bean.setRoomCode(rs.getString(2));
                bean.setRoomName(rs.getString(3));
                bean.setCapacity(rs.getInt(4));
                bean.setRoomStatus(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByRoomCode Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<MeetingRoomBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<MeetingRoomBean> search(MeetingRoomBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_meeting_room where 1 = 1");
        List<MeetingRoomBean> meetingRoom = new ArrayList<MeetingRoomBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getRoomCode() != null && bean.getRoomCode().length() > 0) {
                sql.append(" and room_code like '" + bean.getRoomCode() + "%'");
            }
            if (bean.getRoomName() != null && bean.getRoomName().length() > 0) {
                sql.append(" and room_name like '" + bean.getRoomName() + "%'");
            }
            if (bean.getCapacity() != null && bean.getCapacity() > 0) {
                sql.append(" and capacity = " + bean.getCapacity());
            }
            if (bean.getRoomStatus() != null && bean.getRoomStatus().length() > 0) {
                sql.append(" and room_status like '" + bean.getRoomStatus() + "%'");
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
                bean = new MeetingRoomBean();
                bean.setId(rs.getLong(1));
                bean.setRoomCode(rs.getString(2));
                bean.setRoomName(rs.getString(3));
                bean.setCapacity(rs.getInt(4));
                bean.setRoomStatus(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
                meetingRoom.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Meeting Room");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return meetingRoom;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_meeting_room");
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