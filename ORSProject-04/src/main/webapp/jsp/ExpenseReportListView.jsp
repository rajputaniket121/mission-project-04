<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.ExpenseReportListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.ExpenseReportBean"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Expense Report List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>
    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.ExpenseReportBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Expense Report List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3>
                <font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
            </h3>
            <h3>
                <font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
            </h3>
        </div>

        <form action="<%=ORSView.EXPENSE_REPORT_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
                List<ExpenseReportBean> list = (List<ExpenseReportBean>) ServletUtility.getList(request);
                Iterator<ExpenseReportBean> it = list.iterator();

                if (list.size() != 0) {
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Submitted By :</b></label>
                        <input type="text" name="submittedBy" placeholder="Enter Submitter Name" value="<%=ServletUtility.getParameter("submittedBy", request)%>">
                        &emsp;
                        <label><b>Submitted Date :</b></label>
                        <input type="text" name="submittedDate" placeholder="Enter Date" value="<%=ServletUtility.getParameter("submittedDate", request)%>">
                        &emsp;
                        <label><b>Total Amount :</b></label>
                        <input type="text" name="totalAmount" placeholder="Enter Amount" value="<%=ServletUtility.getParameter("totalAmount", request)%>">
                        &emsp;&nbsp;
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_SEARCH%>">&nbsp;
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>
            <br>

            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="25%">Submitted By</th>
                    <th width="20%">Submitted Date</th>
                    <th width="20%">Total Amount</th>
                    <th width="5%">Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (ExpenseReportBean) it.next();
                %>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>">
                    </td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center;"><%=bean.getSubmittedBy()%></td>
                    <td style="text-align: center;"><%=DataUtility.getDateString(bean.getSubmittedDate())%></td>
                    <td style="text-align: center;">&#8377; <%=bean.getTotalAmount()%></td>
                    <td style="text-align: center;">
                        <a href="ExpenseReportCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_NEW%>">
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_DELETE%>">
                    </td>
                    <td style="width: 25%" align="right">
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_NEXT%>" <%=nextPageSize != 0 ? "" : "disabled"%>>
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
                        <input type="submit" name="operation" value="<%=ExpenseReportListCtl.OP_BACK%>">
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