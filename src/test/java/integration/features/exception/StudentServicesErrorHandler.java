package integration.features.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by kian on 1/23/16.
 */
public class StudentServicesErrorHandler implements ResponseErrorHandler {

	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

	public boolean hasError(ClientHttpResponse response) throws IOException {
		return errorHandler.hasError(response);
	}

	public void handleError(ClientHttpResponse response) throws IOException {
		StudentServicesTestException exception = new StudentServicesTestException(response.getStatusCode());
		throw exception;
	}
}
