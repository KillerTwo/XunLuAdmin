/*
 * Copyright 2020-2022 the original author or authors.
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


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.wm.authentication.filter.OAuth2UsernameLoginFilter;
import org.wm.authentication.handler.AuthenticationEntryPointImpl;
import org.wm.authentication.handler.OAuth2LoginSuccessHandler;
import org.wm.authentication.password.OAuth2AuthorizationPasswordRequestAuthenticationProvider;
import org.wm.authentication.password.OAuth2PasswordAuthenticationConverter;
import org.wm.authorization.RedisOAuth2AuthorizationService;
import org.wm.config.configurer.ValidateCodeConfigurer;
import org.wm.domain.dto.SecurityContextUser;
import org.wm.feignClient.UserServiceClient;
import org.wm.feignClient.ValidateCodeClient;
import org.wm.jackson2.GrantedAuthorityDeserializer;
import org.wm.jackson2.GrantedAuthoritySerializer;
import org.wm.jackson2.SecurityContextUserMixin;
import org.wm.jose.Jwks;
import org.wm.utils.OAuth2ConfigurerUtils;
import org.wm.utils.UserInfoMapperUtils;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

/**
 * 功能描述：<功能描述>
 *     Spring Security Oauth2 Server配置
 * @author dove
 * @date 2023/7/31 15:33
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

	private final AuthenticationManager authManager;

	private final JdbcTemplate jdbcTemplate;

	private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

	private final PasswordEncoder passwordEncoder;

	private final RedisTemplate<Object, Object> redisTemplate;

	private final UserServiceClient userServiceClient;

	private final ValidateCodeClient validateCodeClient;

	private final ValidateCodeConfigurer validateCodeConfigurer;



	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer();

		/*OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		var authorizationServerConfigurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);*/

		Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> {
			OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
			JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
			var claims = principal.getToken().getClaims();
			var username = claims.get("sub");
			var res = userServiceClient.userInfoByUsername(String.valueOf(username));

			// return new OidcUserInfo(principal.getToken().getClaims());
			return UserInfoMapperUtils.loginUserToOidcUserInfo(res.getData());
		};

		// 启用OpenID Connect 1.0， 默认是禁止的
		// authorizationServerConfigurer.oidc(Customizer.withDefaults());
		// 自定义/userinfo端点的返回值
		authorizationServerConfigurer
				.oidc((oidc) -> oidc
						.userInfoEndpoint((userInfo) -> userInfo
								.userInfoMapper(userInfoMapper)
						)
				);


		/*http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());*/

		/*OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer<>();*/
		authorizationServerConfigurer
				.authorizationEndpoint(authorizationEndpoint ->
						authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

//		authorizationServerConfigurer.tokenGenerator(context -> {
//			var tokenType = context.getTokenType();
//
//		});
		// 配置password授权模式
		// OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(http);
		authorizationServerConfigurer.tokenEndpoint(endpoint -> {

			AuthenticationConverter authenticationConverter = new DelegatingAuthenticationConverter(
					Arrays.asList(
							new OAuth2AuthorizationCodeAuthenticationConverter(),
							new OAuth2RefreshTokenAuthenticationConverter(),
							new OAuth2ClientCredentialsAuthenticationConverter(),
							new OAuth2PasswordAuthenticationConverter()));

			endpoint.accessTokenRequestConverter(authenticationConverter);
			// 该方法provider并不会被注册
			/*endpoint.authenticationProvider(
					new OAuth2AuthorizationPasswordRequestAuthenticationProvider(authenticationManager,
							authorizationService(), tokenGenerator));*/
		});


		// 设置自定义token生成器
		// authorizationServerConfigurer.tokenGenerator(tokenGenerator());

		RequestMatcher endpointsMatcher = authorizationServerConfigurer
				.getEndpointsMatcher();

		/*http.requestCache(httpSecurityRequestCacheConfigurer -> {
			httpSecurityRequestCacheConfigurer.requestCache(redisRequestCache());
		});*/

		/*http.securityContext(s -> {
			s.securityContextRepository()
		});*/

		/*http.sessionManagement(s -> {

		});*/

		HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
		http.securityContext((securityContext) -> securityContext
						.securityContextRepository(httpSessionSecurityContextRepository)
				// .requireExplicitSave(false)
		);

		http.authenticationProvider(new OAuth2AuthorizationPasswordRequestAuthenticationProvider(authManager,
				auth2AuthorizationService(), tokenGenerator()));

		http.oauth2ResourceServer(oauth2Configurer ->
				oauth2Configurer
						.jwt(jwtConfigurer -> jwtConfigurer.decoder(OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource())))
						.authenticationEntryPoint(authenticationEntryPointImpl));

		http.securityMatcher(endpointsMatcher)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests.anyRequest().authenticated()
				)
				// .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.csrf().disable()
				.apply(validateCodeConfigurer).and()
				.apply(authorizationServerConfigurer);

		// 设置自定义token生成器
		authorizationServerConfigurer.tokenGenerator(tokenGenerator());
		http.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/oauth2/login"))
				.addFilterBefore(oAuth2UsernameLoginFilter(), BasicAuthenticationFilter.class);
		// http.csrf().disable();
		// http.formLogin(Customizer.withDefaults()); //.addFilterBefore(usernamePasswordAuthenticationFilter(), JwtAuthenticationTokenFilter.class);

		// http.addFilterBefore(usernamePasswordAuthenticationFilter(), JwtAuthenticationTokenFilter.class);
		return http.build();
	}

	// 授权模式登录处理filter
	@Bean
	public OAuth2UsernameLoginFilter oAuth2UsernameLoginFilter() {
		HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
		var oAuth2UsernameLoginFilter = new OAuth2UsernameLoginFilter();
		oAuth2UsernameLoginFilter.setFilterProcessesUrl("/oauth2/doLogin");
		oAuth2UsernameLoginFilter.setAuthenticationManager(authManager);
		oAuth2UsernameLoginFilter.setAuthenticationSuccessHandler(oAuth2LoginSuccessHandler());
		oAuth2UsernameLoginFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler());
		oAuth2UsernameLoginFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
		oAuth2UsernameLoginFilter.setSecurityContextRepository(httpSessionSecurityContextRepository);
		return oAuth2UsernameLoginFilter;
	}

	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new SessionFixationProtectionStrategy();
	}

	@Bean
	public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
		var successHandler = new OAuth2LoginSuccessHandler();
		// successHandler.setRequestCache(redisRequestCache());
		return successHandler;
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
		var failureHandler = new SimpleUrlAuthenticationFailureHandler();
		// failureHandler.setDefaultFailureUrl("/oauth2/login?error=");
		failureHandler.setDefaultFailureUrl("/oauth2/login");
		return failureHandler;
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {

		var secret = passwordEncoder.encode("secret");

		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("messaging-client-opaque")
				// .clientSecret("secret")
				// .clientSecret("$2a$10$Xt5ahdi2KFfptijeHvkyqe9LghZRuanSSQxfsJ.fzoCWM3gB72/kq")
				.clientSecret(secret)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				// .redirectUri("http://127.0.0.1:8080")
				// .redirectUri("http://127.0.0.1:8080/authorized")
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
				.redirectUri("http://127.0.0.1:8080/authorized")
				.scope(OidcScopes.OPENID)
				.scope("message.read")
				.scope("message.write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				// OAuth2TokenFormat.REFERENCE 表示生成 opaque token  OAuth2TokenFormat.SELF_CONTAINED表示生成 jwt token, 设置token过期时间为30分钟
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
				.build();

		// Save registered client in db as if in-memory
		// var repository = new InMemoryRegisteredClientRepository(registeredClient);
		var repository = new JdbcRegisteredClientRepository(jdbcTemplate);
		// repository.save(registeredClient);
		return repository;
	}

	// @formatter:on


	@Bean
	public OAuth2AuthorizationService auth2AuthorizationService() {
		return new RedisOAuth2AuthorizationService(redisTemplate, registeredClientRepository());
	}


	// @Bean
	public OAuth2AuthorizationService authorizationService() {
		// var service = new InMemoryOAuth2AuthorizationService();
		var service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository());

		var authorizationParametersMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationParametersMapper();

		// 自定义ObjectMapper解决
		// The class with java.util.ImmutableCollections$ListN and name
		// of java.util.ImmutableCollections$ListN is not in the allowlist问题
		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

		objectMapper.registerModule(new CoreJackson2Module());
		objectMapper.addMixIn(SecurityContextUser.class, SecurityContextUserMixin.class);
		objectMapper.enableDefaultTyping(NON_FINAL, JsonTypeInfo.As.PROPERTY);

		/*SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(new GrantedAuthoritySerializer());
		simpleModule.addDeserializer(Collection.class, new GrantedAuthorityDeserializer());

		objectMapper.registerModule(simpleModule);*/
		authorizationParametersMapper.setObjectMapper(objectMapper);

		service.setAuthorizationParametersMapper(authorizationParametersMapper);



		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository());
		// authorizationRowMapper.setLobHandler(lobHandler);
		authorizationRowMapper.setObjectMapper(objectMapper);

		service.setAuthorizationRowMapper(authorizationRowMapper);
		return service;
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService() {
		var consentService = new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository());
		// return new InMemoryOAuth2AuthorizationConsentService();
		return consentService;
	}

	/**
	 * 功能描述：<功能描述>
	 *       自定义Token生成器 OAuth2TokenGenerator
	 * @author dove
	 * @date 2023/8/5 21:43
	 * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator<?>
	 */
	// token生成
	@Bean
	public OAuth2TokenGenerator<?> tokenGenerator() {
		/*JwtEncoder jwtEncoder = null;
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);*/
		var jwtEncoder = new NimbusJwtEncoder(jwkSource());
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);   // jwt token
		jwtGenerator.setJwtCustomizer(jwtCustomizer());

		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();  // opaque token
		accessTokenGenerator.setAccessTokenCustomizer(accessTokenCustomizer());
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

		return new DelegatingOAuth2TokenGenerator(
				jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	// 自定义OAuth2Token属性
	@Bean
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer() {
		return context -> {
			OAuth2TokenClaimsSet.Builder claims = context.getClaims();
			var user = context.getPrincipal();
			// Customize claims
			// claims.claim();

			if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
				// Customize headers/claims for access_token

			} else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
				// Customize headers/claims for id_token

			}

			claims.claim("authorities", new ArrayList<>(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
			// claims.claim("principal", user.getPrincipal());



		};
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> {
			JwtClaimsSet.Builder claims = context.getClaims();


			var user = context.getPrincipal();
			if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
				// Customize headers/claims for access_token

			} else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
				// Customize headers/claims for id_token

			}

			claims.claim("authorities", new ArrayList<>(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
			// claims.claim("principal", user.getPrincipal());
		};
	}


	// 用于签名访问令牌
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	// 应用解密签名的访问令牌
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				// .issuer()
				.oidcUserInfoEndpoint("/oauth2/userinfo")
				.build();
	}

	/*@Bean
	public EmbeddedDatabase embeddedDatabase() {
		// @formatter:off
		return new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("UTF-8")
				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
				.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
				.build();
		// @formatter:on
	}*/

}
