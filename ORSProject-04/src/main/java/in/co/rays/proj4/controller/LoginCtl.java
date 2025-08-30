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
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "LoginCtl", urlPatterns = {"/LoginCtl"})
public class LoginCtl extends BaseCtl{
	public static final String  OP_SIGN_IN = "Sign In";
	public static final String  OP_SIGN_UP = "Sign Up";
	
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		
		String op = request.getParameter("operation");
		
		if(OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}
		
		if(DataValidator.isNull(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.require","Login Id"));
			pass = false;
		} else if(!DataValidator.isEmail(request.getParameter("login"))) {
			request.setAttribute("login", PropertyReader.getValue("error.email","Login "));
			pass = false;
		}
		
		if(DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require","Password"));
			pass = false;
		}
		return pass;
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		UserBean bean = new UserBean();
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = DataUtility.getString(req.getParameter("operation"));
		if(OP_LOG_OUT.equalsIgnoreCase(op)) {
			HttpSession session = req.getSession();
			session.invalidate();
			ServletUtility.setSuccessMessage("Logout Successfully !!!", req);
		}
		ServletUtility.forward(getView(), req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = DataUtility.getString(req.getParameter("operation"));
		if(OP_SIGN_UP.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, req, resp);
			return;
		}else if(OP_SIGN_IN.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(req);
			try {
				UserModel model = new UserModel();
				RoleModel roleModel = new RoleModel();
				UserBean user = model.authenticate(bean.getLogin(), bean.getPassword());
				RoleBean roleBean = roleModel.findByPk(user.getRoleId());
				HttpSession session = req.getSession();
				session.setAttribute("user", user);
				session.setAttribute("role", roleBean.getName());
				ServletUtility.redirect(ORSView.WELCOME_CTL, req, resp);
				return;
			}catch(ApplicationException ae) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Invalid Login id & Password", req);
				ServletUtility.forward(getView(), req, resp);
			}
		}
	}

	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}

}
