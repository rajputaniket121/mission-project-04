<%@page import="in.co.rays.proj4.controller.RouteCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>RouteView</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.ROUTE_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.RouteBean"
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
                Route
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
                HashMap<String, String> cityMap = (HashMap<String, String>) request.getAttribute("cityMap");
            %>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Route Code<span style="color: red">*</span></th>
                    <td><input type="text" name="routeCode"
                        placeholder="Enter Route Code"
                        value="<%=DataUtility.getStringData(bean.getRouteCode())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("routeCode", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Source<span style="color: red">*</span></th>
                    <td>
                        <%=HTMLUtility.getList("source", bean.getSource(), cityMap)%>
                    </td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("source", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Destination<span style="color: red">*</span></th>
                    <td>
                        <%=HTMLUtility.getList("destination", bean.getDestination(), cityMap)%>
                    </td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("destination", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Distance (km)<span style="color: red">*</span></th>
                    <td><input type="text" name="distance"
                        placeholder="Enter Distance"
                        value="<%=DataUtility.getStringData(String.valueOf(bean.getDistance()))%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("distance", request)%></font></td>
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
                        <input type="submit" name="operation" value="<%=RouteCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=RouteCtl.OP_CANCEL%>">
                    </td>
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=RouteCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=RouteCtl.OP_RESET%>">
                    </td>
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