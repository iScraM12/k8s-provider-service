package com.iscram.k8s.provider.service.controll.car;

import com.iscram.k8s.provider.service.entitiy.car.Car;
import com.iscram.k8s.provider.service.entitiy.car.CarRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class CarService {

    @Inject
    CarRepository carRepository;

    @Transactional
    public Car createCar(Car car) {
        log.info("Received request to create a new car: {}", car);
        car.setId(null); // Ensure it's a new entity for POST
        carRepository.persist(car);
        log.info("Successfully persisted new car with id: {}", car.getId());
        return car;
    }

    @Transactional
    public Optional<Car> updateCar(Long id, Car carToUpdate) {
        log.info("Received request to update car with id {}: {}", id, carToUpdate);
        return carRepository.findByIdOptional(id)
                .map(existingCar -> {
                    existingCar.setBrand(carToUpdate.getBrand());
                    existingCar.setModel(carToUpdate.getModel());
                    existingCar.setColor(carToUpdate.getColor());
                    carRepository.persist(existingCar); // persist acts as merge for existing entities
                    log.info("Successfully updated car with id: {}", id);
                    return existingCar;
                });
    }

    @Transactional
    public boolean deleteCar(Long id) {
        log.info("Received request to delete car with id: {}", id);
        boolean deleted = carRepository.deleteById(id);
        if (deleted) {
            log.info("Successfully deleted car with id: {}", id);
        } else {
            log.warn("Car with id {} not found for deletion.", id);
        }
        return deleted;
    }

    public List<Car> getAllCars() {
        log.info("Fetching all cars from the database.");
        return carRepository.listAll();
    }

    public Car getCarById(Long id) {
        log.info("Fetching car with id: {}", id);
        return carRepository.findById(id);
    }
}
