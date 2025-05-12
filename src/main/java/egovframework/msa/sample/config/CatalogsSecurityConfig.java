package egovframework.msa.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CatalogsSecurityConfig {

	private final CatalogsJwtFilter catalogsJwtFilter;

	public CatalogsSecurityConfig(CatalogsJwtFilter catalogsJwtFilter) {
		this.catalogsJwtFilter = catalogsJwtFilter;
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 사이트 위변조 요청 방지
    	http.csrf(AbstractHttpConfigurer::disable);

		// 세션을 사용하지 않기 때문에 STATELESS로 설정
    	http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    	// 인가(접근권한) 설정
    	http.authorizeRequests(authorize -> authorize
				.antMatchers("/catalogs/**").hasRole("ADMIN")
    			.anyRequest().authenticated());

    	http.addFilterBefore(catalogsJwtFilter, UsernamePasswordAuthenticationFilter.class);

    	return http.build();
	}

}
