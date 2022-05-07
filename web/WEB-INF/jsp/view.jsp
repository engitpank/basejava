<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.SimpleLineSection" %>
<%@ page import="ru.javawebinar.basejava.util.HTMLConverter" %>
<%@ page import="ru.javawebinar.basejava.model.BulletedListSection" %>
<%@ page import="ru.javawebinar.basejava.model.CompanyListSection" %>
<%@ page import="ru.javawebinar.basejava.model.AbstractSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<div class="layout-wrapper">
    <jsp:include page="fragments/header.jsp"/>
    <main>
        <div class="resume">
            <h1 id="fullname">${resume.fullName}&nbsp; <a href="resume?uuid=${resume.uuid}&action=edit">Редактировать</a></h1>
            <div class="contact-block">
                <h2>Контакты: </h2>
                <c:forEach items="${resume.contacts}" var="contactEntry">
                    <jsp:useBean id="contactEntry" type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
                    <p>${HTMLConverter.convertContactToHTML(contactEntry.key,contactEntry.value)}</p>
                </c:forEach>
            </div>
            <hr>
            <div class="section-block">
                <h2>Секции: </h2>
                <c:forEach var="sectionEntry" items="${resume.sections}">
                    <c:set var="type" value="${sectionEntry.key}"/>
                    <c:set var="section" value="${resume.getSection(type)}"/>
                    <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
                    <div class="section-item">
                        <h3>${type.title}:</h3>
                        <c:choose>
                            <c:when test="${type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)}">
                                <%=((SimpleLineSection) section).getText() %>
                            </c:when>
                            <c:when test="${type.equals(SectionType.QUALIFICATIONS) || type.equals(SectionType.ACHIEVEMENT)}">
                                <ul>
                                    <c:forEach var="item" items="<%=((BulletedListSection) section).getItems() %>">
                                        <li>${item}</li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:when test="${type.equals(SectionType.EXPERIENCE) || type.equals(SectionType.EDUCATION)}">
                                <c:forEach var="company" items="<%=((CompanyListSection) section).getCompanySections()%>">
                                    <div class="company-block">
                                        <c:choose>
                                            <c:when test="${empty company.homePage.link}">
                                                <h3><a href="#">${company.homePage.name}</a></h3>
                                            </c:when>
                                            <c:otherwise>
                                                <h3><a href="${company.homePage.link}">${company.homePage.name}</a></h3>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:forEach var="experience" items="${company.experienceList}">
                                            <jsp:useBean id="experience" type="ru.javawebinar.basejava.model.CompanySection.Experience"/>
                                            <div class="experience">
                                                <div class="experience-date">
                                                    <span><%=DateUtil.format(experience.getStartDate())%> - <%=DateUtil.format(experience.getFinishDate())%></span>
                                                </div>
                                                <div class="experience-description">
                                                    <h4>${experience.title}</h4>
                                                    <span>${experience.description}</span>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
    <jsp:include page="fragments/footer.jsp"/>
</div>
</body>
</html>