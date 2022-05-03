<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.SimpleLineSection" %>
<%@ page import="ru.javawebinar.basejava.util.HTMLConverter" %>
<%@ page import="ru.javawebinar.basejava.model.BulletedListSection" %>
<%@ page import="ru.javawebinar.basejava.model.CompanyListSection" %>
<%@ page import="ru.javawebinar.basejava.model.AbstractSection" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp; <a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <p>
        <c:forEach items="${resume.contacts}" var="contactEntry">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            ${HTMLConverter.convertContactToHTML(contactEntry.key,contactEntry.value)}<br>
        </c:forEach>
    </p>
    <p>
    <h3>Секции: </h3>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <c:set var="section" value="${resume.getSection(sectionType)}"/>
        <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
        <div class="company">
            <h3>${sectionType.title}:</h3>
            <c:choose>
                <c:when test="${sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)}">
                    <%=((SimpleLineSection) section).getText() %>
                </c:when>
                <c:when test="${sectionType.equals(SectionType.QUALIFICATIONS) || sectionType.equals(SectionType.ACHIEVEMENT)}">
                    <ul>
                        <c:forEach var="item" items="<%=((BulletedListSection) section).getItems() %>">
                            <li>
                                    ${item}
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)}">
                    <c:forEach var="company" items="<%=((CompanyListSection) section).getCompanySections()%>">
                        <div class="company">
                            <h3><a href="${company.homePage.link}">${company.homePage.name}</a></h3>
                        </div>
                        <c:forEach var="experience" items="${company.experienceList}">
                            <h4>${experience.title}</h4>
                            <p>${experience.startDate} - ${experience.finishDate}</p>
                            <p>${experience.description}</p>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
                <%--            <p>${resume.getSection(section)}</p>--%>
        </div>
    </c:forEach>
    <jsp:include page="fragments/footer.jsp"/>
</section>
</body>
</html>