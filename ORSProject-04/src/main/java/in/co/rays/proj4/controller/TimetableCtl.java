package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.bean.TimetableBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.model.TimetableModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "TimetableCtl",urlPatterns = {"/TimetableCtl"})
public class TimetableCtl extends BaseCtl {
	
	 @Override
	    protected void preload(HttpServletRequest request) {
	        CourseModel courseModel = new CourseModel();
	        SubjectModel subjectModel = new SubjectModel();
	        try {
	            List<CourseBean> courseList =  courseModel.list();
	            List<SubjectBean> subjectList = subjectModel.list();
	            request.setAttribute("courseList", courseList);
	            request.setAttribute("subjectList", subjectList);
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

	        if (DataValidator.isNull(request.getParameter("semester"))) {
	            request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("examDate"))) {
	            request.setAttribute("examDate", PropertyReader.getValue("error.require", "ExamDate"));
	            pass = false;
	        } else if (!DataValidator.isDate(request.getParameter("examDate"))) {
	            request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of Birth"));
	            pass = false;
	        }
	        if(DataValidator.isNull(request.getParameter("examTime"))){
	            request.setAttribute("examTime", PropertyReader.getValue("error.require", "ExamTime "));
	            pass = false;
	        }

	        if(DataValidator.isNull(request.getParameter("description"))){
	            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	        TimetableBean bean = new TimetableBean();
	        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
	        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
	        bean.setSemester(DataUtility.getString(request.getParameter("semester")));
	        bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
	        bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
	        bean.setDescription(DataUtility.getString(request.getParameter("description")));
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
	        if(TimetableCtl.OP_SAVE.equalsIgnoreCase(op)) {
	            TimetableBean bean = (TimetableBean) populateBean(req);
	            try {
	                TimetableModel model = new TimetableModel();
	                model.addTimetable(bean);
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setSuccessMessage("Timetable Added SuccessFully !!!", req);
	            }catch(DuplicateRecordException dre) {
	                ServletUtility.setBean(bean, req);
	                ServletUtility.setErrorMessage(" Already Exist !!!", req);
	            }catch(ApplicationException ae) {
	                ae.printStackTrace();
	                ServletUtility.handleException(ae, req, resp);
	            }
	            ServletUtility.forward(getView(), req, resp);
	        }else if(OP_RESET.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.TIMETABLE_CTL, req, resp);
	            return;
	        }
	    }


	@Override
	protected String getView() {
		return ORSView.TIMETABLE_VIEW;
	}

}
