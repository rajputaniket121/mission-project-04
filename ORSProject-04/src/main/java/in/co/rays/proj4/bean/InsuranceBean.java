package in.co.rays.proj4.bean;

import java.util.Date;

public class InsuranceBean extends BaseBean {
    private String insuranceCode;
    private String carName;
    private Date expiryDate;
    private String providerName;

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return insuranceCode + " - " + carName;
    }

    @Override
    public String toString() {
        return "InsuranceBean [insuranceCode=" + insuranceCode + ", carName=" + carName + ", expiryDate=" + expiryDate
                + ", providerName=" + providerName + "]";
    }
}