package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.SchoolAttendanceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.SchoolAttendanceModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "SchoolAttendanceCtl", urlPatterns = "/ctl/SchoolAttendanceCtl")
public class SchoolAttendanceCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put("Present", "Present");
        statusMap.put("Absent", "Absent");
        statusMap.put("OnLeave", "On Leave");
        request.setAttribute("statusMap", statusMap);
        
        HashMap<String, String> classMap = new HashMap<>();
        classMap.put("Class 1", "Class 1");
        classMap.put("Class 2", "Class 2");
        classMap.put("Class 3", "Class 3");
        classMap.put("Class 4", "Class 4");
        classMap.put("Class 5", "Class 5");
        classMap.put("Class 6", "Class 6");
        classMap.put("Class 7", "Class 7");
        classMap.put("Class 8", "Class 8");
        classMap.put("Class 9", "Class 9");
        classMap.put("Class 10", "Class 10");
        classMap.put("Class 11", "Class 11");
        classMap.put("Class 12", "Class 12");
        request.setAttribute("classMap", classMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("studentName"))) {
            request.setAttribute("studentName", PropertyReader.getValue("error.require", "Student Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("studentName"))) {
            request.setAttribute("studentName", "Invalid Student Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("className"))) {
            request.setAttribute("className", PropertyReader.getValue("error.require", "Class Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("attendanceDate"))) {
            request.setAttribute("attendanceDate", PropertyReader.getValue("error.require", "Attendance Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("attendanceDate"))) {
            request.setAttribute("attendanceDate", PropertyReader.getValue("error.date", "Attendance Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("attendanceStatus"))) {
            request.setAttribute("attendanceStatus", PropertyReader.getValue("error.require", "Attendance Status"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        SchoolAttendanceBean bean = new SchoolAttendanceBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setStudentName(DataUtility.getString(request.getParameter("studentName")));
        bean.setClassName(DataUtility.getString(request.getParameter("className")));
        bean.setAttendanceDate(DataUtility.getDate(request.getParameter("attendanceDate")));
        bean.setAttendanceStatus(DataUtility.getString(request.getParameter("attendanceStatus")));
        bean.setRemarks(DataUtility.getString(request.getParameter("remarks")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        SchoolAttendanceModel model = new SchoolAttendanceModel();
        if (id > 0) {
            try {
                SchoolAttendanceBean bean = model.findByPk(id);
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
        SchoolAttendanceModel model = new SchoolAttendanceModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            SchoolAttendanceBean bean = (SchoolAttendanceBean) populateBean(req);
            try {
                model.addAttendance(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Attendance Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Attendance for this student on this date already exists !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.SCHOOL_ATTENDANCE_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            SchoolAttendanceBean bean = (SchoolAttendanceBean) populateBean(req);
            try {
                model.updateAttendance(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Attendance Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Attendance for this student on this date already exists !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.SCHOOL_ATTENDANCE_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.SCHOOL_ATTENDANCE_VIEW;
    }
}