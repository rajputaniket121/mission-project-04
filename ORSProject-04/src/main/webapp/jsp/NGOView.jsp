<%@page import="in.co.rays.proj4.controller.NGOCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>NGO View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.NGO_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.NGOBean"
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
                NGO
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
            <%
                HashMap<String, String> focusAreaMap = (HashMap<String, String>) request.getAttribute("focusAreaMap");
            %>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">NGO Name<span style="color: red">*</span></th>
                    <td><input type="text" name="ngoName"
                        placeholder="Enter NGO Name"
                        value="<%=DataUtility.getStringData(bean.getNgoName())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("ngoName", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">City<span style="color: red">*</span></th>
                    <td><input type="text" name="city"
                        placeholder="Enter City"
                        value="<%=DataUtility.getStringData(bean.getCity())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("city", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Focus Area<span style="color: red">*</span></th>
                    <td>
                        <%=HTMLUtility.getList("focusArea", bean.getFocusArea(), focusAreaMap)%>
                    </td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("focusArea", request)%></font></td>
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
                        <input type="submit" name="operation" value="<%=NGOCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=NGOCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=NGOCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=NGOCtl.OP_RESET%>">
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