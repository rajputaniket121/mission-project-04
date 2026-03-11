package in.co.rays.proj4.bean;

import java.util.Date;

public class ArtGalleryBean extends BaseBean {
    private String artworkTitle;
    private String artistName;
    private Date exhibitionDate;
    private long price;

    public String getArtworkTitle() {
        return artworkTitle;
    }

    public void setArtworkTitle(String artworkTitle) {
        this.artworkTitle = artworkTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Date getExhibitionDate() {
        return exhibitionDate;
    }

    public void setExhibitionDate(Date exhibitionDate) {
        this.exhibitionDate = exhibitionDate;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return artworkTitle + " - " + artistName;
    }

    @Override
    public String toString() {
        return "ArtGalleryBean [artworkTitle=" + artworkTitle + ", artistName=" + artistName + ", exhibitionDate="
                + exhibitionDate + ", price=" + price + "]";
    }
}