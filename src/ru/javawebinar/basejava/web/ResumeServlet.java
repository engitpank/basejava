package ru.javawebinar.basejava.web;


import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.HTMLConverter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    private final static SimpleLineSection EMPTY_LINE_SECTION = new SimpleLineSection("");
    private final static BulletedListSection EMPTY_BULLETED_SECTION = new BulletedListSection("");
    private final static CompanySection.Experience EMPTY_EXPERIENCE = new CompanySection.Experience(YearMonth.now().getMonth().getValue()
            , YearMonth.now().getYear(), "", "");
    private final static CompanySection EMPTY_COMPANY = new CompanySection("", "", EMPTY_EXPERIENCE);

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
            if (isEmpty(value)) {
                resume.getContacts().remove(type);
            } else {
                resume.addContact(type, value);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (isEmpty(value) && values.length == 1) {
                resume.getSections().remove(type);
            } else {
                switch (type) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(type, new SimpleLineSection(value));
                    case QUALIFICATIONS, ACHIEVEMENT -> {
                        String[] items = value.trim().split("[\\s+]{2,}");
                        resume.addSection(type, new BulletedListSection(items));
                    } //\r\n|\r|
                    case EDUCATION, EXPERIENCE -> {
                        ArrayList<CompanySection> companySections = new ArrayList<>();
                        String[] companyNames = request.getParameterValues(type.name());
                        String[] companyUrls = request.getParameterValues(type.name() + "pageLink");
                        int indexOfDeletedSection = HTMLConverter.parseIntParameter(request.getParameter(type + "deletedSection"));
                        int indexOfAddedSection = HTMLConverter.parseIntParameter(request.getParameter(type + "addSection"));
                        int indexOfAddedExpSection = HTMLConverter.parseIntParameter(request.getParameter(type + "addExperience"));
                        String deletedExperience = request.getParameter(type + "deletedExperience");
                        String[] indexesExperience = deletedExperience != null ? deletedExperience.split(",") : new String[]{"-1", "-1"};
                        int indexOfDeletedExp = HTMLConverter.parseIntParameter(indexesExperience[1]);
                        int indexOfDeletedExpInCompany = HTMLConverter.parseIntParameter(indexesExperience[0]);
                        for (int i = 0; i < companyNames.length; i++) {
                            ArrayList<CompanySection.Experience> experiences = new ArrayList<>();
                            if (!isEmpty(companyNames[i]) && indexOfDeletedSection != i) {
                                String prefix = type.name() + i;
                                String[] startDates = request.getParameterValues(prefix + "startDate");
                                String[] finishDates = request.getParameterValues(prefix + "finishDate");
                                String[] titles = request.getParameterValues(prefix + "title");
                                String[] descriptions = request.getParameterValues(prefix + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!isEmpty(titles[j]) && !(indexOfDeletedExp == j && indexOfDeletedExpInCompany == i)) {
                                        experiences.add(new CompanySection.Experience(DateUtil.parse(startDates[j]),
                                                DateUtil.parse(finishDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                if (experiences.size() == 0) {
                                    experiences.add(EMPTY_EXPERIENCE);
                                }
                                companySections.add(new CompanySection(new Link(companyNames[i], companyUrls[i]), experiences));
                            }
                            if (indexOfAddedSection == i) {
                                companySections.add(EMPTY_COMPANY);
                            } else if (indexOfAddedExpSection == i) {
                                experiences.add(EMPTY_EXPERIENCE);
                            }
                        }
                        if (companySections.size() == 0) {
                            resume.addSection(type, new CompanyListSection(EMPTY_COMPANY));
                        } else {
                            resume.addSection(type, new CompanyListSection(companySections));
                        }

                    }
                }
            }
        }
        if (isNotCreatedResume) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        if (request.getParameter("saveForm") == null && request.getParameter("cancelForm") == null) {
            response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
        } else {
            response.sendRedirect("resume");
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
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
                resume.addSection(SectionType.OBJECTIVE, EMPTY_LINE_SECTION);
                resume.addSection(SectionType.PERSONAL, EMPTY_LINE_SECTION);
                resume.addSection(SectionType.ACHIEVEMENT, EMPTY_BULLETED_SECTION);
                resume.addSection(SectionType.QUALIFICATIONS, EMPTY_BULLETED_SECTION);
                resume.addSection(SectionType.EDUCATION, new CompanyListSection(EMPTY_COMPANY));
                resume.addSection(SectionType.EXPERIENCE, new CompanyListSection(EMPTY_COMPANY));
            }
            case "edit" -> {
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = resume.getSection(type);
                    switch (type) {
                        case PERSONAL, OBJECTIVE -> {
                            if (section == null) section = EMPTY_LINE_SECTION;
                        }
                        case QUALIFICATIONS, ACHIEVEMENT -> {
                            if (section == null) section = EMPTY_BULLETED_SECTION;
                        }
                        case EXPERIENCE, EDUCATION -> {
                            if (section == null) section = new CompanyListSection(EMPTY_COMPANY);
                        }
                    }
                    resume.addSection(type, section);
                }
            }
            default -> throw new IllegalArgumentException("Type " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(("view".equals(action)) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(request,
                response);
    }
}
