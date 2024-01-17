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

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.CarDto;
import telran.cars.dto.PersonDto;
import telran.cars.dto.TradeDealDto;
import telran.cars.exceptions.NotFoundException;
import telran.cars.service.CarsService;

@WebMvcTest
class CarsControllerTest {
	private static final long PERSON_ID = 123L;
	private static final String CAR_NUMBER = "123-01-002";
	private static final String PERSON_NOT_FOUND_MESSAGE = "Person not found";
	private static final String CAR_ALREADY_EXISTS_MESSAGE = "Car already exists";
	private static final String PERSON_ALREADY_EXISTS_MESSAGE = "Person already exists";
	private static final String CAR_NOT_FOUND_MESSAGE = "Car not found";
	@MockBean
	CarsService carsService;
	@Autowired
	MockMvc mockMvc;
	CarDto carDto = new CarDto(CAR_NUMBER, "model");
	CarDto carDto1 = new CarDto("123-01-001", "model");
	PersonDto personDto = new PersonDto(PERSON_ID, "name", "2000-01-01", "email@email.com");
	PersonDto personDtoUpdated = new PersonDto(PERSON_ID, "name", "2000-01-01", "email@gmail.com");
	PersonDto personWrongEmail = new PersonDto(PERSON_ID, "name", "2000-01-01", "kuku");

	TradeDealDto tradeDeal = new TradeDealDto(CAR_NUMBER, 123l);
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
	


}
