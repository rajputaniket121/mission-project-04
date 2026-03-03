package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.LockerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.LockerModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "LockerCtl", urlPatterns = "/ctl/LockerCtl")
public class LockerCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> lockerTypeMap = new HashMap<>();
        lockerTypeMap.put("Small", "Small");
        lockerTypeMap.put("Medium", "Medium");
        lockerTypeMap.put("Large", "Large");
        lockerTypeMap.put("Extra Large", "Extra Large");
        lockerTypeMap.put("Safety Deposit", "Safety Deposit");
        lockerTypeMap.put("Jewelry", "Jewelry");
        lockerTypeMap.put("Document", "Document");
        request.setAttribute("lockerTypeMap", lockerTypeMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("lockerNumber"))) {
            request.setAttribute("lockerNumber", PropertyReader.getValue("error.require", "Locker Number"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lockerType"))) {
            request.setAttribute("lockerType", PropertyReader.getValue("error.require", "Locker Type"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("annualFee"))) {
            request.setAttribute("annualFee", PropertyReader.getValue("error.require", "Annual Fee"));
            pass = false;
        } else if (!DataValidator.isDouble(request.getParameter("annualFee"))) {
            request.setAttribute("annualFee", "Invalid Annual Fee");
            pass = false;
        } else if (DataUtility.getDouble(request.getParameter("annualFee")) <= 0) {
            request.setAttribute("annualFee", "Annual Fee must be greater than zero");
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        LockerBean bean = new LockerBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setLockerNumber(DataUtility.getString(request.getParameter("lockerNumber")));
        bean.setLockerType(DataUtility.getString(request.getParameter("lockerType")));
        bean.setAnnualFee(DataUtility.getDouble(request.getParameter("annualFee")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        LockerModel model = new LockerModel();
        if (id > 0) {
            try {
                LockerBean bean = model.findByPk(id);
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
        LockerModel model = new LockerModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            LockerBean bean = (LockerBean) populateBean(req);
            try {
                model.addLocker(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Locker Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Locker Number Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.LOCKER_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            LockerBean bean = (LockerBean) populateBean(req);
            try {
                model.updateLocker(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Locker Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Locker Number Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.LOCKER_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.LOCKER_VIEW;
    }
}