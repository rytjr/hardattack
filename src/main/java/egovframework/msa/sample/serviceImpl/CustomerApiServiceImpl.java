package egovframework.msa.sample.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import egovframework.msa.sample.service.CustomerApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CustomerApiServiceImpl implements CustomerApiService {

	private static final Logger log = LoggerFactory.getLogger(CustomerApiServiceImpl.class);
	private final WebClient.Builder webClient;
	
	public CustomerApiServiceImpl(WebClient.Builder webClient) {
		this.webClient = webClient;
	}

	@Override
	//@CircuitBreaker(name="getCustomerDetail", fallbackMethod="getCustomerDetailFallback")
	public String getCustomerDetail(String customerId) {
		//return customerId;
    	return webClient.build().get()
    			.uri("http://localhost:8080/customer/customers/" + customerId)
    			.retrieve()
    			.bodyToMono(String.class)
    			.flux()
    			.toStream()
    			.findFirst()
    			.orElse(null);
	}

	public String getCustomerDetailFallback(String customerId, Throwable ex) {
		log.info("Error:" + ex.getMessage());
		return "고객정보 조회가 지연되고 있습니다.";
	}

}
