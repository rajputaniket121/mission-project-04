package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.PatientModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "PatientCtl" , urlPatterns = "/ctl/PatientCtl")
public class PatientCtl extends BaseCtl{
	
	@Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if(OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if(DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require","Name"));
            pass = false;
        } else if(!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Patient Name");
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require","Date Of Visit"));
            pass = false;
        } else if(!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.date", "Date of Visit"));
            pass = false;
        }
        
        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Invalid Mobile No");
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("decease"))){
            request.setAttribute("decease", PropertyReader.getValue("error.require", "Decease "));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        PatientBean bean = new PatientBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setDecease(DataUtility.getString(request.getParameter("decease")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Long id = DataUtility.getLong(req.getParameter("id"));
    	PatientModel model = new PatientModel();
    	if(id>0) {
    		try {
    			PatientBean bean = model.findByPk(id);
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
        PatientModel model = new PatientModel();
        if(UserCtl.OP_SAVE.equalsIgnoreCase(op)) {
            PatientBean bean = (PatientBean) populateBean(req);
            try {
                model.addPatient(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        }else if(OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_CTL, req, resp);
            return;
        }else if(OP_UPDATE.equalsIgnoreCase(op)) {
        	PatientBean bean = (PatientBean) populateBean(req);
	       	try {
	               model.updatePatient(bean);
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setSuccessMessage("Patient Updated SuccessFully !!!", req);
	           }catch(DuplicateRecordException dre) {
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setErrorMessage("Patient Already Exist !!!", req);
	           }catch(ApplicationException ae) {
	               ae.printStackTrace();
	               ServletUtility.handleException(ae, req, resp);
	               return;
	           }
	       }
	       else if(OP_CANCEL.equalsIgnoreCase(op)) {
	       	 ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, req, resp);
	       	 return;
	       }
	       ServletUtility.forward(getView(), req, resp);
    }
	
	@Override
	protected String getView() {
		return ORSView.PATIENT_VIEW;
	}

}
