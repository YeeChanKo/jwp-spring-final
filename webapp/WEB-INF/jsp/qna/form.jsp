<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/tags.jspf"%>

<!DOCTYPE html>
<html lang="kr">
<head>
<%@ include file="/include/header.jspf"%>
</head>
<body>
	<%@ include file="/include/navigation.jspf"%>

	<div class="container" id="main">
		<div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
			<div class="panel panel-default content-main">
				<c:choose>
					<c:when test="${editFlag}">
						<c:set var="requestPath"
							value="/questions/${question.questionId}/edit" />
						<c:set var="buttonText" value="수정하기" />
					</c:when>
					<c:otherwise>
						<c:set var="requestPath" value="/questions" />
						<c:set var="buttonText" value="질문하기" />
					</c:otherwise>
				</c:choose>
				<form:form name="question" modelAttribute="question"
					action="${requestPath}" method="POST">
					<div class="form-group">
						<label for="title">제목</label>
						<form:input path="title" cssClass="form-control" />
					</div>
					<div class="form-group">
						<label for="contents">내용</label>
						<form:textarea path="contents" rows="5" cssClass="form-control" />
					</div>
					<button type="submit" class="btn btn-success clearfix pull-right">${buttonText}</button>
					<div class="clearfix"></div>
				</form:form>
			</div>
		</div>
	</div>

	<%@ include file="/include/footer.jspf"%>
</body>
</html>