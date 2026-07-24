<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.stockLedger.title" /></h2>

<style>
	#stockLedgerTable { border-collapse: collapse; width: 100%; }
	#stockLedgerTable th, #stockLedgerTable td { border: 1px solid #ccc; padding: 4px 8px; text-align: center; }
	#stockLedgerTable td.ledgerDate { text-align: left; white-space: nowrap; }
	#stockLedgerTable th { background-color: #f0f0f0; }
</style>

<form method="get" action="${pageContext.request.contextPath}/module/labtestreport/stockLedger.form">
	<label for="startDate"><spring:message code="labtestreport.startDate" /></label>
	<input type="date" id="startDate" name="startDate" value="${startDate}" />
	<label for="endDate"><spring:message code="labtestreport.endDate" /></label>
	<input type="date" id="endDate" name="endDate" value="${endDate}" />
	<input type="submit" value="<spring:message code='labtestreport.filter'/>" />
</form>

<br />

<div style="overflow-x: auto;">
<table id="stockLedgerTable">
	<thead>
		<tr>
			<th rowspan="2"><spring:message code="labtestreport.stockLedger.date" /></th>
			<c:forEach items="${items}" var="item">
				<th colspan="4">${item.itemName}</th>
			</c:forEach>
		</tr>
		<tr>
			<c:forEach items="${items}" var="item">
				<th><spring:message code="labtestreport.stockLedger.actual" /></th>
				<th><spring:message code="labtestreport.stockLedger.incoming" /></th>
				<th><spring:message code="labtestreport.stockLedger.outgoing" /></th>
				<th><spring:message code="labtestreport.stockLedger.remaining" /></th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${dayBlocks}" var="block" varStatus="blockStatus">
			<tr class="${blockStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
				<td class="ledgerDate"><fmt:formatDate value="${block.date}" pattern="yyyy-MM-dd" /></td>
				<c:forEach items="${block.cells}" var="cell">
					<td>${cell.actualQty}</td>
					<td>${cell.incomingQty}</td>
					<td>${cell.outgoingQty}</td>
					<td>${cell.remainingQty}</td>
				</c:forEach>
			</tr>
		</c:forEach>
		<c:if test="${empty dayBlocks}">
			<tr>
				<td colspan="${totalColumns}"><spring:message code="labtestreport.drilldown.noPatients" /></td>
			</tr>
		</c:if>
	</tbody>
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
