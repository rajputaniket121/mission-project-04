package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MyProfileCtl", urlPatterns = {"/MyProfileCtl"})
public class MyProfileCtl extends BaseCtl {
	public static final String OP_CHANGE_PASSWORD = "Change Password";
	
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		
		String op = request.getParameter("operation");
		
		if(OP_CHANGE_PASSWORD.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}
		
		if(DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require","Login Id"));
			pass = false;
		} else if(!DataValidator.isEmail(request.getParameter("login"))) {
			request.setAttribute("login",PropertyReader.getValue("error.email","Login"));
			pass = false;
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
		if(DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require","Date Of Birth"));
			pass = false;
		} else if(!DataValidator.isDate(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			pass = false;
		} 
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
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
		HttpSession session = request.getSession();
		UserBean bean = (UserBean) session.getAttribute("user");
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		populateDTO(bean,request);
	return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		UserBean bean = (UserBean) session.getAttribute("user");
		ServletUtility.setBean(bean, req);
		ServletUtility.forward(getView(), req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = DataUtility.getString(req.getParameter("operation"));
		UserBean bean = (UserBean) populateBean(req);
		if(BaseCtl.OP_SAVE.equalsIgnoreCase(op)) {
			UserModel model = new UserModel();
			try {
				model.updateUser(bean);
			 bean = model.findByPk(bean.getId());
			 ServletUtility.setSuccessMessage("Information Updated Successfully", req);
			 ServletUtility.setBean(bean, req);
			 ServletUtility.forward(getView(), req, resp);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Duplicate Login Id ", req);
				ServletUtility.forward(getView(), req, resp);
			}catch(ApplicationException ae) {
				ae.printStackTrace();
				ServletUtility.handleException(ae, req, resp);
				ServletUtility.forward(getView(), req, resp);
			}
		} else if (OP_CHANGE_PASSWORD.equalsIgnoreCase(op)){
			ServletUtility.forward(getView(), req, resp);
			return;
		}
	}

	@Override
	protected String getView() {
		return ORSView.MY_PROFILE_VIEW;
	}

}
