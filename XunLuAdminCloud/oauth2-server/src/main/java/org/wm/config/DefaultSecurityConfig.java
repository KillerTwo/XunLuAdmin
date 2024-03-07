/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wm.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.wm.feignClient.UserServiceClient;
import org.wm.service.Oauth2UserDetailsService;


/**
 * 功能描述：<功能描述>
 * Security 配置
 *
 * @author dove
 * @date 2023/8/2 22:24
 * @since 1.0
 **/
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class DefaultSecurityConfig {

	private final UserServiceClient userServiceClient;

	/**
	 * 功能描述：<功能描述>
	 *       创建AuthenticationManager Bean, 如果需要设置其他配置使用这个，否则使用
	 *       authenticationConfiguration.getAuthenticationManager();
	 * @author dove
	 * @date 2023/8/5 22:48
	 * @param http HttpSecurity
	 * @return org.springframework.security.authentication.AuthenticationManager
	 * @throws Exception
	 */
	// @Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		// authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());   // 如果不配置，则默认的DaoAuthenticationProvider不会被配置
		// authenticationManagerBuilder.authenticationProvider(smsAuthenticationProvider());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

    // @formatter:off
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize ->
				authorize
						.requestMatchers("/login",
								"/oauth2/login",
								"/oauth2/doLogin")
					.permitAll()
					.anyRequest().authenticated()
			).csrf().disable();
				/*.csrf(httpSecurityCsrfConfigurer -> {
					httpSecurityCsrfConfigurer
							.ignoringRequestMatchers("/login",
									"/oauth2/login", "/oauth2/doLogin");
				});*/

		return http.build();
	}

	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		http.securityMatchers((matchers) -> matchers.requestMatchers("/actuator/**", "/css/**", "/error",
						"/webjars/**", "/assets/**", "/js/**", "/img/**", "/favicon.ico"))
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
				.requestCache(RequestCacheConfigurer::disable)
				.securityContext(AbstractHttpConfigurer::disable)
				.sessionManagement(AbstractHttpConfigurer::disable);
		return http.build();
	}

	// @formatter:on

	/*private AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new FederatedIdentityAuthenticationSuccessHandler();
	}*/

    // @formatter:off
	@Bean
	public UserDetailsService users() {
		/*UserDetails user = User
				.withUsername("admin")
				.password("$2a$10$Ye8usAMrn/h1G1sURE8ji.iU5a1fdxZxbX2ZP7g7tZu3NMazGFMOK")
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);*/
		return new Oauth2UserDetailsService(userServiceClient);
	}
	// @formatter:on

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }



	/**
	 * 强散列哈希加密实现
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
