package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.InvestorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.InvestorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "InvestorCtl", urlPatterns = "/ctl/InvestorCtl")
public class InvestorCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> investmentTypeMap = new HashMap<>();
        investmentTypeMap.put("Stocks", "Stocks");
        investmentTypeMap.put("Mutual Funds", "Mutual Funds");
        investmentTypeMap.put("Fixed Deposits", "Fixed Deposits");
        investmentTypeMap.put("Real Estate", "Real Estate");
        investmentTypeMap.put("Gold", "Gold");
        investmentTypeMap.put("Bonds", "Bonds");
        investmentTypeMap.put("Cryptocurrency", "Cryptocurrency");
        investmentTypeMap.put("PPF", "PPF");
        investmentTypeMap.put("NPS", "NPS");
        investmentTypeMap.put("ULIP", "ULIP");
        request.setAttribute("investmentTypeMap", investmentTypeMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("investorName"))) {
            request.setAttribute("investorName", PropertyReader.getValue("error.require", "Investor Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("investorName"))) {
            request.setAttribute("investorName", "Invalid Investor Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("investmentAmount"))) {
            request.setAttribute("investmentAmount", PropertyReader.getValue("error.require", "Investment Amount"));
            pass = false;
        } else if (!DataValidator.isLong(request.getParameter("investmentAmount"))) {
            request.setAttribute("investmentAmount", "Invalid Investment Amount");
            pass = false;
        } else if (DataUtility.getLong(request.getParameter("investmentAmount")) <= 0) {
            request.setAttribute("investmentAmount", "Investment Amount must be greater than zero");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("investmentType"))) {
            request.setAttribute("investmentType", PropertyReader.getValue("error.require", "Investment Type"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        InvestorBean bean = new InvestorBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setInvestorName(DataUtility.getString(request.getParameter("investorName")));
        bean.setInvestmentAmount(DataUtility.getLong(request.getParameter("investmentAmount")));
        bean.setInvestmentType(DataUtility.getString(request.getParameter("investmentType")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        InvestorModel model = new InvestorModel();
        if (id > 0) {
            try {
                InvestorBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        InvestorModel model = new InvestorModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            InvestorBean bean = (InvestorBean) populateBean(req);
            try {
                model.addInvestor(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Investor Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Investor Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.INVESTOR_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            InvestorBean bean = (InvestorBean) populateBean(req);
            try {
                model.updateInvestor(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Investor Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Investor Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.INVESTOR_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.INVESTOR_VIEW;
    }
}