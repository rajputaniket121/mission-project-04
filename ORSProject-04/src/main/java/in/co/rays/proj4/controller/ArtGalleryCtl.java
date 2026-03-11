package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.ArtGalleryBean;
import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.ArtGalleryModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "ArtGalleryCtl", urlPatterns = "/ctl/ArtGalleryCtl")
public class ArtGalleryCtl extends BaseCtl {

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_LOG_OUT.equalsIgnoreCase(op) || OP_RESET.equalsIgnoreCase(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("artworkTitle"))) {
            request.setAttribute("artworkTitle", PropertyReader.getValue("error.require", "Artwork Title"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("artistName"))) {
            request.setAttribute("artistName", PropertyReader.getValue("error.require", "Artist Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("artistName"))) {
            request.setAttribute("artistName", "Invalid Artist Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("exhibitionDate"))) {
            request.setAttribute("exhibitionDate", PropertyReader.getValue("error.require", "Exhibition Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("exhibitionDate"))) {
            request.setAttribute("exhibitionDate", PropertyReader.getValue("error.date", "Exhibition Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("price"))) {
            request.setAttribute("price", PropertyReader.getValue("error.require", "Price"));
            pass = false;
        } else if (!DataValidator.isLong(request.getParameter("price"))) {
            request.setAttribute("price", "Invalid Price");
            pass = false;
        } else if (DataUtility.getLong(request.getParameter("price")) <= 0) {
            request.setAttribute("price", "Price must be greater than 0");
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        ArtGalleryBean bean = new ArtGalleryBean();
        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setArtworkTitle(DataUtility.getString(request.getParameter("artworkTitle")));
        bean.setArtistName(DataUtility.getString(request.getParameter("artistName")));
        bean.setExhibitionDate(DataUtility.getDate(request.getParameter("exhibitionDate")));
        bean.setPrice(DataUtility.getLong(request.getParameter("price")));
        populateDTO(bean, request);
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = DataUtility.getLong(req.getParameter("id"));
        ArtGalleryModel model = new ArtGalleryModel();
        if (id > 0) {
            try {
                ArtGalleryBean bean = model.findByPk(id);
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
        ArtGalleryModel model = new ArtGalleryModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            ArtGalleryBean bean = (ArtGalleryBean) populateBean(req);
            try {
                model.addArtwork(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Artwork Added Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Artwork Title Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ART_GALLERY_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            ArtGalleryBean bean = (ArtGalleryBean) populateBean(req);
            try {
                model.updateArtwork(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Artwork Updated Successfully !!!", req);
            } catch (DuplicateRecordException dre) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Artwork Title Already Exist !!!", req);
            } catch (ApplicationException ae) {
                ae.printStackTrace();
                ServletUtility.handleException(ae, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ART_GALLERY_LIST_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    @Override
    protected String getView() {
        return ORSView.ART_GALLERY_VIEW;
    }
}