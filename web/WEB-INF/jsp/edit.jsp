<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="/css/style.css">
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
                    <dd><input id="${contactType.name()}" type="text" name="${contactType.name()}" size=30 value="${resume.getContact(contactType)}"/></dd>
                </dl>
            </c:forEach>
            <hr>
            <h3>Секции: </h3>
            <c:forEach var="sectionType" items="${SectionType.values()}">
                <dl>
                    <dt><label for="${sectionType.name()}">${sectionType.title}</label></dt>
                    <dd><textarea id="${sectionType.name()}" name="${sectionType.name()}">${resume.getSection(sectionType)}</textarea></dd>

                </dl>
            </c:forEach>
            <hr>
            <button type="submit">Сохранить</button>
            <button onclick="window.history.back()">Отменить</button>
        </dl>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>