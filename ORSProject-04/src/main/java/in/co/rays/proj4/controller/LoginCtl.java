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

/**
 * LoginCtl is a servlet controller that handles user login and logout operations.
 *
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl {

    /** Operation name for Sign In */
    public static final String OP_SIGN_IN = "Sign In";

    /** Operation name for Sign Up */
    public static final String OP_SIGN_UP = "Sign Up";

    /**
     * Validates login form input parameters.
     *
     * @param request the HttpServletRequest object
     * @return true if input is valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        }
        return pass;
    }

    /**
     * Populates a {@link UserBean} from login form parameters.
     *
     * @param request the HttpServletRequest object
     * @return a UserBean populated with login credentials
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        UserBean bean = new UserBean();
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        return bean;
    }

    /**
     * Handles HTTP GET requests for login page display and logout operation.
     *
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        if (OP_LOG_OUT.equalsIgnoreCase(op)) {
            HttpSession session = req.getSession();
            session.invalidate();
            ServletUtility.setSuccessMessage("Logout Successfully !!!", req);
        }
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles HTTP POST requests for user login and registration redirection.
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

        if (OP_SIGN_UP.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, req, resp);
            return;
        } else if (OP_SIGN_IN.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(req);
            try {
                UserModel model = new UserModel();
                RoleModel roleModel = new RoleModel();

                UserBean user = model.authenticate(bean.getLogin(), bean.getPassword());

                if (user != null) {
                    RoleBean roleBean = roleModel.findByPk(user.getRoleId());
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    if (roleBean != null) {
                        session.setAttribute("role", roleBean.getName());
                    }

                    String uri = (String) req.getParameter("uri");
                    if (uri == null || "null".equalsIgnoreCase(uri)) {
                        ServletUtility.redirect(ORSView.WELCOME_CTL, req, resp);
                        return;
                    } else {
                        ServletUtility.redirect(uri, req, resp);
                        return;
                    }

                } else {
                    bean = (UserBean) populateBean(req);
                    ServletUtility.setBean(bean, req);
                    ServletUtility.setErrorMessage("Invalid Login id & Password", req);
                    ServletUtility.forward(getView(), req, resp);
                }

            } catch (ApplicationException ae) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Invalid Login id & Password", req);
                ServletUtility.forward(getView(), req, resp);
            }
        }
    }

    /**
     * Returns the login view (JSP page) associated with this controller.
     *
     * @return the login view JSP path
     */
    @Override
    protected String getView() {
        return ORSView.LOGIN_VIEW;
    }
}
