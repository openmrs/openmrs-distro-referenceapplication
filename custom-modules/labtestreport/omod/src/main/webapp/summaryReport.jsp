<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="labtestreport.title" /></h2>

<style>
	#labTestReportTable { border-collapse: collapse; width: 100%; }
	#labTestReportTable th, #labTestReportTable td { border: 1px solid #ccc; padding: 4px 8px; text-align: center; }
	#labTestReportTable td.category, #labTestReportTable td.labTest { text-align: left; }
	#labTestReportTable th { background-color: #f0f0f0; }
</style>

<form method="get" action="${pageContext.request.contextPath}/module/labtestreport/summary.form">
	<label for="startDate"><spring:message code="labtestreport.startDate" /></label>
	<input type="date" id="startDate" name="startDate" value="${startDate}" />
	<label for="endDate"><spring:message code="labtestreport.endDate" /></label>
	<input type="date" id="endDate" name="endDate" value="${endDate}" />
	<input type="submit" value="<spring:message code='labtestreport.filter'/>" />
</form>

<br />

<table id="labTestReportTable">
	<thead>
		<tr>
			<th rowspan="2"><spring:message code="labtestreport.category" /></th>
			<th rowspan="2"><spring:message code="labtestreport.labTest" /></th>
			<th rowspan="2"><spring:message code="labtestreport.totalTests" /></th>
			<th colspan="2">0-4</th>
			<th colspan="2">5-14</th>
			<th colspan="2">15-18</th>
			<th colspan="2">19-49</th>
			<th colspan="2">50-65</th>
			<th colspan="2">65+</th>
			<th rowspan="2"><spring:message code="labtestreport.total" /></th>
		</tr>
		<tr>
			<c:forEach begin="1" end="6">
				<th>M</th>
				<th>F</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${rows}" var="row" varStatus="rowStatus">
			<tr class="${rowStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
				<c:if test="${row.categoryRowSpan > 0}">
					<td class="category" rowspan="${row.categoryRowSpan}">${row.category}</td>
				</c:if>
				<td class="labTest">${row.testLabel}</td>
				<td>
					<c:choose>
						<c:when test="${row.totalTests > 0}">
							<c:url value="/module/labtestreport/drilldown.form" var="totalUrl">
								<c:param name="testConceptId" value="${row.testConceptId}" />
								<c:param name="category" value="${row.category}" />
								<c:param name="labTest" value="${row.testLabel}" />
								<c:param name="startDate" value="${startDate}" />
								<c:param name="endDate" value="${endDate}" />
							</c:url>
							<a href="${totalUrl}">${row.totalTests}</a>
						</c:when>
						<c:otherwise>${row.totalTests}</c:otherwise>
					</c:choose>
				</td>
				<c:forEach items="${columns}" var="col">
					<c:set var="cellCount" value="${row.counts[col.key]}" />
					<td>
						<c:choose>
							<c:when test="${cellCount > 0}">
								<c:url value="/module/labtestreport/drilldown.form" var="cellUrl">
									<c:param name="testConceptId" value="${row.testConceptId}" />
									<c:param name="ageGroup" value="${col.ageGroup}" />
									<c:param name="gender" value="${col.gender}" />
									<c:param name="category" value="${row.category}" />
									<c:param name="labTest" value="${row.testLabel}" />
									<c:param name="startDate" value="${startDate}" />
									<c:param name="endDate" value="${endDate}" />
								</c:url>
								<a href="${cellUrl}">${cellCount}</a>
							</c:when>
							<c:otherwise>${cellCount}</c:otherwise>
						</c:choose>
					</td>
				</c:forEach>
				<td>
					<c:choose>
						<c:when test="${row.total > 0}">
							<c:url value="/module/labtestreport/drilldown.form" var="grandTotalUrl">
								<c:param name="testConceptId" value="${row.testConceptId}" />
								<c:param name="category" value="${row.category}" />
								<c:param name="labTest" value="${row.testLabel}" />
								<c:param name="startDate" value="${startDate}" />
								<c:param name="endDate" value="${endDate}" />
							</c:url>
							<a href="${grandTotalUrl}">${row.total}</a>
						</c:when>
						<c:otherwise>${row.total}</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
