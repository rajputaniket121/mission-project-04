package in.co.rays.proj4.bean;

public class LockerBean extends BaseBean {
    private String lockerNumber;
    private String lockerType;
    private Double annualFee;

    public String getLockerNumber() {
        return lockerNumber;
    }

    public void setLockerNumber(String lockerNumber) {
        this.lockerNumber = lockerNumber;
    }

    public String getLockerType() {
        return lockerType;
    }

    public void setLockerType(String lockerType) {
        this.lockerType = lockerType;
    }

    public Double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(Double annualFee) {
        this.annualFee = annualFee;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return lockerNumber + " - " + lockerType;
    }

    @Override
    public String toString() {
        return "LockerBean [lockerNumber=" + lockerNumber + ", lockerType=" + lockerType + ", annualFee=" + annualFee
                + "]";
    }
}