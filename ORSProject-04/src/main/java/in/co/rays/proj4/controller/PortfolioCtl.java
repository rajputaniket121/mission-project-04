package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PortfolioBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.PortfolioModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "PortfolioCtl", urlPatterns = "/ctl/PortfolioCtl")
public class PortfolioCtl extends BaseCtl {

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("portfolioName"))) {
            request.setAttribute("portfolioName", PropertyReader.getValue("error.require", "Portfolio Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("totalValue"))) {
            request.setAttribute("totalValue", PropertyReader.getValue("error.require", "Total Value"));
            pass = false;
        } else if (!DataValidator.isDouble(request.getParameter("totalValue"))) {
            request.setAttribute("totalValue", "Invalid Total Value");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("createdDate"))) {
            request.setAttribute("createdDate", PropertyReader.getValue("error.require", "Created Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("createdDate"))) {
            request.setAttribute("createdDate", PropertyReader.getValue("error.date", "Created Date"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        PortfolioBean bean = new PortfolioBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setPortfolioName(DataUtility.getString(request.getParameter("portfolioName")));
        bean.setTotalValue(DataUtility.getLong(request.getParameter("totalValue")));
        bean.setCreatedDate(DataUtility.getDate(request.getParameter("createdDate")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        PortfolioModel model = new PortfolioModel();
        if (id > 0) {
            try {
                PortfolioBean bean = model.findByPk(id);
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
        PortfolioModel model = new PortfolioModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            PortfolioBean bean = (PortfolioBean) populateBean(req);
            try {
                model.addPortfolio(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Portfolio Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Portfolio Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PORTFOLIO_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            PortfolioBean bean = (PortfolioBean) populateBean(req);
            try {
                model.updatePortfolio(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Portfolio Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Portfolio Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PORTFOLIO_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.PORTFOLIO_VIEW;
    }
}