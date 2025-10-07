package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import org.apache.log4j.Logger;
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

/**
 * DoctorCtl is a servlet controller that handles CRUD operations
 * for Doctor records in the application.
 *
 */
@WebServlet(name = "DoctorCtl", urlPatterns = "/ctl/DoctorCtl")
public class DoctorCtl extends BaseCtl {

    private static final Logger log = Logger.getLogger(DoctorCtl.class.getName());

    /**
     * Preloads specialties (experties) for doctor form dropdown.
     *
     * @param request the HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        log.info("Inside preload method of DoctorCtl");
        HashMap<String, String> expertiesMap = new HashMap<>();
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

    /**
     * Validates doctor form data.
     *
     * @param request the HttpServletRequest object containing form data
     * @return true if form data is valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        log.info("Inside validate method of DoctorCtl");
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Doctor Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
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

        if (DataValidator.isNull(request.getParameter("experties"))) {
            request.setAttribute("experties", PropertyReader.getValue("error.require", "Experties"));
            pass = false;
        }

        return pass;
    }

    /**
     * Populates a {@link DoctorBean} with request parameters.
     *
     * @param request the HttpServletRequest object
     * @return a DoctorBean populated with form data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.info("Inside populateBean method of DoctorCtl");
        DoctorBean bean = new DoctorBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExperties(DataUtility.getString(request.getParameter("experties")));
        populateDTO(bean, request);
        return bean;
    }

    /**
     * Handles HTTP GET requests.
     * Loads doctor data by ID if provided and forwards to doctor view.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Inside doGet method of DoctorCtl");
        Long id = DataUtility.getLong(req.getParameter("id"));
        DoctorModel model = new DoctorModel();
        if (id > 0) {
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

    /**
     * Handles HTTP POST requests for saving, updating, resetting, or cancelling doctor data.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Inside doPost method of DoctorCtl");

        String op = DataUtility.getString(req.getParameter("operation"));
        DoctorModel model = new DoctorModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            DoctorBean bean = (DoctorBean) populateBean(req);
            try {
                model.addDoctor(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Doctor Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Doctor Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            DoctorBean bean = (DoctorBean) populateBean(req);
            try {
                model.updateDoctor(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Doctor Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Doctor Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view (JSP page) associated with this controller.
     *
     * @return the doctor view JSP path
     */
    @Override
    protected String getView() {
        log.info("Inside getView method of DoctorCtl");
        return ORSView.DOCTOR_VIEW;
    }
}