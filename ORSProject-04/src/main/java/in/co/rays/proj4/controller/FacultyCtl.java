package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.bean.FacultyBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.model.FacultyModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "FacultyCtl", urlPatterns = {"/FacultyCtl"})
public class FacultyCtl extends BaseCtl{
	
	 @Override
	    protected void preload(HttpServletRequest request) {
		 	CollegeModel collegeModel = new CollegeModel();
	        CourseModel courseModel = new CourseModel();
	        SubjectModel subjectModel = new SubjectModel();
	        try {
	            List<CourseBean> courseList =  courseModel.list();
	            List<SubjectBean> subjectList = subjectModel.list();
	            List<CollegeBean> collegeList = collegeModel.list();
	            request.setAttribute("courseList", courseList);
	            request.setAttribute("subjectList", subjectList);
	            request.setAttribute("collegeList", collegeList);
	        } catch (ApplicationException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    @Override
	    protected boolean validate(HttpServletRequest request) {
	        boolean pass = true;
	        String op = request.getParameter("operation");

	        if(OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
	            return pass;
	        }
	        
	        if(DataValidator.isNull(request.getParameter("firstName"))) {
	            request.setAttribute("firstName", PropertyReader.getValue("error.require","First Name"));
	            pass = false;
	        } else if(!DataValidator.isName(request.getParameter("firstName"))) {
	            request.setAttribute("firstName", "Invalid First Name");
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("lastName"))) {
	            request.setAttribute("lastName", PropertyReader.getValue("error.require","Last Name"));
	            pass = false;
	        }
	        else if(!DataValidator.isName(request.getParameter("lastName"))) {
	            request.setAttribute("lastName", "Invalid Last Name");
	            pass = false;
	        }
	        if(DataValidator.isNull(request.getParameter("email"))) {
	            request.setAttribute("email", PropertyReader.getValue("error.require","Email Id"));
	            pass = false;
	        } else if(!DataValidator.isEmail(request.getParameter("email"))) {
	            request.setAttribute("email",PropertyReader.getValue("error.email","Email"));
	            pass = false;
	        }
	        if(DataValidator.isNull(request.getParameter("dob"))) {
	            request.setAttribute("dob", PropertyReader.getValue("error.require","Date Of Birth"));
	            pass = false;
	        } else if(!DataValidator.isDate(request.getParameter("dob"))) {
	            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
	            pass = false;
	        }
	        if(DataValidator.isNull(request.getParameter("gender"))){
	            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender "));
	            pass = false;
	        }
	        
	        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
	            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No "));
	            pass = false;
	        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
	            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
	            pass = false;
	        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
	            request.setAttribute("mobileNo", "Invalid Mobile No");
	            pass = false;
	        }
	        
	        if(DataValidator.isNull(request.getParameter("collegeId"))) {
	            request.setAttribute("collegeId", PropertyReader.getValue("error.require","College Name"));
	            pass = false;
	        } else if(!DataValidator.isLong(request.getParameter("collegeId"))) {
	            request.setAttribute("collegeId","Invalid College Name");
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("courseId"))) {
	            request.setAttribute("courseId", PropertyReader.getValue("error.require","Course Name"));
	            pass = false;
	        }
	        else if(!DataValidator.isLong(request.getParameter("courseId"))) {
	            request.setAttribute("courseId", "Invalid Course Name");
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("subjectId"))) {
	            request.setAttribute("subjectId", PropertyReader.getValue("error.require","Subject Name"));
	            pass = false;
	        } else if(!DataValidator.isLong(request.getParameter("subjectId"))) {
	            request.setAttribute("subjectId","Invalid Subject Name");
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	        FacultyBean bean = new FacultyBean();
	        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
	        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
	        bean.setEmail(DataUtility.getString(request.getParameter("email")));
	        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
	        bean.setGender(DataUtility.getString(request.getParameter("gender")));
	        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
	        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
	        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
	        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
	       
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
	        if(FacultyCtl.OP_SAVE.equalsIgnoreCase(op)) {
	            FacultyBean bean = (FacultyBean) populateBean(req);
	            try {
	                FacultyModel model = new FacultyModel();
	                model.addFaculty(bean);
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setSuccessMessage("Faculty Added SuccessFully !!!", req);
	            }catch(DuplicateRecordException dre) {
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setErrorMessage("Faculty Already Exist !!!", req);
	            }catch(ApplicationException ae) {
	                ae.printStackTrace();
	                ServletUtility.handleException(ae, req, resp);
	            }
	            ServletUtility.forward(getView(), req, resp);
	        }else if(OP_RESET.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.FACULTY_CTL, req, resp);
	            return;
	        }
	    }


	@Override
	protected String getView() {
		return ORSView.FACULTY_VIEW;
	}

}
