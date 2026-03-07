package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.ExpenseReportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.ExpenseReportModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "ExpenseReportCtl", urlPatterns = "/ctl/ExpenseReportCtl")
public class ExpenseReportCtl extends BaseCtl {

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("submittedBy"))) {
            request.setAttribute("submittedBy", PropertyReader.getValue("error.require", "Submitted By"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("submittedBy"))) {
            request.setAttribute("submittedBy", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("submittedDate"))) {
            request.setAttribute("submittedDate", PropertyReader.getValue("error.require", "Submitted Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("submittedDate"))) {
            request.setAttribute("submittedDate", PropertyReader.getValue("error.date", "Submitted Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("totalAmount"))) {
            request.setAttribute("totalAmount", PropertyReader.getValue("error.require", "Total Amount"));
            pass = false;
        } else if (!DataValidator.isDouble(request.getParameter("totalAmount"))) {
            request.setAttribute("totalAmount", "Invalid Amount");
            pass = false;
        } else if (DataUtility.getDouble(request.getParameter("totalAmount")) <= 0) {
            request.setAttribute("totalAmount", "Amount must be greater than 0");
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        ExpenseReportBean bean = new ExpenseReportBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setSubmittedBy(DataUtility.getString(request.getParameter("submittedBy")));
        bean.setSubmittedDate(DataUtility.getDate(request.getParameter("submittedDate")));
        bean.setTotalAmount(DataUtility.getLong(request.getParameter("totalAmount")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        ExpenseReportModel model = new ExpenseReportModel();
        if (id > 0) {
            try {
                ExpenseReportBean bean = model.findByPk(id);
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
        ExpenseReportModel model = new ExpenseReportModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            ExpenseReportBean bean = (ExpenseReportBean) populateBean(req);
            try {
                model.addExpense(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Expense Report Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Expense Report for this submitter on this date already exists !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.EXPENSE_REPORT_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            ExpenseReportBean bean = (ExpenseReportBean) populateBean(req);
            try {
                model.updateExpense(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Expense Report Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Expense Report for this submitter on this date already exists !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.EXPENSE_REPORT_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.EXPENSE_REPORT_VIEW;
    }
}