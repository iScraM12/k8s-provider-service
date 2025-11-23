package com.iscram.k8s.provider.service.boundary.rest;

import com.iscram.k8s.provider.service.entitiy.car.Car;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class CarResourceTest {

    @Test
    void testGetAllCarsEndpoint_whenNoCars_returnsEmptyList() {
        // Arrange (no setup needed)

        // Act & Assert
        given()
                .when().get("/cars")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void testCreateCarEndpoint_whenValidCar_createsAndReturnsCar() {
        // Arrange
        Car newCar = Car.builder().brand("Tesla").model("Model Y").color("Black").build();

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(newCar)
                .when().post("/cars")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("brand", is("Tesla"))
                .body("color", is("Black"));
    }

    @Test
    void testGetCarByIdEndpoint_whenCarExists_returnsCar() {
        // Arrange
        Car newCar = Car.builder().brand("Porsche").model("911").color("Silver").build();
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(newCar)
                .when().post("/cars")
                .then()
                .statusCode(201)
                .extract().path("id");
        Long carId = id.longValue();

        // Act & Assert
        given()
                .when().get("/cars/" + carId)
                .then()
                .statusCode(200)
                .body("id", is(carId.intValue()))
                .body("model", is("911"));
    }

    @Test
    void testGetCarByIdEndpoint_whenCarDoesNotExist_returnsNotFound() {
        // Arrange (no setup needed)

        // Act & Assert
        given()
                .when().get("/cars/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateCarEndpoint_whenCarExists_updatesAndReturnsCar() {
        // Arrange
        Car originalCar = Car.builder().brand("BMW").model("M3").color("Blue").build();
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(originalCar)
                .when().post("/cars")
                .then()
                .statusCode(201)
                .extract().path("id");
        Long carId = id.longValue();
        Car updatedCar = Car.builder().brand("BMW").model("M3 Competition").color("Brooklyn Grey").build();

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(updatedCar)
                .when().put("/cars/" + carId)
                .then()
                .statusCode(200)
                .body("model", is("M3 Competition"))
                .body("color", is("Brooklyn Grey"));
    }

    @Test
    void testUpdateCarEndpoint_whenCarDoesNotExist_returnsNotFound() {
        // Arrange
        Car car = Car.builder().brand("Fake").model("Fakemobil").build();

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(car)
                .when().put("/cars/99999")
                .then()
                .statusCode(404);
    }
}
