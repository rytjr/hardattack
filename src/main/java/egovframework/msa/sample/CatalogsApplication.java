package egovframework.msa.sample;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootApplication
@ComponentScan("egovframework.*")
@EnableEurekaClient
public class CatalogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogsApplication.class, args);
	}

	/*@Bean
	public CircuitBreakerRegistry circuitBreakerRegistry() {
		return CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
				.minimumNumberOfCalls(10) // Circuit Breaker가 실패율과 느린 응답 비율을 계산할 최소 요청 수(실패비율을 계산할 때 필요한 최소 호출수)를 지정
				.slidingWindowType(SlidingWindowType.COUNT_BASED) // Circuit Breaker가 닫힌 상태에서 호출결과를 기록하기 위한 슬라이딩 윈도우 타입 설정
				.slidingWindowSize(10) // Circuit Breaker가 닫힌 상태에서 호출결과를 기록하기 위한 슬라이딩 윈도우 크기 설정
				.failureRateThreshold(50) // 실패확률이 failureRateThreshold 에 도달하면 서킷 브레이커를 open 상태로 만들고 호출을 차단함
				.waitDurationInOpenState(Duration.ofMillis(10000)) // Circuit Breaker 유지시간
				.build()); // 10회 요청 후 최근 10건 중 실패율이 50% 이상이면 Circuit Breaker를 오픈하고 10초간 유지
	}
	
	@Bean
	public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
		return circuitBreakerRegistry.circuitBreaker("getCustomerDetail");
	}*/

}
