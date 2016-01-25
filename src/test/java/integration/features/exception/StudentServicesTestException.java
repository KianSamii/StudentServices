package integration.features.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by kian on 1/23/16.
 */
public class StudentServicesTestException extends RuntimeException {
	private HttpStatus status;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
