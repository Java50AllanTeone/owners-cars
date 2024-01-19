package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.service.CarsService;

@SpringBootTest
class CarsServiceTest {
	private static final String MODEL1 = "model1";
	private static final String MODEL2 = "model2";
	private static final String MODEL3 = "model3";
	private static final String CAR_NUMBER_1 = "111-11-111";
	private static final String CAR_NUMBER_2 = "222-22-222";
	private static final String CAR_NUMBER_3 = "333-33-333";
	private static final Long PERSON_ID_1 = 123l;
	private static final String NAME1 = "name1";
	private static final String BIRTH_DATE1 = "2000-10-10";
	private static final String EMAIL_1 = "name1@gmail.com";
	private static final Long PERSON_ID_2 = 124l;
	private static final String NAME2 = "name2";
	private static final String BIRTH_DATE2 = "2000-10-10";
	private static final String EMAIL_2 = "name2@gmail.com";
	private static final Long PERSON_ID_NOT_EXISTS = 111111111l;
	private static final String CAR_NUMBER_NOT_EXISTS = "444-44-44";
	CarDto car1 = new CarDto(CAR_NUMBER_1, MODEL1);
	CarDto car2 = new CarDto(CAR_NUMBER_2, MODEL2);
	CarDto car3 = new CarDto(CAR_NUMBER_3, MODEL3);
	PersonDto person1 = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE1, EMAIL_1);
	PersonDto person2 = new PersonDto(PERSON_ID_2, NAME2, BIRTH_DATE2, EMAIL_2);
	PersonDto personUpdated = new PersonDto(PERSON_ID_1, NAME1, BIRTH_DATE2, EMAIL_2);
	PersonDto personDto = new PersonDto(PERSON_ID_NOT_EXISTS, NAME2, BIRTH_DATE2, EMAIL_2);
	CarDto carDto = new CarDto(CAR_NUMBER_NOT_EXISTS, MODEL1);

	
	@Autowired
	ApplicationContext ctx;
	CarsService carsService;
	
	@BeforeEach
	void setUp() {
		carsService = ctx.getBean("carsService", CarsService.class);
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
		assertEquals(carDto, carsService.addCar(carDto));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.addCar(car1));
	}

	@Test
	void testUpdatePerson() {
		assertEquals(personUpdated, carsService.updatePerson(personUpdated));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.updatePerson(personDto));
	}

	@Test
	void testDeletePerson() {
		assertEquals(person1, carsService.deletePerson(person1.id()));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.deletePerson(personDto.id()));
	}

	@Test
	void testDeleteCar() {
		assertEquals(car1, carsService.deleteCar(car1.number()));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.deleteCar(carDto.number()));
	}

	@Test
	void testPurchase() {
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(new TradeDealDto(CAR_NUMBER_NOT_EXISTS, PERSON_ID_1)));
		assertThrowsExactly(IllegalStateException.class, () -> carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_NOT_EXISTS)));
		
		carsService.purchase(new TradeDealDto(CAR_NUMBER_1, PERSON_ID_2));
		assertFalse(carsService.getOwnerCars(person1.id()).contains(car1));
		assertTrue(carsService.getOwnerCars(person2.id()).contains(car1));
		assertEquals(person2, carsService.getCarOwner(car1.number())); 
	}

	@Test
	void testGetOwnerCars() {
		assertThrowsExactly(IllegalStateException.class, () -> carsService.getOwnerCars(personDto.id()));
		
		assertArrayEquals(new CarDto[]{car1}, carsService.getOwnerCars(person1.id()).toArray());
		
		carsService.addPerson(personDto);
		assertArrayEquals(new CarDto[]{}, carsService.getOwnerCars(personDto.id()).toArray());
	}

	@Test
	void testGetCarOwner() {
		assertThrowsExactly(IllegalStateException.class, () -> carsService.getCarOwner(carDto.number()));

		carsService.addCar(carDto);	
		assertNull(carsService.getCarOwner(carDto.number()));
		assertEquals(person1, carsService.getCarOwner(car1.number()));
	}
	
	@Test
	void mostPopularCarModels_correct() {
		carsService.addCar(car3);
		carsService.addPerson(personDto);
		carsService.purchase(new TradeDealDto(car3.number(), personDto.id()));
		
		String[] expected = new String[] {car1.model(), car2.model(), car3.model()};
		String[] result = carsService.mostPopularCarModels().toArray(String[]::new);
		Arrays.sort(result);
		assertArrayEquals(expected, result);
		
		carsService.purchase(new TradeDealDto(car1.number(), personDto.id()));
		expected = new String[] {car1.model()};
		result = carsService.mostPopularCarModels().toArray(String[]::new);
		Arrays.sort(result);
		assertArrayEquals(expected, result);
	}

}
