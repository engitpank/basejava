package ru.javawebinar.basejava.web;


import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    Storage storage = Config.get().getStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        PrintWriter out = response.getWriter();
        if (uuid == null) {
            List<Resume> allResume = storage.getAllSorted();
            out.write("<table> <tr>" +
                    "    <th>UUID</th>" +
                    "    <th>FULLNAME</th>" +
                    "  </tr>");
            for (Resume resume : allResume) {
                out.write("  <tr>" +
                        "    <td>" + resume.getUuid() + "</td>" +
                        "    <td>" + resume.getFullName() + "</td>" +
                        "  </tr>");
            }
            out.write("</table>");
        } else {
            Resume resume = storage.get(uuid);
            out.write("<h3>" + resume.getUuid() + ": " + resume.getFullName() + "</h3>");
        }
    }
}
