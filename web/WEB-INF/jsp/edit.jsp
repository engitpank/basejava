<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/inputValidator.js"></script>
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<div class="layout-wrapper">
    <jsp:include page="fragments/header.jsp"/>
    <main>
        <section class="resume">
            <form method="post" action="resume" enctype="application/x-www-form-urlencoded" onsubmit="return validateForm ( );">
                <input type="hidden" name="uuid" value="${resume.uuid}">
                <div class="input-block">
                    <label>Имя:</label>
                    <input type="text" id="fullName" name="fullName" size="50" required value="${resume.fullName}"/>
                </div>
                <hr>
                <h3>Контакты: </h3>
                <c:forEach var="contactType" items="${ContactType.values()}">
                    <div class="input-block">
                        <label for="${contactType.name()}">${contactType.title}:</label>
                        <input type="text" name="${contactType.name()}" size=30
                               value="${resume.getContact(contactType)}"/>
                    </div>
                </c:forEach>
                <hr>
                <h3>Секции: </h3>
                <c:forEach var="type" items="${SectionType.values()}">
                    <c:set var="section" value="${resume.getSection(type)}"/>
                    <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
                    <div class="company-block">
                        <h3>${type.title}:</h3>
                        <c:choose>
                            <c:when test="${type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)}">
                                <label for="${type.name()}">${type.title}</label>
                                <textarea id="${type.name()}"
                                          name="${type.name()}"><%=((SimpleLineSection) section).getText()%></textarea>
                            </c:when>
                            <c:when test="${type.equals(SectionType.QUALIFICATIONS) || type.equals(SectionType.ACHIEVEMENT)}">
                                <label for="${type.name()}">${type.title}</label>
                                <textarea id="${type.name()}" name="${type.name()}"><%=String.join("\n", ((BulletedListSection)
                                        section).getItems())%></textarea>
                            </c:when>
                        </c:choose>
                    </div>
                </c:forEach>
                <div class="action-button">
                    <button type="submit" id="submit">Сохранить</button>
                    <button type="button" onclick="window.history.back(); return false;">Отменить</button>
                </div>
            </form>
        </section>
    </main>
    <jsp:include page="fragments/footer.jsp"/>
</div>
</body>
</html>