<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.cmam.title" /></h2>

<style>
	.cmamSummaryTable { border-collapse: collapse; width: 100%; margin-bottom: 2em; }
	.cmamSummaryTable th, .cmamSummaryTable td { border: 1px solid #ccc; padding: 4px 8px; text-align: left; }
	.cmamSummaryTable th { background-color: #f0f0f0; }
	.cmamSummaryTable td.count { text-align: center; }
</style>

<form method="get" action="${pageContext.request.contextPath}/module/labtestreport/cmamSummary.form">
	<label for="startDate"><spring:message code="labtestreport.startDate" /></label>
	<input type="date" id="startDate" name="startDate" value="${startDate}" />
	<label for="endDate"><spring:message code="labtestreport.endDate" /></label>
	<input type="date" id="endDate" name="endDate" value="${endDate}" />
	<input type="submit" value="<spring:message code='labtestreport.filter'/>" />
</form>

<br />

<h3><spring:message code="labtestreport.cmam.currentDiagnosis" /></h3>
<table class="cmamSummaryTable">
	<thead>
		<tr>
			<th><spring:message code="labtestreport.cmam.category" /></th>
			<th><spring:message code="labtestreport.cmam.numberOfChildren" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${currentDiagnosisRows}" var="row" varStatus="rowStatus">
			<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
				<td>${row.category}</td>
				<td class="count">
					<c:url value="/module/labtestreport/cmamDrilldown.form" var="cellUrl">
						<c:param name="dimensionConceptUuid" value="${dimensionConceptUuids['currentDiagnosis']}" />
						<c:param name="categoryConceptId" value="${row.categoryConceptId}" />
						<c:param name="dimensionLabel" value="Current Diagnosis" />
						<c:param name="categoryLabel" value="${row.category}" />
						<c:param name="startDate" value="${startDate}" />
						<c:param name="endDate" value="${endDate}" />
					</c:url>
					<a href="${cellUrl}">${row.total}</a>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty currentDiagnosisRows}">
			<tr><td colspan="2"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
		</c:if>
	</tbody>
</table>

<h3><spring:message code="labtestreport.cmam.childLastStatus" /></h3>
<table class="cmamSummaryTable">
	<thead>
		<tr>
			<th><spring:message code="labtestreport.cmam.category" /></th>
			<th><spring:message code="labtestreport.cmam.numberOfChildren" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${childLastStatusRows}" var="row" varStatus="rowStatus">
			<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
				<td>${row.category}</td>
				<td class="count">
					<c:url value="/module/labtestreport/cmamDrilldown.form" var="cellUrl">
						<c:param name="dimensionConceptUuid" value="${dimensionConceptUuids['childLastStatus']}" />
						<c:param name="categoryConceptId" value="${row.categoryConceptId}" />
						<c:param name="dimensionLabel" value="Child Last Status" />
						<c:param name="categoryLabel" value="${row.category}" />
						<c:param name="startDate" value="${startDate}" />
						<c:param name="endDate" value="${endDate}" />
					</c:url>
					<a href="${cellUrl}">${row.total}</a>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty childLastStatusRows}">
			<tr><td colspan="2"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
		</c:if>
	</tbody>
</table>

<h3><spring:message code="labtestreport.cmam.alertStatus" /></h3>
<table class="cmamSummaryTable">
	<thead>
		<tr>
			<th><spring:message code="labtestreport.cmam.category" /></th>
			<th><spring:message code="labtestreport.cmam.numberOfChildren" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${alertStatusRows}" var="row" varStatus="rowStatus">
			<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
				<td>${row.category}</td>
				<td class="count">
					<c:url value="/module/labtestreport/cmamDrilldown.form" var="cellUrl">
						<c:param name="dimensionConceptUuid" value="${dimensionConceptUuids['alertStatus']}" />
						<c:param name="categoryConceptId" value="${row.categoryConceptId}" />
						<c:param name="dimensionLabel" value="Alert Status" />
						<c:param name="categoryLabel" value="${row.category}" />
						<c:param name="startDate" value="${startDate}" />
						<c:param name="endDate" value="${endDate}" />
					</c:url>
					<a href="${cellUrl}">${row.total}</a>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty alertStatusRows}">
			<tr><td colspan="2"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
		</c:if>
	</tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
