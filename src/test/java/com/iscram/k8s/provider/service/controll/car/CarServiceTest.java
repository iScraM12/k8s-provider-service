package com.iscram.k8s.provider.service.controll.car;

import com.iscram.k8s.provider.service.entitiy.car.Car;
import com.iscram.k8s.provider.service.entitiy.car.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void createCar_shouldSetIdToNullAndPersist() {
        // Arrange
        Car carToCreate = Car.builder().id(123L).brand("Test").model("Model").color("Color").build();
        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        // Act
        Car createdCar = carService.createCar(carToCreate);

        // Assert
        verify(carRepository).persist(carCaptor.capture());
        Car capturedCar = carCaptor.getValue();

        assertNull(capturedCar.getId(), "ID should be null before persisting.");
        assertEquals("Test", capturedCar.getBrand());
        assertNotNull(createdCar, "Returned car should not be null.");
    }

    @Test
    void updateCar_whenCarExists_shouldUpdateAndPersist() {
        // Arrange
        Long carId = 1L;
        Car existingCar = new Car(carId, "Old Brand", "Old Model", "Old Color");
        Car carToUpdate = Car.builder().brand("New Brand").model("New Model").color("New Color").build();

        when(carRepository.findByIdOptional(carId)).thenReturn(Optional.of(existingCar));

        // Act
        Optional<Car> updatedCarOptional = carService.updateCar(carId, carToUpdate);

        // Assert
        assertTrue(updatedCarOptional.isPresent(), "Updated car should be present.");
        Car updatedCar = updatedCarOptional.get();

        assertEquals("New Brand", updatedCar.getBrand());
        assertEquals("New Model", updatedCar.getModel());
        assertEquals("New Color", updatedCar.getColor());
        verify(carRepository).persist(existingCar); // Verify persist was called on the modified object
    }

    @Test
    void updateCar_whenCarDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        Long carId = 99L;
        Car carToUpdate = Car.builder().brand("New Brand").build();
        when(carRepository.findByIdOptional(carId)).thenReturn(Optional.empty());

        // Act
        Optional<Car> result = carService.updateCar(carId, carToUpdate);

        // Assert
        assertTrue(result.isEmpty(), "Result should be an empty Optional.");
        verify(carRepository, never()).persist(any(Car.class)); // Verify persist was never called
    }

    @Test
    void getAllCars_shouldReturnListOfCars() {
        // Arrange
        List<Car> expectedCars = List.of(new Car(1L, "A", "B", "C"), new Car(2L, "D", "E", "F"));
        when(carRepository.listAll()).thenReturn(expectedCars);

        // Act
        List<Car> actualCars = carService.getAllCars();

        // Assert
        assertEquals(2, actualCars.size());
        assertEquals(expectedCars, actualCars);
        verify(carRepository).listAll();
    }

    @Test
    void getCarById_whenCarExists_shouldReturnCar() {
        // Arrange
        Long carId = 1L;
        Car expectedCar = new Car(carId, "A", "B", "C");
        when(carRepository.findById(carId)).thenReturn(expectedCar);

        // Act
        Car actualCar = carService.getCarById(carId);

        // Assert
        assertNotNull(actualCar);
        assertEquals(carId, actualCar.getId());
        verify(carRepository).findById(carId);
    }

    @Test
    void getCarById_whenCarDoesNotExist_shouldReturnNull() {
        // Arrange
        Long carId = 99L;
        when(carRepository.findById(carId)).thenReturn(null);

        // Act
        Car actualCar = carService.getCarById(carId);

        // Assert
        assertNull(actualCar);
        verify(carRepository).findById(carId);
    }
}
