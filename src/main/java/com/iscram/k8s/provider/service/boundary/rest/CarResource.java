package com.iscram.k8s.provider.service.boundary.rest;

import com.iscram.k8s.provider.service.controll.car.CarService;
import com.iscram.k8s.provider.service.entitiy.car.Car;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class CarResource {

    @Inject
    CarService carService;

    @GET
    public List<Car> getAllCars() {
        log.info("Received HTTP GET request to /cars");
        return carService.getAllCars();
    }

    @GET
    @Path("/{id}")
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
    public Response createCar(Car car) {
        log.info("Received HTTP POST request to /cars with payload: {}", car);
        Car createdCar = carService.createCar(car);
        log.info("Responding with HTTP 201 Created. New car ID: {}", createdCar.getId());
        return Response.status(Response.Status.CREATED).entity(createdCar).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCar(@PathParam("id") Long id, Car car) {
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
}
