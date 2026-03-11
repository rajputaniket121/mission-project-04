<%@page import="in.co.rays.proj4.controller.ArtGalleryCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>Art Gallery View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.ART_GALLERY_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.ArtGalleryBean"
            scope="request"></jsp:useBean>

        <div align="center">
            <h1 align="center" style="margin-bottom: -15; color: navy">
                <%
                    if (bean != null && bean.getId() > 0) {
                %>Update<%
                    } else {
                %>Add<%
                    }
                %>
                Artwork
            </h1>

            <div style="height: 15px; margin-bottom: 12px">
                <H3 align="center">
                    <font color="red"> <%=ServletUtility.getErrorMessage(request)%>
                    </font>
                </H3>

                <H3 align="center">
                    <font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
                    </font>
                </H3>
            </div>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Artwork Title<span style="color: red">*</span></th>
                    <td><input type="text" name="artworkTitle"
                        placeholder="Enter Artwork Title"
                        value="<%=DataUtility.getStringData(bean.getArtworkTitle())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("artworkTitle", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Artist Name<span style="color: red">*</span></th>
                    <td><input type="text" name="artistName"
                        placeholder="Enter Artist Name"
                        value="<%=DataUtility.getStringData(bean.getArtistName())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("artistName", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Exhibition Date<span style="color: red">*</span></th>
                    <td><input style="width: 100%" type="text" name="exhibitionDate" id="udate"
                        value="<%=DataUtility.getDateString(bean.getExhibitionDate())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("exhibitionDate", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Price (&#8377;)<span style="color: red">*</span></th>
                    <td><input type="text" name="price"
                        placeholder="Enter Price"
                        value="<%=DataUtility.getStringData(bean.getPrice())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("price", request)%></font></td>
                </tr>

                <tr>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <th></th>
                    <%
                        if (bean != null && bean.getId() > 0) {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=ArtGalleryCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=ArtGalleryCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=ArtGalleryCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=ArtGalleryCtl.OP_RESET%>">
                    <%
                        }
                    %>
                </tr>
            </table>
        </div>
    </form>
    <%@include file="Footer.jsp"%>
</body>
</html>