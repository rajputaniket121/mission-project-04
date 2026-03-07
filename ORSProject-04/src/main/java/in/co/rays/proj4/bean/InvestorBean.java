package in.co.rays.proj4.bean;

public class InvestorBean extends BaseBean {
    private String investorName;
    private long investmentAmount;
    private String investmentType;

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public long getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(long investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return investorName + " (" + investmentType + ")";
    }

    @Override
    public String toString() {
        return "InvestorBean [investorName=" + investorName + ", investmentAmount=" + investmentAmount
                + ", investmentType=" + investmentType + "]";
    }
}