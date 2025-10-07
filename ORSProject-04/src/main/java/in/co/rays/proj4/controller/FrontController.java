package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.proj4.util.ServletUtility;

/**
 * @author Aniket Rajput
 *
 * FrontController is a servlet filter that intercepts requests to protected
 * URLs in the application and ensures that the user session is valid.
 *
 * <p>
 * It is mapped to the URL patterns "/doc/" and "/ctl/*" and performs the following tasks:
 * <ul>
 *   <li>Checks if the user session exists.</li>
 *   <li>If the session has expired, redirects the user to the login page with an error message.</li>
 *   <li>Allows the request to proceed if the session is valid.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Use Case:</b>
 * <ul>
 *   <li>Provides centralized session management and access control for secured resources.</li>
 *   <li>Prevents unauthorized access to controller actions and sensitive pages.</li>
 * </ul>
 * </p>
 */
@WebFilter(urlPatterns = {"/doc/", "/ctl/*"})
public class FrontController implements Filter {

    /**
     * Initializes the filter. Currently, no initialization is required.
     *
     * @param filterConfig the filter configuration object
     * @throws ServletException if an exception occurs during initialization
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    /**
     * Intercepts requests to check for a valid user session.
     *
     * <p>
     * If the session has expired or the user is not logged in, forwards the request
     * to the login page with an error message. Otherwise, the request is passed
     * along the filter chain.
     * </p>
     *
     * @param req the ServletRequest object
     * @param resp the ServletResponse object
     * @param chain the FilterChain for invoking the next filter or the resource
     * @throws IOException if an I/O error occurs during request processing
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        if (session.getAttribute("user") == null) {
            request.setAttribute("error", "Your session has been expired. Please Login again!");
            ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
            return;
        } else {
            chain.doFilter(req, resp);
        }
    }

    /**
     * Destroys the filter. Currently, no cleanup is required.
     */
    @Override
    public void destroy() {
        // No cleanup required
    }
}
