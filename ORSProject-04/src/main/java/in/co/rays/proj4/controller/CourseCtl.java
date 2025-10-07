package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CourseCtl", urlPatterns = {"/ctl/CourseCtl"})
public class CourseCtl extends BaseCtl {

    private static final Logger log = Logger.getLogger(CourseCtl.class);
    
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("CourseCtl validate method started");
        boolean pass = true;
        String op = request.getParameter("operation");

        if(OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            log.debug("Operation is logout or reset, skipping validation");
            return pass;
        }

        if(DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require","Name"));
            pass = false;
            log.debug("Name validation failed");
        } else if(!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Course Name");
            pass = false;
            log.debug("Name format is invalid");
        }

        if(DataValidator.isNull(request.getParameter("duration"))) {
            request.setAttribute("duration", PropertyReader.getValue("error.require","Duration "));
            pass = false;
            log.debug("Duration validation failed");
        }

        if(DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require","description "));
            pass = false;
            log.debug("Description validation failed");
        }
        
        log.debug("CourseCtl validate method ended with pass = " + pass);
        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("CourseCtl populateBean method started");
        CourseBean bean = new CourseBean();
        
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        
        populateDTO(bean, request);
        log.debug("CourseCtl populateBean method ended");
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CourseCtl doGet method started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        CourseModel model = new CourseModel();
        
        if(id > 0) {
            try {
                log.debug("Fetching course details for id: " + id);
                CourseBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                log.error("Error in getting course details", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl doGet method ended");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        log.debug("CourseCtl doPost method started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        CourseModel model = new CourseModel();
        
        if(OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            CourseBean bean = (CourseBean) populateBean(request);
            
            try {
                if(id > 0) {
                    log.debug("Updating course with id: " + id);
                    model.updateCourse(bean);
                    log.info("Course updated successfully: " + bean.getName());
                    ServletUtility.setSuccessMessage("Course is successfully updated", request);
                } else {
                    log.debug("Adding new course: " + bean.getName());
                    long pk = model.addCourse(bean);
                    log.info("Course added successfully with id: " + pk);
                    ServletUtility.setSuccessMessage("Course is successfully added", request);
                }
            } catch (ApplicationException e) {
                log.error("Application exception in course operation", e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                log.warn("Duplicate course found: " + bean.getName());
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Course Name already exists", request);
            }
        } else if(OP_DELETE.equalsIgnoreCase(op)) {
            CourseBean bean = (CourseBean) populateBean(request);
            try {
                log.debug("Deleting course with id: " + id);
                model.deleteCourse(bean.getId());
                log.info("Course deleted successfully with id: " + id);
                ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                log.error("Error in deleting course", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if(OP_CANCEL.equalsIgnoreCase(op)) {
            log.debug("Operation cancelled, redirecting to course list");
            ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
            return;
        } else if(OP_RESET.equalsIgnoreCase(op)) {
            log.debug("Reset operation, redirecting to course form");
            ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
            return;
        }
        
        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl doPost method ended");
    }

    @Override
    protected String getView() {
        log.debug("CourseCtl getView method called");
        return ORSView.COURSE_VIEW;
    }
}
