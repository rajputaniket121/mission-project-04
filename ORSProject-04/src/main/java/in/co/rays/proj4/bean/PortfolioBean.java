package in.co.rays.proj4.bean;

import java.util.Date;

public class PortfolioBean extends BaseBean {
    private String portfolioName;
    private long totalValue;
    private Date createdDate;

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return portfolioName;
    }

    @Override
    public String toString() {
        return "PortfolioBean [portfolioName=" + portfolioName + ", totalValue=" + totalValue + ", createdDate="
                + createdDate + "]";
    }
}