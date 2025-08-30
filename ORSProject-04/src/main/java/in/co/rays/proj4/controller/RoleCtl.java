package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "RoleCtl", urlPatterns = {"/RoleCtl"})
public class RoleCtl extends BaseCtl{
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		
		if(DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require","Role Name"));
			pass = false;
		}
		
		if(DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require","Description"));
			pass = false;
		}
		return pass;
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		RoleBean bean = new RoleBean();
		bean.setName(DataUtility.getString(request.getParameter("name")));
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
		RoleBean bean = (RoleBean) populateBean(req);
		RoleModel model = new RoleModel();
		if(RoleCtl.OP_SAVE.equalsIgnoreCase(op)) {
			try {
				model.addRole(bean);
				ServletUtility.setSuccessMessage("Role Added Successfully !!!", req);
				ServletUtility.setBean(bean, req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Role Already exist !!!", req);
			}catch (ApplicationException ae) {
				ae.printStackTrace();
				ServletUtility.handleException(ae, req, resp);
			}
			ServletUtility.forward(getView(),req, resp);
		}else if(RoleCtl.OP_RESET.equalsIgnoreCase(op)){
			ServletUtility.redirect(ORSView.ROLE_CTL,req, resp);
			return;
		}
	}

	@Override
	protected String getView() {
		return ORSView.ROLE_VIEW;
	}

}
