package com.astontech.servlet;

import com.astontech.bo.Person;
import com.astontech.dao.PersonDAO;
import com.astontech.dao.mysql.PersonDAOImpl;
import common.helpers.ServletHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PersonServlet extends javax.servlet.http.HttpServlet {
    final static Logger logger = Logger.getLogger(PersonServlet.class);
    private static PersonDAO personDAO = new PersonDAOImpl();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        switch(request.getParameter("formName")) {
            case "choosePerson":
                choosePerson(request);
                break;
            case "updatePerson":
                updatePerson(request);
                break;
            default:
                break;
        }

        // notes:   generate person dropdown using JSTL (logic is the same between forms)
        request.setAttribute("personList", personDAO.getPersonList());
        request.getRequestDispatcher("./person.jsp").forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setAttribute("personList", personDAO.getPersonList());
        request.setAttribute("selectPerson", generatePersonDropDownHTML(0));
        request.getRequestDispatcher("./person.jsp").forward(request, response);
    }

    private static void choosePerson(HttpServletRequest request) {
        logger.info("Form #1 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        //notes:    everything comes back from the request as a STRING
        String selectedPersonId = request.getParameter("selectPerson");

        Person selectedPerson = personDAO.getPersonById(Integer.parseInt(selectedPersonId));

        logger.info("Selected Person Details: " + selectedPerson.toString());

        personToRequest(request, selectedPerson);

        request.setAttribute("selectPerson", generatePersonDropDownHTML(selectedPerson.getPersonId()));
    }

    private static void updatePerson(HttpServletRequest request) {
        logger.info("Form #2 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        Person updatePerson = new Person();
        requestToPerson(request, updatePerson);

        logger.info(updatePerson.toString());
        if (personDAO.updatePerson(updatePerson))
            request.setAttribute("updateSuccessful", "Person Updated in Database Successfully!");
        else
            request.setAttribute("updateSuccessful", "Person Update FAILED!");

        // notes:   inefficient! extra call to the database
        updatePerson = personDAO.getPersonById(updatePerson.getPersonId());
        personToRequest(request, updatePerson);

        String personIdString = request.getParameter("personId");
        request.setAttribute("selectedPerson", generatePersonDropDownHTML(Integer.parseInt(personIdString)));
    }

    private static String generatePersonDropDownHTML(int selectedPersonId) {
        StringBuilder strBld = new StringBuilder();
        strBld.append("<select name=\'selectPerson\'>");
        strBld.append("<option value='0'>(Select Person)</option>");

        for (Person person : personDAO.getPersonList()) {
            if (person.getPersonId() == selectedPersonId)
                strBld.append("<option selected value='").append(person.getPersonId()).append("'>").append(person.GetFullName()).append("</option>");
            else
            strBld.append("<option value='").append(person.getPersonId()).append("'>").append(person.GetFullName()).append("</option>");
        }

        strBld.append("</select>");

        return strBld.toString();
    }

    private static void requestToPerson(HttpServletRequest request, Person person) {
        // notes:   everything comes back from the request as a STRING
        person.setPersonId(Integer.parseInt(request.getParameter("personId")));
        person.setFirstName(request.getParameter("firstName"));
        person.setLastName(request.getParameter("lastName"));
    }

    private static void personToRequest(HttpServletRequest request, Person person) {
        request.setAttribute("personId", person.getPersonId());
        request.setAttribute("firstName", person.getFirstName());
        request.setAttribute("lastName", person.getLastName());
    }

}
