package in.co.rays.proj4.controller;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserCtl",urlPatterns = {"/UserCtl"})
public class UserCtl extends BaseCtl{

    @Override
    protected void preload(HttpServletRequest request) {
        RoleModel roleModel = new RoleModel();
        try {
            List<RoleBean> roleList =  roleModel.list();
            request.setAttribute("roleList", roleList);
        } catch (ApplicationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if(OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
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

        if(DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require","Login Id"));
            pass = false;
        } else if(!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login",PropertyReader.getValue("error.email","Login"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        } else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
            request.setAttribute("password", "Password should be 8 to 12 characters");
            pass = false;
        } else if (!DataValidator.isPassword(request.getParameter("password"))) {
            request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require","Date Of Birth"));
            pass = false;
        } else if(!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
                && !"".equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("confirmPassword", "Password and Confirm Password must be Same!");
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
        if(DataValidator.isNull(request.getParameter("gender"))){
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender "));
            pass = false;
        }

        if(DataValidator.isNull(request.getParameter("roleId"))){
            request.setAttribute("roleId", PropertyReader.getValue("error.require", "Role Id "));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        UserBean bean = new UserBean();
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
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
        if(UserCtl.OP_SAVE.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(req);
            try {
                UserModel model = new UserModel();
                model.addUser(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("User Added SuccessFully !!!", req);
            }catch(DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Login Id Already Exist !!!", req);
            }catch(ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
            }
            ServletUtility.forward(getView(), req, resp);
        }else if(OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_CTL, req, resp);
            return;
        }
    }


    @Override
    protected String getView() {
        return ORSView.USER_VIEW;
    }
}
