package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.NGOBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.NGOModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "NGOCtl", urlPatterns = "/ctl/NGOCtl")
public class NGOCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> focusAreaMap = new HashMap<>();
        focusAreaMap.put("Education", "Education");
        focusAreaMap.put("Healthcare", "Healthcare");
        focusAreaMap.put("Hospitals", "Hospitals");
        focusAreaMap.put("Child Welfare", "Child Welfare");
        focusAreaMap.put("Women Empowerment", "Women Empowerment");
        focusAreaMap.put("Environment", "Environment");
        focusAreaMap.put("Animal Welfare", "Animal Welfare");
        focusAreaMap.put("Disaster Relief", "Disaster Relief");
        focusAreaMap.put("Rural Development", "Rural Development");
        focusAreaMap.put("Food & Nutrition", "Food & Nutrition");
        focusAreaMap.put("Skill Development", "Skill Development");
        focusAreaMap.put("Elderly Care", "Elderly Care");
        request.setAttribute("focusAreaMap", focusAreaMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("ngoName"))) {
            request.setAttribute("ngoName", PropertyReader.getValue("error.require", "NGO Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("ngoName"))) {
            request.setAttribute("ngoName", "Invalid NGO Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("city"))) {
            request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("city"))) {
            request.setAttribute("city", "Invalid City Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("focusArea"))) {
            request.setAttribute("focusArea", PropertyReader.getValue("error.require", "Focus Area"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        NGOBean bean = new NGOBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setNgoName(DataUtility.getString(request.getParameter("ngoName")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setFocusArea(DataUtility.getString(request.getParameter("focusArea")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        NGOModel model = new NGOModel();
        if (id > 0) {
            try {
                NGOBean bean = model.findByPk(id);
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
        NGOModel model = new NGOModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            NGOBean bean = (NGOBean) populateBean(req);
            try {
                model.addNGO(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("NGO Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("NGO Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.NGO_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            NGOBean bean = (NGOBean) populateBean(req);
            try {
                model.updateNGO(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("NGO Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("NGO Name Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.NGO_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.NGO_VIEW;
    }
}