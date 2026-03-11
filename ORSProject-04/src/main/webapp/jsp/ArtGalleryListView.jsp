<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.ArtGalleryListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.ArtGalleryBean"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Art Gallery List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>
    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.ArtGalleryBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Art Gallery List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3>
                <font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
            </h3>
            <h3>
                <font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
            </h3>
        </div>

        <form action="<%=ORSView.ART_GALLERY_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
                List<ArtGalleryBean> list = (List<ArtGalleryBean>) ServletUtility.getList(request);
                Iterator<ArtGalleryBean> it = list.iterator();

                if (list.size() != 0) {
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Artwork Title :</b></label>
                        <input type="text" name="artworkTitle" placeholder="Enter Artwork Title" value="<%=ServletUtility.getParameter("artworkTitle", request)%>">
                        &emsp;
                        <label><b>Artist Name :</b></label>
                        <input type="text" name="artistName" placeholder="Enter Artist Name" value="<%=ServletUtility.getParameter("artistName", request)%>">
                        &emsp;&nbsp;
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_SEARCH%>">&nbsp;
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>
            <br>

            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="25%">Artwork Title</th>
                    <th width="20%">Artist Name</th>
                    <th width="20%">Exhibition Date</th>
                    <th width="15%">Price (&#8377;)</th>
                    <th width="5%">Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (ArtGalleryBean) it.next();
                %>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>">
                    </td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center;"><%=bean.getArtworkTitle()%></td>
                    <td style="text-align: center;"><%=bean.getArtistName()%></td>
                    <td style="text-align: center;"><%=DataUtility.getDateString(bean.getExhibitionDate())%></td>
                    <td style="text-align: center;">&#8377; <%=bean.getPrice()%></td>
                    <td style="text-align: center;">
                        <a href="ArtGalleryCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_NEW%>">
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_DELETE%>">
                    </td>
                    <td style="width: 25%" align="right">
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_NEXT%>" <%=nextPageSize != 0 ? "" : "disabled"%>>
                    </td>
                </tr>
            </table>

            <%
                }
                if (list.size() == 0) {
            %>
            <table>
                <tr>
                    <td align="right">
                        <input type="submit" name="operation" value="<%=ArtGalleryListCtl.OP_BACK%>">
                    </td>
                </tr>
            </table>
            <%
                }
            %>
        </form>
    </div>
    <%@include file="Footer.jsp"%>
</body>
</html>