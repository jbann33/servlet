package com.astontech.servlet;

import com.astontech.bo.Vehicle;
import com.astontech.bo.VehicleMake;
import com.astontech.bo.VehicleModel;
import com.astontech.dao.VehicleDAO;
import com.astontech.dao.VehicleMakeDAO;
import com.astontech.dao.VehicleModelDAO;
import com.astontech.dao.mysql.VehicleDAOImpl;
import com.astontech.dao.mysql.VehicleMakeDAOImpl;
import com.astontech.dao.mysql.VehicleModelDAOImpl;
import common.helpers.ServletHelper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "VehicleServlet")
public class VehicleServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(VehicleServlet.class);
    private static VehicleMakeDAO vehicleMakeDAO = new VehicleMakeDAOImpl();
    private static VehicleModelDAO vehicleModelDAO = new VehicleModelDAOImpl();
    private static VehicleDAO vehicleDAO = new VehicleDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletHelper.logRequestParams(request);
        switch (request.getParameter("formName")) {
            case "insertVehicle":
                insertVehicle(request);
                break;
            case "updateOrDeleteVehicle":
               if (request.getParameter("updateVehicle") != null) {
                   updateVehicle(request);
               } else if (request.getParameter("deleteVehicle") != null)
                   deleteVehicle(request);
               break;
        }
        request.setAttribute("vehicleList", vehicleDAO.getVehicleList());
        request.setAttribute("vehicleMakeList", vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList", vehicleModelDAO.getVehicleModelList());
        request.getRequestDispatcher("./vehicle.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("vehicleList", vehicleDAO.getVehicleList());
        request.setAttribute("vehicleMakeList", vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList", vehicleModelDAO.getVehicleModelList());
        request.getRequestDispatcher("./vehicle.jsp").forward(request, response);
    }

    private static void insertVehicle(HttpServletRequest request) {
        Vehicle vehicle = new Vehicle();
        requestToVehicle(request, vehicle);

        vehicleDAO.insertVehicle(vehicle);
    }

    private static void updateVehicle(HttpServletRequest request) {
        logger.info(request.getParameter("updateVehicle"));
        ServletHelper.logRequestParams(request);

        Vehicle updatedVehicle = new Vehicle();
        requestToVehicle(request, updatedVehicle);

        vehicleDAO.updateVehicle(updatedVehicle);

        logger.info(updatedVehicle.toString());
        if (vehicleDAO.updateVehicle(updatedVehicle))
            request.setAttribute("updateSuccessful", "Vehicle Updated in Database Successfully!");
        else
            request.setAttribute("updateSuccessful", "Vehicle Update FAILED!");
    }

    private static void deleteVehicle(HttpServletRequest request) {
        if (vehicleDAO.deleteVehicle(Integer.parseInt(request.getParameter("vehicleId"))))
            request.setAttribute("deleteSuccessful", "Vehicle Deleted Successfully!");
        else
            request.setAttribute("deleteSuccessful", "Vehicle Delete FAILED!");
    }

    private static String generateVehicleMakeDropDownHTML(int selectedVehicleMakeId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select name=\'vehicleMake\'>");
        sb.append("<option value='0'>(Select Vehicle Make)</option>");

        for (VehicleMake vehicleMake : vehicleMakeDAO.getVehicleMakeList()) {
            if (vehicleMake.getVehicleMakeId() == selectedVehicleMakeId)
                sb.append("<option selected value='").append(vehicleMake.getVehicleMakeId()).append("'>").append(vehicleMake.getVehicleMakeName()).append("</option>");
            else
                sb.append("<option value='").append(vehicleMake.getVehicleMakeId()).append("'>").append(vehicleMake.getVehicleMakeName()).append("</option>");
        }

        sb.append("</select>");

        return sb.toString();
    }

    private static String generateVehicleModelDropDownHTML(int selectedVehicleModelId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select name=\'vehicleModel\'>");
        sb.append("<option value='0'>(Select Vehicle Model)</option>");

        for (VehicleModel vehicleModel : vehicleModelDAO.getVehicleModelList()) {
            if (vehicleModel.getVehicleModelId() == selectedVehicleModelId)
                sb.append("<option selected value='").append(vehicleModel.getVehicleModelId()).append("'>").append(vehicleModel.getVehicleModelName()).append("</option>");
            else
                sb.append("<option value='").append(vehicleModel.getVehicleModelId()).append("'>").append(vehicleModel.getVehicleModelName()).append("</option>");
        }

        sb.append("</select>");

        return sb.toString();
    }

    private static void requestToVehicle(HttpServletRequest request, Vehicle vehicle) {
        vehicle.getVehicleModel().setVehicleModelId(Integer.parseInt(request.getParameter("selectVehicleModel")));
        if (request.getParameter("vehicleId") != null)
            vehicle.setVehicleId(Integer.parseInt(request.getParameter("vehicleId")));
        else
            vehicle.setVehicleId(0);

        if (request.getParameter("licensePlate") != null)
            vehicle.setLicensePlate(request.getParameter("licensePlate"));
        else
            vehicle.setLicensePlate(null);

        if (request.getParameter("vin") != null)
            vehicle.setVIN(request.getParameter("vin"));
        else
            vehicle.setVIN(null);

        if (request.getParameter("year") != null)
            vehicle.setYear(Integer.parseInt(request.getParameter("year")));
        else
            vehicle.setYear(Integer.parseInt("0"));

        if (request.getParameter("color") != null)
            vehicle.setColor(request.getParameter("color"));
        else
            vehicle.setColor(null);

    }

    private static void vehicleToRequest(HttpServletRequest request, Vehicle vehicle) {
        request.setAttribute("vehicleModelId", vehicle.getVehicleModel().getVehicleModelId());
        request.setAttribute("vehicleModelName", vehicle.getVehicleModel().getVehicleModelName());
        request.setAttribute("vehicleMakeId", vehicle.getVehicleModel().getVehicleMake().getVehicleMakeId());
        request.setAttribute("vehicleMakeName", vehicle.getVehicleModel().getVehicleMake().getVehicleMakeName());
        request.setAttribute("licensePlate", vehicle.getLicensePlate());
        request.setAttribute("vin", vehicle.getVIN());
        request.setAttribute("year", vehicle.getYear());
        request.setAttribute("color", vehicle.getColor());
    }

}
