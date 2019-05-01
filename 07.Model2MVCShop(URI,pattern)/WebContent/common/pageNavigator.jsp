<%@ page contentType="text/html; charset=euc-kr" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


${page.currentPage } 현재 보고있는 페이지
${page.beginUnitPage} - 시작 페이지는 ?
${page.endUnitPage} -  끝 페이지는 ?

	<c:if test = "${! empty search.searchKeyword }">
	
	<c:if test="${ page.currentPage <= page.pageUnit }">
			◀ 이전
	</c:if>
	<c:if test="${ page.currentPage > page.pageUnit }">
			<a href="/product/listProduct?searchCondition=${search.searchCondition}&searchKeyword=${search.searchKeyword}&menu=${param.menu}>&page=${page.currentPage - 1}">◀ 이전</a>
	</c:if>
	
	<c:forEach var="i"  begin="${page.beginUnitPage}" end="${page.endUnitPage}" step="1">
		<a href="/product/listProduct?searchCondition=${search.searchCondition}&searchKeyword=${search.searchKeyword}&menu=${param.menu}&page=${i}">${i }</a>
	</c:forEach>
	
	<c:if test="${ page.endUnitPage >= page.maxPage }">
			이후 ▶
	</c:if>
	<c:if test="${ page.endUnitPage < page.maxPage }">
			<a href="/product/listProduct?searchCondition=${search.searchCondition}&searchKeyword=${search.searchKeyword}&menu=${param.menu}%>&page=${page.currentPage + 1}">▶ 이후</a>
	</c:if>

	
	 </c:if>
	 
	 
	 <c:if test = "${ empty search.searchKeyword}">
	  
	
	<c:if test="${ page.currentPage <= page.pageUnit }">
			◀ 이전
	</c:if>
	<c:if test="${ page.currentPage > page.pageUnit }">
			<a href="javascript:fncGetList('${ page.currentPage-1}')">◀ 이전</a>
	</c:if>
	
	<c:forEach var="i"  begin="${page.beginUnitPage}" end="${page.endUnitPage}" step="1">
		<a href="javascript:fncGetList('${ i }');">${ i }</a>
	</c:forEach>
	
	<c:if test="${ page.endUnitPage >= page.maxPage }">
			이후 ▶
	</c:if>
	<c:if test="${ page.endUnitPage < page.maxPage }">
			<a href="javascript:fncGetList('${page.endUnitPage+1}')">이후 ▶</a>
	</c:if>
	
	</c:if>