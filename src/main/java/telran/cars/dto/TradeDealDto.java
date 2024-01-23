package telran.cars.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import telran.cars.service.model.Car;
import telran.cars.service.model.CarOwner;

import static telran.cars.api.ValidationConstants.*;



public record TradeDealDto(
		@NotNull(message=MISSING_CAR_NUMBER_MESSAGE)
		@Pattern(regexp=CAR_NUMBER_REGEXP, message=WRONG_CAR_NUMBER_MESSAGE) String carNumber, 
		@Min(value=MIN_PERSON_ID_VALUE, message=WRONG_MIN_PERSON_ID_VALUE) 
		@Max(value=MAX_PERSON_ID_VALUE, message=WRONG_MAX_PERSON_ID_VALUE )Long personId,  
		@Pattern(regexp=BIRTH_DATE_REGEXP, message=WRONG_DATE_FORMAT) String date) {

}

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//long id;
//@ManyToOne
//@JoinColumn(name="car_number", nullable = false)
//Car car;
//@ManyToOne
//@JoinColumn(name="owner_id")
//CarOwner carOwner;
//@Temporal(TemporalType.DATE)
//LocalDate date;
