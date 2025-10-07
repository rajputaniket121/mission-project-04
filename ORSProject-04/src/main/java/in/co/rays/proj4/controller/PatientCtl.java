package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
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

/**
 * PatientCtl is a servlet controller class that handles CRUD operations
 * for Patient records in the application.
 *
 * <p>
 * It extends {@link BaseCtl} and provides functionality for:
 * <ul>
 *   <li>Preloading data for dropdowns (e.g., list of diseases)</li>
 *   <li>Validating patient form input</li>
 *   <li>Populating {@link PatientBean} from request parameters</li>
 *   <li>Handling GET and POST requests for add, update, and reset operations</li>
 * </ul>
 * </p>
 *
 * <p>
 * URL mapping: <code>/ctl/PatientCtl</code>
 * </p>
 */
@WebServlet(name = "PatientCtl", urlPatterns = "/ctl/PatientCtl")
public class PatientCtl extends BaseCtl {

    /**
     * Preloads data required for patient form.
     * 
     * @param request the HttpServletRequest object to set preloaded data
     */
    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> decease = new HashMap<>();
        decease.put("Diabetes", "Diabetes");
        decease.put("Hypertension", "Hypertension");
        decease.put("Asthma", "Asthma");
        decease.put("Tuberculosis", "Tuberculosis");
        decease.put("Malaria", "Malaria");
        decease.put("Alzheimer's", "Alzheimer's");
        decease.put("Parkinson's", "Parkinson's");
        decease.put("Hepatitis", "Hepatitis");
        decease.put("Cholera", "Cholera");
        decease.put("Ebola", "Ebola");
        request.setAttribute("deceaseMap", decease);
    }

    /**
     * Validates patient form data.
     *
     * @param request the HttpServletRequest object containing form data
     * @return true if form data is valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Patient Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require", "Date Of Visit"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
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

        if (DataValidator.isNull(request.getParameter("decease"))) {
            request.setAttribute("decease", PropertyReader.getValue("error.require", "Decease"));
            pass = false;
        }

        return pass;
    }

    /**
     * Populates a {@link PatientBean} with request parameters.
     *
     * @param request the HttpServletRequest object
     * @return a PatientBean populated with request data
     */
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

    /**
     * Handles HTTP GET requests.
     * Loads patient data by ID if provided and forwards to patient view.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = DataUtility.getLong(req.getParameter("id"));
        PatientModel model = new PatientModel();
        if (id > 0) {
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

    /**
     * Handles HTTP POST requests for saving, updating, resetting, or cancelling patient data.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        PatientModel model = new PatientModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            PatientBean bean = (PatientBean) populateBean(req);
            try {
                model.addPatient(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            PatientBean bean = (PatientBean) populateBean(req);
            try {
                model.updatePatient(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view (JSP page) associated with this controller.
     *
     * @return the patient view JSP path
     */
    @Override
    protected String getView() {
        return ORSView.PATIENT_VIEW;
    }
}
