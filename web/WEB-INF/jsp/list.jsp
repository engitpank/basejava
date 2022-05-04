<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<div class="layout-wrapper">
    <jsp:include page="fragments/header.jsp"/>
    <main>
        <section class="resume">
            <a href="resume?action=add">Добавить резюме</a>
            <table class="resume-list">
                <tr>
                    <th>Имя</th>
                    <th>Email</th>
                    <th></th>
                    <th></th>
                </tr>
                <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
                <c:forEach items="${resumes}" var="resume">
                    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume"/>
                    <tr>
                        <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                        <td>${resume.getContact(ContactType.MAIL)}</td>
                        <td><a href="resume?uuid=${resume.uuid}&action=delete">Удалить</a></td>
                        <td><a href="resume?uuid=${resume.uuid}&action=edit">Редактировать</a></td>
                    </tr>
                </c:forEach>

            </table>
        </section>
    </main>
    <jsp:include page="fragments/footer.jsp"/>
</div>
</body>
</html>
