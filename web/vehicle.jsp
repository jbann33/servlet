<%--
  Created by IntelliJ IDEA.
  User: jbann33
  Date: 4/2/18
  Time: 1:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="./static/css/vehicle.css" />
    <title>Vehicle Management</title>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
<div class="border">
    <div class="col" style="padding-left: 5px">
        <h4 style="text-decoration: underline">Add a Vehicle:</h4>
    </div>
    <form name="insertVehicle" action="./vehicle" method="post">
        <input type="hidden" name="formName" value="insertVehicle"/>
            <select name="selectVehicleMake">
                <option value='0'>(Select Vehicle Make)</option>
                    <c:forEach var="vehicleMake" items="${vehicleMakeList}">
                        <option value="${vehicleMake.vehicleMakeId}">${vehicleMake.vehicleMakeName} </option>
                    </c:forEach>
            </select>

            <select name="selectVehicleModel">
                <option value='0'>(Select Vehicle Model)</option>
                    <c:forEach var="vehicleModel" items="${vehicleModelList}">
                        <option value="${vehicleModel.vehicleModelId}">${vehicleModel.vehicleModelName} </option>
                    </c:forEach>
            </select>

            <div>
                <input type="text" class="form-control" name="licensePlate" value="${licensePlate}" placeholder="License Plate" />
            </div>
            <div>
                <input type="text" class="form-control" name="vin" value="${vin}" placeholder="VIN" />
            </div>
            <div>
                <input type="text" class="form-control" name="year" value="${year}" placeholder="Year" />
            </div>
            <div>
                <input type="text" class="form-control" name="color" value="${color}" placeholder="Color" />
            </div>
            <button type="submit">Add Vehicle</button>
    </form>
</div>
   <br>
<div style="padding-left: 25px">
    ${updateSuccessful}
    ${deleteSuccessful}
</div>
<br>
<div>
    <div class="form-row">
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Plate</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">VIN</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Year</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Color</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Make</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Model</h4>
        </div>
        <div class="col" style="padding-left: 25px">
            <h4 style="text-decoration: underline">Actions</h4>
        </div>
        <hr style="height: 5px">
    </div>
    <c:forEach var="vehicle" items="${requestScope.vehicleList}">
    <form name="updateOrDeleteVehicle" action="./vehicle" method="post">
        <input type="hidden" name="formName" value="updateOrDeleteVehicle" />
        <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}" />
        <div class="form-row">
            <div class="col" style="padding-left: 25px">
                    <input type="text" class="form-control" name="licensePlate" value="<c:out value="${vehicle.licensePlate}" />" />
            </div>
            <div class="col">
                    <input type="text" class="form-control" name="vin" value="<c:out value="${vehicle.VIN}" />" />
            </div>
            <div class="col">
                    <input type="text" class="form-control" name="year" value="<c:out value="${vehicle.year}" />" />
            </div>
            <div class="col">
                    <input type="text" class="form-control" name="color" value="<c:out value="${vehicle.color}" />" />
            </div>
            <div class="col">
                    <select name="selectVehicleMake">
                        <option value='0'>(Select Vehicle Make)</option>
                            <c:forEach var="vehicleMake" items="${requestScope.vehicleMakeList}">
                                <c:choose>
                                    <c:when test="${vehicle.vehicleModel.vehicleMake.vehicleMakeId == vehicleMake.vehicleMakeId}">
                                        <option selected value="${vehicleMake.vehicleMakeId}">${vehicleMake.vehicleMakeName} </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${vehicleMake.vehicleMakeId}">${vehicleMake.vehicleMakeName} </option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                    </select>
            </div>
            <div class="col">
                    <select name="selectVehicleModel">
                        <option value='0'>(Select Vehicle Model)</option>
                            <c:forEach var="vehicleModel" items="${requestScope.vehicleModelList}">
                                <c:choose>
                                    <c:when test="${vehicle.vehicleModel.vehicleModelId == vehicleModel.vehicleModelId}">
                                        <option selected value="${vehicleModel.vehicleModelId}">${vehicleModel.vehicleModelName} </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${vehicleModel.vehicleModelId}">${vehicleModel.vehicleModelName} </option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                    </select>
            </div>
            <div class="col">
                <br>
                <button name="updateVehicle" value="updateVehicle" type="submit" class="btn btn-secondary" style="position: relative; top: -25px">Update</button>
                <button name="deleteVehicle" value="deleteVehicle" type="submit" class="btn btn-danger" style="position: relative; top: -25px">Delete</button>
                <br>

            </div>
        </div>
    </form>
    </c:forEach>

</div>

</body>
</html>
