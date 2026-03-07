package in.co.rays.proj4.bean;

import java.util.Date;

public class ExpenseReportBean extends BaseBean {
    private String submittedBy;
    private Date submittedDate;
    private long totalAmount;

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return submittedBy + " - " + totalAmount;
    }

    @Override
    public String toString() {
        return "ExpenseReportBean [submittedBy=" + submittedBy + ", submittedDate=" + submittedDate + ", totalAmount="
                + totalAmount + "]";
    }
}