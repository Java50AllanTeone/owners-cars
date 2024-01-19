package telran.cars.service;

import java.util.*;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import telran.cars.dto.*;
import telran.cars.service.model.*;

@Service("carsService")
@Scope("prototype")
public class CarsServiceImpl implements CarsService {
	HashMap<Long, CarOwner> owners = new HashMap<Long, CarOwner>();
	HashMap<String, Car> cars = new HashMap<String, Car>();

	@Override
	public PersonDto addPerson(PersonDto personDto) {
		CarOwner newOwner = new CarOwner(personDto);
		CarOwner actualOwner = owners.computeIfAbsent(personDto.id(), v -> newOwner);
		
		if (!newOwner.equals(actualOwner))
			throw new IllegalStateException("Person already exists");
		
		return personDto;
	}

	@Override
	public CarDto addCar(CarDto carDto) {
		Car newCar = new Car(carDto);
		Car actualCar = cars.computeIfAbsent(carDto.number(), v -> newCar);
		
		if (!newCar.equals(actualCar))
			throw new IllegalStateException("Car already exists");
		
		return carDto;
	}

	@Override
	public PersonDto updatePerson(PersonDto personDto) {
		CarOwner updOwner = owners.computeIfPresent(personDto.id(), (k, v) -> new CarOwner(personDto));
		
		if (updOwner == null)
			throw new IllegalStateException("Person is not exists");
		
		return personDto;
	}

	@Override
	public PersonDto deletePerson(long id) {
		CarOwner deletedOwner = owners.remove(id);
		
		if (deletedOwner == null)
			throw new IllegalStateException("Person is not exists");
			
		return deletedOwner.build();
	}

	@Override
	public CarDto deleteCar(String carNumber) {
		Car deletedCar = cars.remove(carNumber);
		
		if (deletedCar == null)
			throw new IllegalStateException("Car is not exists");
		
		return deletedCar.build();
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDeal) {
		Car car = cars.get(tradeDeal.carNumber());
		
		if (car == null)
			throw new IllegalStateException("Car is not exists");
		
		CarOwner prevOwner = car.getOwner(); 
		CarOwner newOwner = owners.get(tradeDeal.personId());
		
		if (newOwner == null)
			throw new IllegalStateException("New owner is not exists");
		
		newOwner.getCars().add(car);
		car.setOwner(newOwner);
		
		if (prevOwner != null)
			prevOwner.getCars().remove(car);
		
		return tradeDeal;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		try {
			return owners
					.get(id)
					.getCars()
					.stream()
					.map(c -> c.build())
					.toList();
		} catch (NullPointerException e) {
			throw new IllegalStateException("Person is not exists");
		}

	}

	@Override
	public PersonDto getCarOwner(String carNumber) {
		CarOwner owner;
		
		try {
			owner = cars.get(carNumber).getOwner();
		} catch (NullPointerException e) {
			throw new IllegalStateException("Car is not exists");
		}
		
		try {
			return owner.build();
		} catch (NullPointerException e) {
			return null;
		}
	}

}
