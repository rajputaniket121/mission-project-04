package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.ArtGalleryBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class ArtGalleryModel {

    public Long addArtwork(ArtGalleryBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        Long pk = 0l;
        ArtGalleryBean exist = findByArtworkTitle(bean.getArtworkTitle());
        if (exist != null) {
            throw new DuplicateRecordException("Artwork Title already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            pk = getNextPk();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_art_gallery values(?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getArtworkTitle());
            pstmt.setString(3, bean.getArtistName());
            pstmt.setDate(4, new java.sql.Date(bean.getExhibitionDate().getTime()));
            pstmt.setLong(5, bean.getPrice());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());
            int i = pstmt.executeUpdate();
            System.out.println("New Artwork Added " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    public void updateArtwork(ArtGalleryBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        ArtGalleryBean exist = findByArtworkTitle(bean.getArtworkTitle());
        if (exist != null && exist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Artwork Title already Exist");
        }
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_art_gallery set artwork_title = ?, artist_name = ?, exhibition_date = ?, price = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getArtworkTitle());
            pstmt.setString(2, bean.getArtistName());
            pstmt.setDate(3, new java.sql.Date(bean.getExhibitionDate().getTime()));
            pstmt.setLong(4, bean.getPrice());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            pstmt.setLong(9, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Artwork updated " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in Update Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public void deleteArtwork(Long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_art_gallery where id = ?");
            pstmt.setLong(1, id);
            int i = pstmt.executeUpdate();
            System.out.println("Artwork deleted " + i);
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in delete Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public ArtGalleryBean findByPk(Long id) throws ApplicationException {
        Connection conn = null;
        ArtGalleryBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_art_gallery where id = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ArtGalleryBean();
                bean.setId(rs.getLong(1));
                bean.setArtworkTitle(rs.getString(2));
                bean.setArtistName(rs.getString(3));
                bean.setExhibitionDate(rs.getDate(4));
                bean.setPrice(rs.getLong(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in FindByPk Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public ArtGalleryBean findByArtworkTitle(String artworkTitle) throws ApplicationException {
        Connection conn = null;
        ArtGalleryBean bean = null;
        StringBuffer sql = new StringBuffer("select * from st_art_gallery where artwork_title = ?");
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, artworkTitle);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ArtGalleryBean();
                bean.setId(rs.getLong(1));
                bean.setArtworkTitle(rs.getString(2));
                bean.setArtistName(rs.getString(3));
                bean.setExhibitionDate(rs.getDate(4));
                bean.setPrice(rs.getLong(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in findByArtworkTitle Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public List<ArtGalleryBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    public List<ArtGalleryBean> search(ArtGalleryBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuffer sql = new StringBuffer("select * from st_art_gallery where 1 = 1");
        List<ArtGalleryBean> artwork = new ArrayList<ArtGalleryBean>();

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getArtworkTitle() != null && bean.getArtworkTitle().length() > 0) {
                sql.append(" and artwork_title like '" + bean.getArtworkTitle() + "%'");
            }
            if (bean.getArtistName() != null && bean.getArtistName().length() > 0) {
                sql.append(" and artist_name like '" + bean.getArtistName() + "%'");
            }
            if (bean.getExhibitionDate() != null) {
                sql.append(" and exhibition_date like '" + new java.sql.Date(bean.getExhibitionDate().getTime()) + "%'");
            }
            if (bean.getPrice() > 0) {
                sql.append(" and price = " + bean.getPrice());
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
                bean = new ArtGalleryBean();
                bean.setId(rs.getLong(1));
                bean.setArtworkTitle(rs.getString(2));
                bean.setArtistName(rs.getString(3));
                bean.setExhibitionDate(rs.getDate(4));
                bean.setPrice(rs.getLong(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDateTime(rs.getTimestamp(8));
                bean.setModifiedDateTime(rs.getTimestamp(9));
                artwork.add(bean);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in search Artwork");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return artwork;
    }

    public Long getNextPk() throws DatabaseException {
        Connection conn = null;
        Long pk = 0l;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_art_gallery");
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