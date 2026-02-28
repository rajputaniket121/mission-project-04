package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.InsuranceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.InsuranceModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "InsuranceCtl", urlPatterns = "/ctl/InsuranceCtl")
public class InsuranceCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		String op = request.getParameter("operation");

		if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
			return pass;
		}

		String insuranceCode = request.getParameter("insuranceCode");

		if (DataValidator.isNull(insuranceCode)) {
			request.setAttribute("insuranceCode", PropertyReader.getValue("error.require", "Insurance Code"));
			pass = false;

		} else if (!insuranceCode.matches("^INS-\\d+$")) {
			request.setAttribute("insuranceCode", "Insurance Code must be in format INS-123");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("carName"))) {
			request.setAttribute("carName", PropertyReader.getValue("error.require", "Car Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("expiryDate"))) {
			request.setAttribute("expiryDate", PropertyReader.getValue("error.require", "Expiry Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("expiryDate"))) {
			request.setAttribute("expiryDate", PropertyReader.getValue("error.date", "Expiry Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("providerName"))) {
			request.setAttribute("providerName", PropertyReader.getValue("error.require", "Provider Name"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		InsuranceBean bean = new InsuranceBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setInsuranceCode(DataUtility.getString(request.getParameter("insuranceCode")));
		bean.setCarName(DataUtility.getString(request.getParameter("carName")));
		bean.setExpiryDate(DataUtility.getDate(request.getParameter("expiryDate")));
		bean.setProviderName(DataUtility.getString(request.getParameter("providerName")));
		populateDTO(bean, request);
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long id = DataUtility.getLong(req.getParameter("id"));
		InsuranceModel model = new InsuranceModel();
		if (id > 0) {
			try {
				InsuranceBean bean = model.findByPk(id);
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
		InsuranceModel model = new InsuranceModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			InsuranceBean bean = (InsuranceBean) populateBean(req);
			try {
				model.addInsurance(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Insurance Added Successfully !!!", req);
			} catch (DuplicateRecordException dre) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Insurance Code Already Exist !!!", req);
			} catch (ApplicationException ae) {
				ae.printStackTrace();
				ServletUtility.handleException(ae, req, resp);
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INSURANCE_CTL, req, resp);
			return;
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			InsuranceBean bean = (InsuranceBean) populateBean(req);
			try {
				model.updateInsurance(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Insurance Updated Successfully !!!", req);
			} catch (DuplicateRecordException dre) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Insurance Code Already Exist !!!", req);
			} catch (ApplicationException ae) {
				ae.printStackTrace();
				ServletUtility.handleException(ae, req, resp);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INSURANCE_LIST_CTL, req, resp);
			return;
		}

		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		return ORSView.INSURANCE_VIEW;
	}
}