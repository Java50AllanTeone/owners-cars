package telran.cars.service.model;

import jakarta.persistence.*;
import lombok.*;
import telran.cars.dto.CarDto;
import telran.cars.dto.CarState;

@Entity
@Getter
@Table(name="cars")
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class Car {
	@Id
	String number;
	@ManyToOne
	@JoinColumns({@JoinColumn(name="model_name", nullable = false),
		@JoinColumn(name="model_year", nullable = false)})
	Model model;
	@ManyToOne
	@JoinColumn(name="owner_id", nullable=true)
	@Setter
	CarOwner carOwner;
	String color;
	@Setter
	Integer kilometers;
	@Enumerated(EnumType.STRING) // value in the table will be a string (by default a number)
	CarState state;
	
	public static Car of(CarDto carDto) {
		return new Car(carDto.number(), null, null, carDto.color(), carDto.kilometers(), carDto.state());
	}
	public CarDto build() {
		return new CarDto(number, model.modelYear.getName(), model.modelYear.getYear(), color, kilometers, state);
	}
}
