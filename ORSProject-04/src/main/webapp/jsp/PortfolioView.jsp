<%@page import="in.co.rays.proj4.controller.PortfolioCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<html>
<head>
<title>Portfolio View</title>
<link rel="icon" type="image/png"
    href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <form action="<%=ORSView.PORTFOLIO_CTL%>" method="post">

        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="in.co.rays.proj4.bean.PortfolioBean"
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
                Portfolio
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
                    <th align="left">Portfolio Name<span style="color: red">*</span></th>
                    <td><input type="text" name="portfolioName"
                        placeholder="Enter Portfolio Name"
                        value="<%=DataUtility.getStringData(bean.getPortfolioName())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("portfolioName", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Total Value<span style="color: red">*</span></th>
                    <td><input type="text" name="totalValue"
                        placeholder="Enter Total Value"
                        value="<%=DataUtility.getStringData(bean.getTotalValue())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("totalValue", request)%></font></td>
                </tr>
                <tr>
                    <th align="left">Created Date<span style="color: red">*</span></th>
                    <td><input style="width: 100%" type="text" name="createdDate" id="udate"
                        value="<%=DataUtility.getDateString(bean.getCreatedDate())%>"></td>
                    <td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("createdDate", request)%></font></td>
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
                        <input type="submit" name="operation" value="<%=PortfolioCtl.OP_UPDATE%>"> 
                        <input type="submit" name="operation" value="<%=PortfolioCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                    <td align="left" colspan="2">
                        <input type="submit" name="operation" value="<%=PortfolioCtl.OP_SAVE%>"> 
                        <input type="submit" name="operation" value="<%=PortfolioCtl.OP_RESET%>">
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