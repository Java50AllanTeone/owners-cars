package telran.cars.exceptions;

import telran.cars.api.ServiceExceptionMessages;

public class ModelIllegalStateException extends IllegalStateException {

	public ModelIllegalStateException() {
		super(ServiceExceptionMessages.MODEL_ALREADY_EXISTS);
	}
}
