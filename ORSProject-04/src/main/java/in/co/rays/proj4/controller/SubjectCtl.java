package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "SubjectCtl",urlPatterns = {"/ctl/SubjectCtl"})
public class SubjectCtl extends BaseCtl{
	
	@Override
	protected void preload(HttpServletRequest request) {
		CourseModel model = new CourseModel();
		try {
			List<CourseBean> courseList = model.list();
			request.setAttribute("courseList", courseList);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
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
            request.setAttribute("name", "Invalid Subject Name");
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("courseId"))) {
            request.setAttribute("courseId", PropertyReader.getValue("error.require","CourseId"));
            pass = false;
        }else if(!DataValidator.isLong(request.getParameter("courseId"))) {
            request.setAttribute("courseId", "Invalid Course Name");
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
        SubjectBean bean = new SubjectBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Long id = DataUtility.getLong(req.getParameter("id"));
    	SubjectModel model = new SubjectModel();
    	if(id>0) {
    		try {
    			SubjectBean bean = model.findByPk(id);
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
        SubjectModel model = new SubjectModel();
        if(UserCtl.OP_SAVE.equalsIgnoreCase(op)) {
            SubjectBean bean = (SubjectBean) populateBean(req);
            try {
            	model.addSubject(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Subject Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Subject Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
            ServletUtility.forward(getView(), req, resp);
        }else if(OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.SUBJECT_CTL, req, resp);
            return;
        }else if(OP_UPDATE.equalsIgnoreCase(op)) {
        	SubjectBean bean = (SubjectBean) populateBean(req);
	       	try {
	               model.updateSubject(bean);
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setSuccessMessage("Subject Updated SuccessFully !!!", req);
	           }catch(DuplicateRecordException dre) {
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setErrorMessage("Subject Already Exist !!!", req);
	           }catch(ApplicationException ae) {
	               ae.printStackTrace();
	               ServletUtility.handleException(ae, req, resp);
	               return;
	           }
	       }
	       else if(OP_CANCEL.equalsIgnoreCase(op)) {
	       	 ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, req, resp);
	       	 return;
	       }
	       ServletUtility.forward(getView(), req, resp);
    }

	@Override
	protected String getView() {
		return ORSView.SUBJECT_VIEW;
	}

}
