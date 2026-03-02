package in.co.rays.proj4.bean;

public class NGOBean extends BaseBean {
    private String ngoName;
    private String city;
    private String focusArea;

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return ngoName + " (" + focusArea + ")";
    }

    @Override
    public String toString() {
        return "NGOBean [ngoName=" + ngoName + ", city=" + city + ", focusArea=" + focusArea + "]";
    }
}