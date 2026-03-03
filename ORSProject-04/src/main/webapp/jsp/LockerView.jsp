<%@page import="in.co.rays.proj4.controller.LockerCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>Locker View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.LOCKER_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.LockerBean"
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
                Locker
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
                HashMap<String, String> lockerTypeMap = (HashMap<String, String>) request.getAttribute("lockerTypeMap");
            %>

            <input type="hidden" name="id" value="<%=bean.getId()%>"> 
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>"> 
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDateTime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDateTime())%>">

            <table>
                <tr>
                    <th align="left">Locker Number<span style="color: red">*</span></th>
                    <td><input type="text" name="lockerNumber"
                        placeholder="Enter Locker Number"
                        value="<%=DataUtility.getStringData(bean.getLockerNumber())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("lockerNumber", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Locker Type<span style="color: red">*</span></th>
                    <td><%=HTMLUtility.getList("lockerType", bean.getLockerType(), lockerTypeMap)%></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("lockerType", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Annual Fee<span style="color: red">*</span></th>
                    <td><input type="text" name="annualFee"
                        placeholder="Enter Annual Fee"
                        value="<%=DataUtility.getStringData(bean.getAnnualFee())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("annualFee", request)%></font></td>
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
                        <input type="submit" name="operation" value="<%=LockerCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=LockerCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=LockerCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=LockerCtl.OP_RESET%>">
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