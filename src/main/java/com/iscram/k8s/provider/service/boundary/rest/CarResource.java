package com.iscram.k8s.provider.service.boundary.rest;

import com.iscram.k8s.provider.service.controll.car.CarService;
import com.iscram.k8s.provider.service.entitiy.car.Car;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Objects;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@Tag(name = "Car Resource", description = "Operations related to cars")
public class CarResource {

    @Inject
    CarService carService;

    @GET
    @Operation(summary = "Get all cars", description = "Retrieves a list of all available cars.", operationId = "getAllCars")
    @APIResponse(responseCode = "200", description = "List of cars",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Car.class, type = SchemaType.ARRAY)))
    public List<Car> getAllCars() {
        log.info("Received HTTP GET request to /cars");
        return carService.getAllCars();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get car by ID", description = "Retrieves a single car by its unique identifier.", operationId = "getCarById")
    @APIResponse(responseCode = "200", description = "Car found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Car.class)))
    @APIResponse(responseCode = "404", description = "Car not found")
    public Response getCarById(@PathParam("id") Long id) {
        log.info("Received HTTP GET request to /cars/{}", id);
        Car car = carService.getCarById(id);
        if (Objects.isNull(car)) {
            log.warn("Car with id {} not found. Responding with HTTP 404 Not Found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(car).build();
    }

    @POST
    @Operation(summary = "Create a new car", description = "Creates a new car and returns the created car with its assigned ID.", operationId = "createCar")
    @APIResponse(responseCode = "201", description = "Car created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Car.class)))
    @APIResponse(responseCode = "400", description = "Invalid car data provided")
    public Response createCar(
            @RequestBody(description = "Car object to be created", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Car.class)))
            Car car) {
        log.info("Received HTTP POST request to /cars with payload: {}", car);
        Car createdCar = carService.createCar(car);
        log.info("Responding with HTTP 201 Created. New car ID: {}", createdCar.getId());
        return Response.status(Response.Status.CREATED).entity(createdCar).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing car", description = "Updates an existing car identified by its ID.", operationId = "updateCar")
    @APIResponse(responseCode = "200", description = "Car updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Car.class)))
    @APIResponse(responseCode = "404", description = "Car not found")
    @APIResponse(responseCode = "400", description = "Invalid car data provided")
    public Response updateCar(
            @PathParam("id") Long id,
            @RequestBody(description = "Updated car object", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Car.class)))
            Car car) {
        log.info("Received HTTP PUT request to /cars/{} with payload: {}", id, car);
        return carService.updateCar(id, car)
                .map(updatedCar -> {
                    log.info("Responding with HTTP 200 OK. Updated car ID: {}", updatedCar.getId());
                    return Response.ok(updatedCar).build();
                })
                .orElseGet(() -> {
                    log.warn("Car with id {} not found. Responding with HTTP 404 Not Found.", id);
                    return Response.status(Response.Status.NOT_FOUND).build();
                });
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a car by ID", description = "Deletes a car by its unique identifier.", operationId = "deleteCar")
    @APIResponse(responseCode = "204", description = "Car deleted successfully")
    @APIResponse(responseCode = "404", description = "Car not found")
    public Response deleteCar(@PathParam("id") Long id) {
        log.info("Received HTTP DELETE request to /cars/{}", id);
        boolean deleted = carService.deleteCar(id);
        if (deleted) {
            log.info("Responding with HTTP 204 No Content for car ID: {}", id);
            return Response.noContent().build();
        } else {
            log.warn("Car with id {} not found for deletion. Responding with HTTP 404 Not Found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
