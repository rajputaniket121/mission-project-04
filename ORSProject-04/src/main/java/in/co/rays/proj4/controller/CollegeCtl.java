package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CollegeCtl", urlPatterns = {"/ctl/CollegeCtl"})
public class CollegeCtl extends BaseCtl {
    
    private static final Logger log = Logger.getLogger(CollegeCtl.class);
    
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("CollegeCtl validate method started");
        boolean pass = true;
        
        if(DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require","College Name"));
            pass = false;
            log.debug("College Name validation failed");
        }else if(!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "College Name is not valid");
            pass = false;
            log.debug("College Name format is invalid");
        }
        
        if(DataValidator.isNull(request.getParameter("address"))) {
            request.setAttribute("address", PropertyReader.getValue("error.require","address"));
            pass = false;
            log.debug("Address validation failed");
        }
        
        if(DataValidator.isNull(request.getParameter("state"))) {
            request.setAttribute("state", PropertyReader.getValue("error.require","state"));
            pass = false;
            log.debug("State validation failed");
        }
        
        if(DataValidator.isNull(request.getParameter("city"))) {
            request.setAttribute("city", PropertyReader.getValue("error.require","city"));
            pass = false;
            log.debug("City validation failed");
        }
        
        if(DataValidator.isNull(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", PropertyReader.getValue("error.require","Mobile No"));
            pass = false;
            log.debug("Phone No validation failed");
        } else if(!DataValidator.isPhoneNo(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", "Please Enter Valid Mobile No");
            pass = false;
            log.debug("Phone No format is invalid");
        } else if(!DataValidator.isPhoneLength(request.getParameter("phoneNo"))) {
            request.setAttribute("phoneNo", "PhoneNo length should be 10");
            pass = false;
            log.debug("Phone No length is invalid");
        }
        
        log.debug("CollegeCtl validate method ended");
        return pass;
    }
    
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CollegeCtl populateBean method started");
        CollegeBean bean = new CollegeBean();
        
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setAddress(DataUtility.getString(request.getParameter("address")));
        bean.setState(DataUtility.getString(request.getParameter("state")));
        bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));
        
        populateDTO(bean, request);
        log.debug("CollegeCtl populateBean method ended");
        return bean;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CollegeCtl doGet method started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        CollegeModel model = new CollegeModel();
        
        if(id > 0) {
            try {
                log.debug("Fetching college details for id: " + id);
                CollegeBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error("Error in getting college details", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeCtl doGet method ended");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CollegeCtl doPost method started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        CollegeModel model = new CollegeModel();
        
        if(RoleCtl.OP_SAVE.equalsIgnoreCase(op)) {
            CollegeBean bean = (CollegeBean) populateBean(request);
            
            try {
                log.debug("Adding new college: " + bean.getName());
                long pk = model.addCollege(bean);
                log.info("College added successfully with id: " + pk);
                ServletUtility.setSuccessMessage("College is successfully added", request);
            } catch (ApplicationException e) {
                log.error("Application exception in college operation", e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate college found: " + bean.getName());
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("College Name already exists", request);
            }
        } else if(RoleCtl.OP_RESET.equalsIgnoreCase(op)){
            ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
            return;
        } else if(OP_UPDATE.equalsIgnoreCase(op)) {
            CollegeBean bean = (CollegeBean) populateBean(request);
            try {
                log.debug("Updating college with id: " + id);
                model.updateCollege(bean);
                log.info("College updated successfully: " + bean.getName());
                ServletUtility.setSuccessMessage("College is successfully updated", request);
            } catch (ApplicationException e) {
                log.error("Application exception in college operation", e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate college found: " + bean.getName());
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("College Name already exists", request);
            }
        } else if(OP_CANCEL.equalsIgnoreCase(op)) {
            log.debug("Operation cancelled, redirecting to college list");
            ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
            return;
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("CollegeCtl doPost method ended");
    }

    @Override
    protected String getView() {
        log.debug("CollegeCtl getView method called");
        return ORSView.COLLEGE_VIEW;
    }
}
