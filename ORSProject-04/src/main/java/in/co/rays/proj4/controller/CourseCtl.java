package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CourseCtl",urlPatterns = {"/ctl/CourseCtl"})
public class CourseCtl extends BaseCtl{


	@Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if(OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if(DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require","Name"));
            pass = false;
        } else if(!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Course Name");
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("duration"))) {
            request.setAttribute("duration", PropertyReader.getValue("error.require","Duration "));
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require","description "));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        CourseBean bean = new CourseBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Long id = DataUtility.getLong(req.getParameter("id"));
    	CourseModel model = new CourseModel();
    	if(id>0) {
    		try {
    			CourseBean bean = model.findByPk(id);
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
        CourseModel model = new CourseModel();
        if(UserCtl.OP_SAVE.equalsIgnoreCase(op)) {
            CourseBean bean = (CourseBean) populateBean(req);
            try {
                model.addCourse(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Course Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Course Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        }else if(OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.COURSE_CTL, req, resp);
            return;
        }else if(OP_UPDATE.equalsIgnoreCase(op)) {
        	CourseBean bean = (CourseBean) populateBean(req);
	       	try {
	               model.updateCourse(bean);
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setSuccessMessage("Course Updated SuccessFully !!!", req);
	           }catch(DuplicateRecordException dre) {
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setErrorMessage("Course Already Exist !!!", req);
	           }catch(ApplicationException ae) {
	               ae.printStackTrace();
	               ServletUtility.handleException(ae, req, resp);
	               return;
	           }
	       }
	       else if(OP_CANCEL.equalsIgnoreCase(op)) {
	       	 ServletUtility.redirect(ORSView.COURSE_LIST_CTL, req, resp);
	       	 return;
	       }
	       ServletUtility.forward(getView(), req, resp);
    }
	
	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.COURSE_VIEW;
	}

}
