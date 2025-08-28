package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "LoginCtl", urlPatterns = {"/LoginCtl"})
public class LoginCtl extends BaseCtl{
	public static final String  OP_SIGN_IN = "Sign In";
	public static final String  OP_SIGN_UP = "Sign Up";
	public static final String  OP_LOG_OUT = "Logout";
	
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		UserBean bean = new UserBean();
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletUtility.forward(getView(), req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String op = DataUtility.getString(req.getParameter("operation"));
		if(OP_SIGN_UP.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, req, resp);
			return;
		}
		
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.LOGIN_VIEW;
	}

}
