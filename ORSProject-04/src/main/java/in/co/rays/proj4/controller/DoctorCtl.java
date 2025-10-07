package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "DoctorCtl",urlPatterns = "/ctl/DoctorCtl")
public class DoctorCtl extends BaseCtl{
	
	@Override
	protected void preload(HttpServletRequest request) {
		HashMap<String, String> expertiesMap = new HashMap<String, String>();
		expertiesMap.put("Cardiology", "Cardiology");
		expertiesMap.put("Dermatology", "Dermatology");
		expertiesMap.put("Endocrinology", "Endocrinology");
		expertiesMap.put("Family Medicine", "Family Medicine");
		expertiesMap.put("Gastroenterology", "Gastroenterology");
		expertiesMap.put("Hematology", "Hematology");
		expertiesMap.put("Infectious Diseases", "Infectious Diseases");
		expertiesMap.put("Internal Medicine", "Internal Medicine");
		expertiesMap.put("Nephrology", "Nephrology");
		expertiesMap.put("Neurology", "Neurology");
		expertiesMap.put("Oncology", "Oncology");
		expertiesMap.put("Ophthalmology", "Ophthalmology");
		expertiesMap.put("Orthopedics", "Orthopedics");
		expertiesMap.put("Otolaryngology", "Otolaryngology");
		expertiesMap.put("Pediatrics", "Pediatrics");
		expertiesMap.put("Psychiatry", "Psychiatry");
		expertiesMap.put("Pulmonology", "Pulmonology");
		expertiesMap.put("Radiology", "Radiology");
		expertiesMap.put("Surgery", "Surgery");
		expertiesMap.put("Urology", "Urology");
		request.setAttribute("expertiesMap", expertiesMap);
	}
	
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
            request.setAttribute("name", "Invalid Doctor Name");
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require","Date Of Birth"));
            pass = false;
        } else if(!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
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

        if(DataValidator.isNull(request.getParameter("experties"))){
            request.setAttribute("experties", PropertyReader.getValue("error.require", "Experties "));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        DoctorBean bean = new DoctorBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExperties(DataUtility.getString(request.getParameter("experties")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Long id = DataUtility.getLong(req.getParameter("id"));
    	DoctorModel model = new DoctorModel();
    	if(id>0) {
    		try {
    			DoctorBean bean = model.findByPk(id);
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
        DoctorModel model = new DoctorModel();
        if(DoctorCtl.OP_SAVE.equalsIgnoreCase(op)) {
            DoctorBean bean = (DoctorBean) populateBean(req);
            try {
                model.addDoctor(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Doctor Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Doctor Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        }else if(DoctorCtl.OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
            return;
        }else if(DoctorCtl.OP_UPDATE.equalsIgnoreCase(op)) {
        	DoctorBean bean = (DoctorBean) populateBean(req);
	       	try {
	               model.updateDoctor(bean);
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setSuccessMessage("Doctor Updated SuccessFully !!!", req);
	           }catch(DuplicateRecordException dre) {
	               ServletUtility.setBean(bean, req);
	               ServletUtility.setErrorMessage("Doctor Already Exist !!!", req);
	           }catch(ApplicationException ae) {
	               ae.printStackTrace();
	               ServletUtility.handleException(ae, req, resp);
	               return;
	           }
	       }
	       else if(DoctorCtl.OP_CANCEL.equalsIgnoreCase(op)) {
	       	 ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
	       	 return;
	       }
	       ServletUtility.forward(getView(), req, resp);
    }
	
	@Override
	protected String getView() {
		return ORSView.DOCTOR_VIEW;
	}
}
