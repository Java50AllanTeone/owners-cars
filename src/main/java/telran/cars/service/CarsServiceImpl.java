package telran.cars.service;

import java.util.*;
import java.util.List;

import org.springframework.stereotype.Service;

import telran.cars.dto.*;
import telran.cars.service.model.*;

@Service
public class CarsServiceImpl implements CarsService {
	HashMap<Long, CarOwner> owners = new HashMap<Long, CarOwner>();
	HashMap<String, Car> cars = new HashMap<String, Car>();

	@Override
	public PersonDto addPerson(PersonDto personDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarDto addCar(CarDto carDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto updatePerson(PersonDto personDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto deletePerson(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarDto deleteCar(String carNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeDealDto purchase(TradeDealDto tradeDeal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonDto getCarOwner(String carNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}