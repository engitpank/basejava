package ru.javawebinar.basejava.web;


import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        boolean isNotCreatedResume = uuid == null || uuid.length() == 0;
        if (isNotCreatedResume) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value == null) {
                resume.getSections().remove(type);
            } else {
                switch (type) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(type, new SimpleLineSection(value));
                    case QUALIFICATIONS, ACHIEVEMENT -> resume.addSection(type,
                            new BulletedListSection(value.split("\\n"))); //\r\n|\r|
                }
            }
        }
        if (isNotCreatedResume) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view" -> resume = storage.get(uuid);
            case "add" -> {
                resume = new Resume();
                resume.addSection(SectionType.OBJECTIVE, new SimpleLineSection());
                resume.addSection(SectionType.PERSONAL, new SimpleLineSection());
                resume.addSection(SectionType.ACHIEVEMENT, new BulletedListSection(""));
                resume.addSection(SectionType.QUALIFICATIONS, new BulletedListSection(""));
                resume.addSection(SectionType.EDUCATION, new CompanyListSection(new CompanySection("", "", new CompanySection.Experience())));
                resume.addSection(SectionType.EXPERIENCE, new CompanyListSection(new CompanySection("", "", new CompanySection.Experience())));
            }
            case "edit" -> {
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = resume.getSection(type);
                    switch (type) {
                        case PERSONAL, OBJECTIVE -> {
                            if (section == null) section = new SimpleLineSection("");
                        }
                        case QUALIFICATIONS, ACHIEVEMENT -> {
                            if (section == null) section = new BulletedListSection();
                        }
                        case EXPERIENCE, EDUCATION -> {
                            if (section == null) section = new CompanyListSection(new CompanySection("", "",
                                    new CompanySection.Experience()));
                        }
                    }
                    resume.addSection(type, section);
                }
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(("view".equals(action)) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(request,
                response);

    }
}
