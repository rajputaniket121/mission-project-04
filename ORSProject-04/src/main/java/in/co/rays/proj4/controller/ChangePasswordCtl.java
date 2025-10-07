package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.RecordNotFoundException;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/ctl/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseCtl {

    private static final Logger log = Logger.getLogger(ChangePasswordCtl.class);
    public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";

    @Override
    protected boolean validate(HttpServletRequest request) {
        log.debug("ChangePasswordCtl validate method started");
        boolean pass = true;

        String op = request.getParameter("operation");

        if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
            log.debug("Change My Profile operation, skipping validation");
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("oldPassword"))) {
            request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Old Password"));
            pass = false;
            log.debug("Old Password validation failed");
        } else if (request.getParameter("oldPassword").equals(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Old and New passwords should be different");
            pass = false;
            log.debug("Old and New passwords are same");
        }

        if (DataValidator.isNull(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
            pass = false;
            log.debug("New Password validation failed");
        } else if (!DataValidator.isPasswordLength(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Password should be 8 to 12 characters");
            pass = false;
            log.debug("Password length validation failed");
        } else if (!DataValidator.isPassword(request.getParameter("newPassword"))) {
            request.setAttribute("newPassword", "Must contain uppercase, lowercase, digit & special character");
            pass = false;
            log.debug("Password validation failed");
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
            log.debug("Confirm Password validation failed");
        }

        if (!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "New and confirm passwords not matched");
            pass = false;
            log.debug("New and confirm passwords not matched");
        }

        log.debug("ChangePasswordCtl validate method ended");
        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        log.debug("ChangePasswordCtl populateBean method started");
        UserBean bean = new UserBean();

        bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

        populateDTO(bean, request);

        log.debug("ChangePasswordCtl populateBean method ended");
        return bean;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ChangePasswordCtl doGet method started");
        ServletUtility.forward(getView(), request, response);
        log.debug("ChangePasswordCtl doGet method ended");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ChangePasswordCtl doPost method started");
        String op = DataUtility.getString(request.getParameter("operation"));
        String newPassword = (String) request.getParameter("newPassword");

        UserBean bean = (UserBean) populateBean(request);
        UserModel model = new UserModel();

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            try {
                log.debug("Changing password for user: " + user.getLogin());
                boolean flag = model.changePassword(id, bean.getPassword(), newPassword);
                if (flag == true) {
                    log.info("Password changed successfully for user: " + user.getLogin());
                    bean = model.findByLogin(user.getLogin());
                    session.setAttribute("user", bean);
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Password has been changed Successfully", request);
                } else {
                    log.warn("Old Password is invalid for user: " + user.getLogin());
                    ServletUtility.setErrorMessage("Old Password is Invalid", request);
                }
            } catch (RecordNotFoundException e) {
                log.error("Record not found while changing password", e);
                ServletUtility.setErrorMessage("Old Password is Invalid", request);
            } catch (ApplicationException e) {
                log.error("Application exception while changing password", e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
            log.debug("Redirecting to My Profile");
            ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request, response);
            return;
        }
        ServletUtility.forward(ORSView.CHANGE_PASSWORD_VIEW, request, response);
        log.debug("ChangePasswordCtl doPost method ended");
    }

    @Override
    protected String getView() {
        log.debug("ChangePasswordCtl getView method called");
        return ORSView.CHANGE_PASSWORD_VIEW;
    }
}