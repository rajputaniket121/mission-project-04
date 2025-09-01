package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MarksheetCtl",urlPatterns = {"/MarksheetCtl"})
public class MarksheetCtl extends BaseCtl{
	
	
	@Override
	protected void preload(HttpServletRequest request) {
		StudentModel model = new StudentModel();
		try {
			List<StudentBean> studentList =  model.list();
			request.setAttribute("studentList",studentList);
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

	        if(DataValidator.isNull(request.getParameter("rollNo"))) {
	            request.setAttribute("rollNo", PropertyReader.getValue("error.require","Roll No"));
	            pass = false;
	        } else if(!DataValidator.isRollNo(request.getParameter("rollNo"))) {
	            request.setAttribute("rollNo", "Invalid RollNo Name");
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("studentId"))) {
	            request.setAttribute("studentId", PropertyReader.getValue("error.require","Name "));
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("physics"))) {
	            request.setAttribute("physics", PropertyReader.getValue("error.require","Physics "));
	            pass = false;
	        } else if(!DataValidator.isInteger(request.getParameter("physics"))) {
	            request.setAttribute("physics",PropertyReader.getValue("error.require","Physics"));
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("chemistry"))) {
	            request.setAttribute("chemistry", PropertyReader.getValue("error.require","Chemistry "));
	            pass = false;
	        } else if(!DataValidator.isInteger(request.getParameter("chemistry"))) {
	            request.setAttribute("chemistry",PropertyReader.getValue("error.require","Chemistry"));
	            pass = false;
	        }
	        
	        if(DataValidator.isNull(request.getParameter("maths"))) {
	            request.setAttribute("maths", PropertyReader.getValue("error.require","Maths "));
	            pass = false;
	        } else if(!DataValidator.isInteger(request.getParameter("maths"))) {
	            request.setAttribute("maths",PropertyReader.getValue("error.require","Maths"));
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	        MarksheetBean bean = new MarksheetBean();
	        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
	        bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
	        bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
	        bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
	        bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
	        populateDTO(bean, request);
	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        ServletUtility.forward(getView(), req, resp);
	    }

	    @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String op = DataUtility.getString(req.getParameter("operation"));
	        if(UserCtl.OP_SAVE.equalsIgnoreCase(op)) {
	            MarksheetBean bean = (MarksheetBean) populateBean(req);
	            try {
	            	MarksheetModel model = new MarksheetModel();
	                model.addMarksheet(bean);
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setSuccessMessage("Marksheet Added SuccessFully !!!", req);
	            }catch(DuplicateRecordException dre) {
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setErrorMessage("Marksheet Already Exist !!!", req);
	            }catch(ApplicationException ae) {
	                ae.printStackTrace();
	                ServletUtility.handleException(ae, req, resp);
	                return;
	            }
	            ServletUtility.forward(getView(), req, resp);
	        }else if(OP_RESET.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.MARKSHEET_CTL, req, resp);
	            return;
	        }
	    }
	
	
	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.MARKSHEET_VIEW;
	}

}
