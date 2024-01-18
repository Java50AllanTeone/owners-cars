package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.service.CarsService;

@SpringBootTest
class CarsServiceTest {
	private static final String MODEL = "model";
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-22-222";
	private static final Long PERSON_ID_1 = 123l;
	private static final String NAME1 = "name1";
	private static final String BIRTH_DATE1 = "2000-10-10";
	private static final String EMAIL_1 = "name1@gmail.com";
	private static final Long PERSON_ID_2 = 123l;
	private static final String NAME2 = "name2";
	private static final String BIRTH_DATE2 = "2000-10-10";
	private static final String EMAIL_2 = "name2@gmail.com";
	private static final Long PERSON_ID_NOT_EXISTS = 111111111l;;
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL);
	PersonDto person1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE1, EMAIL_1);
	PersonDto person2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE2, EMAIL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME2, BIRTH_DATE2, EMAIL_2);

	
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
	}

	@Test
	void testAddCar() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdatePerson() {
		fail("Not yet implemented");
	}

	@Test
	void testDeletePerson() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteCar() {
		fail("Not yet implemented");
	}

	@Test
	void testPurchase() {
		fail("Not yet implemented");
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
