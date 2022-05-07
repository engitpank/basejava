<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                    <div class="section-block">
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
                            <c:when test="${type.equals(SectionType.EXPERIENCE) || type.equals(SectionType.EDUCATION)}">
                                <c:forEach var="company" items="<%=((CompanyListSection) section).getCompanySections()%>"
                                           varStatus="companyLoop">
                                    <div class="company-block">
                                        <div class="homePage-item">
                                            <div class="homePage-block">
                                                <label>Название компании</label>
                                                <input type="text" name="${type.name()}"
                                                       value="${company.homePage.name}"/>
                                            </div>
                                            <div class="homePage-item">
                                                <label>Ссылка на сайт (при наличии)</label>
                                                <input type="text" name="${type.name()}pageLink"
                                                       value="${company.homePage.link}"/>
                                            </div>
                                        </div>
                                        <c:forEach var="experience" items="${company.experienceList}" varStatus="experienceLoop">
                                            <jsp:useBean id="experience" type="ru.javawebinar.basejava.model.CompanySection.Experience"/>
                                            <div class="experience-block-input">
                                                <div class="experience-date flex-column">
                                                    <label>Начало работы\учёбы</label>
                                                    <input type="text" class="date" name="${type.name()}${companyLoop.index}startDate"
                                                           placeholder="Введите дату в формате: 2020-04"
                                                           value="<%=DateUtil.format(experience.getStartDate())%>"/>
                                                    <label>Окончание работы\учёбы</label>
                                                    <input type="text" class="date finishDate"
                                                           name="${type.name()}${companyLoop.index}finishDate"
                                                           placeholder="Введите дату в формате: 2023-01"
                                                           value="<%=DateUtil.format(experience.getFinishDate())%>"/>
                                                    <label class="checkbox-label">По настоящее время
                                                        <input type="checkbox" class="date-checkbox" value="По настоящее время"
                                                               name="${type.name()}${companyLoop.index}finishDate"/>
                                                    </label>
                                                </div>
                                                <div class="experience-description-input">
                                                    <label>Должность</label>
                                                    <input type="text" name="${type.name()}${companyLoop.index}title"
                                                           value="${experience.title}"/>
                                                    <label>Описание</label>
                                                    <textarea
                                                            name="${type.name()}${companyLoop.index}description">${experience.description}</textarea>
                                                </div>
                                                <div class="experience-button-block">
                                                    <button name="${type.name()}addExperience" value="${companyLoop.index}">
                                                        <img alt="добавить опыт" src="img/add.png" height="25px"/>
                                                    </button>
                                                    <button name="${type.name()}deletedExperience"
                                                            value="${companyLoop.index},${experienceLoop.index}">
                                                        <img alt="удалить опыт"  src="img/delete.png" height="25px"/>
                                                    </button>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <button class="section-button"
                                                name="${type.name()}addSection" value="${companyLoop.index}">Добавить секцию
                                        </button>
                                        <button class="section-button"
                                                name="${type.name()}deletedSection" value="${companyLoop.index}">Удалить секцию
                                        </button>
                                        <hr>
                                    </div>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </div>
                </c:forEach>
                <div class="action-button">
                    <button type="submit" id="submit" class="blue-button" name="saveForm" value="submit">Сохранить</button>
                    <button type="button" onclick="window.history.back(); return false;" class="red-button" name="cancelForm"
                            value="cancel">Отменить
                    </button>
                </div>
            </form>
        </section>
    </main>
    <jsp:include page="fragments/footer.jsp"/>
</div>
<script src="scripts/inputValidator.js"></script>
</body>
</html>