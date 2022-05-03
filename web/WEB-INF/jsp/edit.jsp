<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../../css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"/></dd>
            <hr>
            <h3>Контакты: </h3>
            <c:forEach var="contactType" items="${ContactType.values()}">
                <dl>
                    <label for="${contactType.name()}">${contactType.title}</label>
                    <dd><input id="${contactType.name()}" type="text" name="${contactType.name()}" size=30
                               value="${resume.getContact(contactType)}"/></dd>
                </dl>
            </c:forEach>
            <hr>
            <h3>Секции: </h3>
            <c:forEach var="type" items="${SectionType.values()}">
                <c:set var="section" value="${resume.getSection(type)}"/>
                <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
                <div class="company">
                    <h3>${type.title}:</h3>
                    <c:choose>
                        <c:when test="${type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)}">
                            <textarea id="${type.name()}"
                                      name="${type.name()}"><%=((SimpleLineSection) section).getText()%></textarea>
                        </c:when>
                        <c:when test="${type.equals(SectionType.QUALIFICATIONS) || type.equals(SectionType.ACHIEVEMENT)}">
                            <textarea id="${type.name()}" name="${type.name()}"><%=String.join("\n", ((BulletedListSection)
                                    section).getItems())%></textarea>
                        </c:when>
                    </c:choose>
                </div>
            </c:forEach>
            <button type="submit">Сохранить</button>
            <button type="button" onclick="window.history.back(); return false;">Отменить</button>
        </dl>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>