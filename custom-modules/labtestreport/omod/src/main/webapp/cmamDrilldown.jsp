<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.drilldown.title" /></h2>

<p>
	${dimensionLabel} &raquo; ${categoryLabel}
	<c:if test="${not empty startDate or not empty endDate}">
		<br /><spring:message code="labtestreport.startDate" />: ${empty startDate ? '...' : startDate}
		&ndash; <spring:message code="labtestreport.endDate" />: ${empty endDate ? '...' : endDate}
	</c:if>
</p>

<table cellpadding="2" cellspacing="0" class="box">
	<tr>
		<th><spring:message code="labtestreport.drilldown.name" /></th>
		<th><spring:message code="labtestreport.drilldown.identifier" /></th>
		<th><spring:message code="labtestreport.drilldown.sex" /></th>
		<th><spring:message code="labtestreport.drilldown.nationalId" /></th>
		<th><spring:message code="labtestreport.drilldown.phoneNumber" /></th>
		<th><spring:message code="labtestreport.cmam.currentDiagnosis" /></th>
		<th><spring:message code="labtestreport.cmam.childLastStatus" /></th>
		<th><spring:message code="labtestreport.cmam.alertStatus" /></th>
		<th><spring:message code="labtestreport.cmam.nextVisitDate" /></th>
	</tr>
	<c:set var="i" value="0" />
	<c:forEach items="${patients}" var="patient">
		<tr class="${i % 2 == 0 ? 'evenRow' : 'oddRow'}">
			<td>
				<a href="${pageContext.request.contextPath}/spa/patient/${patient.patientUuid}/chart/visits">
					${patient.givenName} ${patient.familyName}
				</a>
			</td>
			<td>${patient.identifier}</td>
			<td>${patient.sex}</td>
			<td>${patient.nationalId}</td>
			<td>${patient.phoneNumber}</td>
			<td>${patient.currentDiagnosis}</td>
			<td>${patient.childLastStatus}</td>
			<td>${patient.alertStatus}</td>
			<td><fmt:formatDate value="${patient.nextVisitDate}" pattern="yyyy-MM-dd" /></td>
		</tr>
		<c:set var="i" value="${i + 1}" />
	</c:forEach>
	<c:if test="${empty patients}">
		<tr><td colspan="9"><spring:message code="labtestreport.drilldown.noPatients" /></td></tr>
	</c:if>
</table>

<br />
<c:url value="${empty backUrl ? '/module/labtestreport/cmamSummary.form' : backUrl}" var="computedBackUrl">
	<c:param name="startDate" value="${startDate}" />
	<c:param name="endDate" value="${endDate}" />
</c:url>
<a href="${computedBackUrl}"><spring:message code="labtestreport.backToReport" /></a>

<%@ include file="/WEB-INF/template/footer.jsp"%>
