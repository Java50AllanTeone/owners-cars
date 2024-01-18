package telran.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static telran.cars.api.ValidationConstants.*;
import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.CarsService;

@WebMvcTest
class CarsControllerTest {
	private static final long PERSON_ID = 123000L;
	private static final String CAR_NUMBER = "123-01-002";
	private static final String CAR_WRONG_NUMBER = "123-010-002";
	private static final String PERSON_NOT_FOUND_MESSAGE = "Person not found";
	private static final String CAR_ALREADY_EXISTS_MESSAGE = "Car already exists";
	private static final String PERSON_ALREADY_EXISTS_MESSAGE = "Person already exists";
	private static final String CAR_NOT_FOUND_MESSAGE = "Car not found";
	private static final String WRONG_EMAIL_ADDRESS = "kuku";
	private static final String EMAIL_ADDRESS = "email@email.com";
	private static final Long WRONG_ID = 123l;

	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto(CAR_NUMBER, "model");
	CarDto carDto1 = new CarDto("123-01-001", "model");
	PersonDto personDto = new PersonDto(PERSON_ID, "name", "2000-01-01", EMAIL_ADDRESS);
	PersonDto personDtoUpdated = new PersonDto(PERSON_ID, "name", "2000-01-01", "email@gmail.com");
	PersonDto personWrongEmail = new PersonDto(PERSON_ID, "name", "2000-01-01", WRONG_EMAIL_ADDRESS);
	PersonDto personNoEmail = new PersonDto(PERSON_ID, "name", "2000-01-01", null);
	PersonDto personNoId = new PersonDto(null, "Vasya", "2000-10-10", EMAIL_ADDRESS);
	PersonDto personWrongId = new PersonDto(123l, "Vasya", "2000-10-10", EMAIL_ADDRESS);
	PersonDto personNoName = new PersonDto(PERSON_ID, null, "2000-10-10", EMAIL_ADDRESS);
	PersonDto personNoBirthDate = new PersonDto(PERSON_ID, "Vasya", null, EMAIL_ADDRESS);
	PersonDto personWrongBirthDate = new PersonDto(PERSON_ID, "Vasya", "200-10-10", EMAIL_ADDRESS);
	
	CarDto carNoNumber = new CarDto(null, "model");
	CarDto carWrongNumber = new CarDto(CAR_WRONG_NUMBER, "model");
	CarDto carNoModel = new CarDto(CAR_NUMBER, null);
	
	TradeDealDto tradeDealNoCarNumber = new TradeDealDto(null, PERSON_ID);
	TradeDealDto tradeDealWrongCarNumber = new TradeDealDto(CAR_WRONG_NUMBER, PERSON_ID);
	TradeDealDto tradeDealWrongPersonIdNumber = new TradeDealDto(CAR_NUMBER, WRONG_ID);

	TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER, PERSON_ID);
	@Autowired
	ObjectMapper mapper;
	

	@Test
	void testAddCar() throws Exception {
		when(carsService.addCar(carDto)).thenReturn(carDto);
		String jsonCarDto = mapper.writeValueAsString(carDto);
		String actualJson = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isOk()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(jsonCarDto, actualJson);
		
	}

	@Test
	void testAddPerson() throws Exception {
		when(carsService.addPerson(personDto)).thenReturn(personDto);
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonPersonDto, actualJson);
	}

	@Test
	void testUpdatePerson() throws Exception {
		when(carsService.updatePerson(personDtoUpdated)).thenReturn(personDtoUpdated);
		String jsonPersonDtoUpdated = mapper.writeValueAsString(personDtoUpdated);
		String actualJson = mockMvc.perform(put("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDtoUpdated)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonPersonDtoUpdated, actualJson);
	}

	@Test
	void testPurchase() throws Exception {
		when(carsService.purchase(tradeDeal)).thenReturn(tradeDeal);
		String jsonTradeDeal = mapper.writeValueAsString(tradeDeal);
		String actualJson = mockMvc.perform(put("http://localhost:8080/cars/trade").contentType(MediaType.APPLICATION_JSON)
				.content(jsonTradeDeal)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonTradeDeal, actualJson);

	}

	@Test
	void testDeletePerson() throws Exception {
		when(carsService.deletePerson(PERSON_ID)).thenReturn(personDto);
		String jsonExpected = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(delete("http://localhost:8080/cars/person/" + PERSON_ID))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJson);
	}

	@Test
	void testDeleteCar() throws Exception {
		when(carsService.deleteCar(CAR_NUMBER)).thenReturn(carDto);
		String jsonExpected = mapper.writeValueAsString(carDto);
		String actualJson = mockMvc.perform(delete("http://localhost:8080/cars/" + CAR_NUMBER))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJson);
	}

	@Test
	void testGetOwnerCars() throws Exception {
		CarDto[] expectedArray = {
				carDto, carDto1
		};
		String jsonExpected = mapper.writeValueAsString(expectedArray);
		when(carsService.getOwnerCars(PERSON_ID)).thenReturn(Arrays.asList(expectedArray));
		String actualJson = mockMvc.perform(get("http://localhost:8080/cars/person/" + PERSON_ID)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJson);
	}

	@Test
	void testGetCarOwner() throws Exception {
		when(carsService.getCarOwner(CAR_NUMBER)).thenReturn(personDto);
		String jsonExpected = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(get("http://localhost:8080/cars/" + CAR_NUMBER)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertEquals(jsonExpected, actualJson);
	}
	
	
	//alternative flow, service exceptions

	@Test
	void testDeletePersonNotFound() throws Exception {
		when(carsService.deletePerson(PERSON_ID)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		
		String actualJson = mockMvc.perform(delete("http://localhost:8080/cars/person/" + PERSON_ID))
			.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testAddCarAlreadyExists() throws Exception {
		when(carsService.addCar(carDto)).thenThrow(new IllegalStateException(CAR_ALREADY_EXISTS_MESSAGE));
		String jsonCarDto = mapper.writeValueAsString(carDto);
		String actualJson = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonCarDto)).andExpect(status().isAlreadyReported()).andReturn().getResponse()
		.getContentAsString();
		assertEquals(CAR_ALREADY_EXISTS_MESSAGE, actualJson);
	}
	
	@Test
	void testAddPersonAlreadyExists() throws Exception {
		when(carsService.addPerson(personDto)).thenThrow(new IllegalStateException(PERSON_ALREADY_EXISTS_MESSAGE));
		String jsonPersonDto = mapper.writeValueAsString(personDto);
		String actualJson = mockMvc.perform(post("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isAlreadyReported()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_ALREADY_EXISTS_MESSAGE, actualJson);
	}
	
	@Test
	void testUpdatePersonNotFound() throws Exception {
		when(carsService.updatePerson(personDtoUpdated)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String jsonPersonDtoUpdated = mapper.writeValueAsString(personDtoUpdated);
		String actualJson = mockMvc.perform(put("http://localhost:8080/cars/person").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDtoUpdated)).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testPurchaseNotFound() throws Exception {
		when(carsService.purchase(tradeDeal)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String jsonTradeDeal = mapper.writeValueAsString(tradeDeal);
		String actualJson = mockMvc.perform(put("http://localhost:8080/cars/trade").contentType(MediaType.APPLICATION_JSON)
				.content(jsonTradeDeal)).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testDeleteCarNotFound() throws Exception {
		when(carsService.deleteCar(CAR_NUMBER)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String actualJson = mockMvc.perform(delete("http://localhost:8080/cars/" + CAR_NUMBER))
			.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testGetOwnerCarsNotFound() throws Exception {
		when(carsService.getOwnerCars(PERSON_ID)).thenThrow(new NotFoundException(PERSON_NOT_FOUND_MESSAGE));
		String actualJson = mockMvc.perform(get("http://localhost:8080/cars/person/" + PERSON_ID)).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(PERSON_NOT_FOUND_MESSAGE, actualJson);
	}
	
	@Test
	void testGetCarOwnerNotFound() throws Exception {
		when(carsService.getCarOwner(CAR_NUMBER)).thenThrow(new NotFoundException(CAR_NOT_FOUND_MESSAGE));
		String actualJson = mockMvc.perform(get("http://localhost:8080/cars/" + CAR_NUMBER)).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(CAR_NOT_FOUND_MESSAGE, actualJson);
	}
	
	
	//alternative flows - validation exceptions handling
	
	//addPerson
	
	void personValidationTests(PersonDto person, String message, MockHttpServletRequestBuilder request) throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(person);
		String response = mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(message, response);
	}
	
	@Test
	void addPersonNoIdTest() throws Exception {
		personValidationTests(personNoId, MISSING_PERSON_ID_MESSAGE, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonWrongIdTest() throws Exception {
		personValidationTests(personWrongId, WRONG_MIN_PERSON_ID_VALUE, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonNoNameTest() throws Exception {
		personValidationTests(personNoName, MISSING_PERSON_NAME_MESSAGE, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonNoBirthDateTest() throws Exception {
		personValidationTests(personNoBirthDate, MISSING_BIRTH_DATE, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonWrongBirthDateTest() throws Exception {
		personValidationTests(personWrongBirthDate, WRONG_DATE_FORMAT, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonWrongEmailTest() throws Exception {		
		personValidationTests(personWrongEmail, WRONG_EMAIL_FORMAT, post("http://localhost:8080/cars/person"));
	}
	
	@Test
	void addPersonNoEmailTest() throws Exception {
		personValidationTests(personNoEmail, MISSING_PERSON_EMAIL, post("http://localhost:8080/cars/person"));
	}
	
	//add car
	
	void addCarValidationTests(CarDto car, String message) throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(car);
		String response = mockMvc.perform(post("http://localhost:8080/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(message, response);
	}
	
	@Test
	void addCarNoNumberTest() throws Exception {
		addCarValidationTests(carNoNumber, MISSING_CAR_NUMBER_MESSAGE);
	}
	
	@Test
	void addCarWrongNumberTest() throws Exception {
		addCarValidationTests(carWrongNumber, WRONG_CAR_NUMBER_MESSAGE);
	}
	
	@Test
	void addCarNoModelTest() throws Exception {
		addCarValidationTests(carNoModel, MISSING_CAR_MODEL_MESSAGE);
	}
	
	
	//update person
	
	@Test
	void updatePersonNoIdTest() throws Exception {
		personValidationTests(personNoId, MISSING_PERSON_ID_MESSAGE, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonWrongIdTest() throws Exception {
		personValidationTests(personWrongId, WRONG_MIN_PERSON_ID_VALUE, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonNoNameTest() throws Exception {
		personValidationTests(personNoName, MISSING_PERSON_NAME_MESSAGE, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonNoBirthDateTest() throws Exception {
		personValidationTests(personNoBirthDate, MISSING_BIRTH_DATE, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonWrongBirthDateTest() throws Exception {
		personValidationTests(personWrongBirthDate, WRONG_DATE_FORMAT, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonWrongEmailTest() throws Exception {		
		personValidationTests(personWrongEmail, WRONG_EMAIL_FORMAT, put("http://localhost:8080/cars/person"));
	}
	
	@Test
	void updatePersonNoEmailTest() throws Exception {
		personValidationTests(personNoEmail, MISSING_PERSON_EMAIL, put("http://localhost:8080/cars/person"));
	}
	
	//purchase

	void tradeDealValidationTests(TradeDealDto tradeDeal, String message, MockHttpServletRequestBuilder request) throws Exception {
		String jsonPersonDto = mapper.writeValueAsString(tradeDeal);
		String response = mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPersonDto)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(message, response);
	}
	

	@Test
	void purchaseNoCarNumberTest() throws Exception {
		tradeDealValidationTests(tradeDealNoCarNumber, MISSING_CAR_NUMBER_MESSAGE, put("http://localhost:8080/cars/trade"));
	}
	
	@Test
	void purchaseWrongCarNumberTest() throws Exception {
		tradeDealValidationTests(tradeDealWrongCarNumber, WRONG_CAR_NUMBER_MESSAGE, put("http://localhost:8080/cars/trade"));
	}
	
	@Test
	void purchaseWrongPersonIdTest() throws Exception {
		tradeDealValidationTests(tradeDealWrongPersonIdNumber, WRONG_MIN_PERSON_ID_VALUE, put("http://localhost:8080/cars/trade"));
	}
	
	//delete person
	
	void getDeleteTests(String message, MockHttpServletRequestBuilder request) throws Exception	{
		String actualJson = mockMvc.perform(request)
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(message, actualJson);
	}
	
	@Test
	void deletePersonWrongIdTest() throws Exception {
		getDeleteTests(WRONG_MIN_PERSON_ID_VALUE, delete("http://localhost:8080/cars/person/" + WRONG_ID));
	}
	
	
	//delete car
	
	@Test
	void deleteCarWrongNumberTest() throws Exception {
		getDeleteTests(WRONG_CAR_NUMBER_MESSAGE, delete("http://localhost:8080/cars/" + CAR_WRONG_NUMBER));
	}
	
	//getOwnerCars
	
	
	@Test
	void getOwnerCarsWrongIdTest() throws Exception {
		getDeleteTests(WRONG_MIN_PERSON_ID_VALUE, get("http://localhost:8080/cars/person/" + WRONG_ID));
	}
	
	//getCarOwner
	
	@Test
	void getCarOwnerWrongNumberTest() throws Exception {
		getDeleteTests(WRONG_CAR_NUMBER_MESSAGE, get("http://localhost:8080/cars/" + CAR_WRONG_NUMBER));
	}
	
	
	
	


}
