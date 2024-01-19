package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.service.CarsService;

@SpringBootTest
class CarsServiceTest {
	private static final String MODEL = "model";
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-22-222";
	private static final String CAR_NUMBER_3 = "333-33-333";
	private static final Long PERSON_ID_1 = 123l;
	private static final String NAME1 = "name1";
	private static final String BIRTH_DATE1 = "2000-10-10";
	private static final String EMAIL_1 = "name1@gmail.com";
	private static final Long PERSON_ID_2 = 123l;
	private static final String NAME2 = "name2";
	private static final String BIRTH_DATE2 = "2000-10-10";
	private static final String EMAIL_2 = "name2@gmail.com";
	private static final Long PERSON_ID_NOT_EXISTS = 111111111l;
	private static final String CAR_NUMBER_NOT_EXISTS = "444-44-44";
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL);
	CarDto car3 = new CarDto(CAR_NUMBER_3, MODEL);
	PersonDto person1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE1, EMAIL_1);
	PersonDto person2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE2, EMAIL_2);
	PersonDto personUpdated = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE2, EMAIL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME2, BIRTH_DATE2, EMAIL_2);
	CarDto carDto = new CarDto(CAR_NUMBER_NOT_EXISTS, MODEL);

	
	@Autowired
	CarsService carsService;
	
	@BeforeEach
	void setUp() {
		carsService.addCar(car1);
		carsService.addCar(car2);
		carsService.addPerson(person1);
		carsService.addPerson(person2);
		carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_1));
		carsService.purchase(new TradeDealDto(CAR_NUMBER_2, PERSON_ID_2));
	}

	@Test
	void testAddPerson() {
		assertEquals(personDto, carsService.addPerson(personDto));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addPerson(person1));
		
		carsService.deletePerson(person1.id());
		carsService.deletePerson(person2.id());
		carsService.deletePerson(personDto.id());
		carsService.deleteCar(car1.number());
		carsService.deleteCar(car2.number());
	}

	@Test
	void testAddCar() {
		assertEquals(carDto, carsService.addCar(carDto));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addCar(car1));
		
		carsService.deletePerson(person1.id());
		carsService.deletePerson(person2.id());
		carsService.deleteCar(carDto.number());
		carsService.deleteCar(car1.number());
		carsService.deleteCar(car2.number());
	}

	@Test
	void testUpdatePerson() {
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.updatePerson(personDto));
		
		carsService.deletePerson(person1.id());
		carsService.deletePerson(person2.id());
		carsService.deleteCar(car1.number());
		carsService.deleteCar(car2.number());
	}

	@Test
	void testDeletePerson() {
		assertEquals(person1, carsService.deletePerson(person1.id()));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.deletePerson(personDto.id()));

		carsService.deletePerson(person2.id());
		carsService.deleteCar(car1.number());
		carsService.deleteCar(car2.number());
	}

	@Test
	void testDeleteCar() {
		assertEquals(car1, carsService.deleteCar(car1.number()));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.deleteCar(carDto.number()));
		
		carsService.deletePerson(person1.id());
		carsService.deletePerson(person2.id());
		carsService.deleteCar(car2.number());
	}

	@Test
	void testPurchase() {
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(new TradeDealDto(CAR_NUMBER_NOT_EXISTS, PERSON_ID_1)));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_NOT_EXISTS)));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_1)));
		
		carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2));
		
		assertFalse(carsService.getOwnerCars(person1.id()).contains(car1));
		assertTrue(carsService.getOwnerCars(person2.id()).contains(car1));
		assertEquals(person2, carsService.getCarOwner(car1.number())); 
	}

	@Test
	void testGetOwnerCars() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCarOwner() {
		fail("Not yet implemented");
	}

}
