package in.co.rays.proj4.controller;

import in.co.rays.proj4.bean.*;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@WebServlet(name = "StudentCtl",urlPatterns = {"/StudentCtl"})
public class StudentCtl extends  BaseCtl{

    @Override
    protected void preload(HttpServletRequest request) {
        CollegeModel collegeModel = new CollegeModel();
        try {
            List<CollegeBean> collegeList =  collegeModel.list();
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

        if(DataValidator.isNull(request.getParameter("collegeId"))){
            request.setAttribute("collegeId", PropertyReader.getValue("error.require", "College Id "));
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

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        StudentBean bean = new StudentBean();
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
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
            StudentBean bean = (StudentBean) populateBean(req);
            try {
                StudentModel model = new StudentModel();
                model.addStudent(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Student Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Email Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
            }
            ServletUtility.forward(getView(), req, resp);
        }else if(OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.STUDENT_CTL, req, resp);
            return;
        }
    }


    @Override
    protected String getView() {
        return ORSView.STUDENT_VIEW;
    }
}
