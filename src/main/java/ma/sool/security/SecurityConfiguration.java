package ma.sool.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfiguration {

    // 1. jwt 생성을 위한 설정
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;


  @Value("${api.base-url}")
  private String baseUrl;

  private final CorsConfig corsConfig;
  private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
  private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
  private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;
  private final CustomInsufficientAuthenticationEntryPoint customInsufficientAuthenticationEntryPoint;

    public SecurityConfiguration(CorsConfig corsConfig,
                                 CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint,
                                 CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint,
                                 CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler,
                                 CustomInsufficientAuthenticationEntryPoint customInsufficientAuthenticationEntryPoint)
            throws NoSuchAlgorithmException {
        this.corsConfig = corsConfig;
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
        this.customInsufficientAuthenticationEntryPoint = customInsufficientAuthenticationEntryPoint;
        // public/private key pair 생성
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }


    @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // url에 대한 필터링 정책
        .authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.GET, baseUrl+"/arts/**").permitAll()
            .requestMatchers(HttpMethod.GET, baseUrl+"/users/login/**").permitAll()
            .requestMatchers(HttpMethod.GET, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.POST, baseUrl+"/users").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.PUT, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.DELETE, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll() // h2-console 정책
            .anyRequest().authenticated() // 항상 마지막에 넣어야 함
      )
        .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) // h2-console x-frame 관련
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
//        .httpBasic(Customizer.withDefaults())
        // 인증과정에서의 예외를 처리하기 위해 ...(entryPoint를 RestControllerAdvice에서 처리)
        .httpBasic(httpBasic -> httpBasic
//                .authenticationEntryPoint(customInsufficientAuthenticationEntryPoint)
                .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
        )
            // jwt token 관련한 oauth2 설정
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                .jwt(Customizer.withDefaults())
                .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
                .accessDeniedHandler(customBearerTokenAccessDeniedHandler)
        )
        .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    ;
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  // JWT 관련 설정
  @Bean
  public JwtEncoder jwtEncoder(){
      JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
      JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
      return new NimbusJwtEncoder(jwkSet);
  }
  @Bean
    public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
      JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

      jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
      jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
      return jwtAuthenticationConverter;
  }
}
/**  CorsConfigurationSource corsConf() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://127.0.0.1:8080"));
      config.setMaxAge(3600L);
      config.setAllowCredentials(true);

      return config;
    };
  }
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http
      .authorizeHttpRequests((auth -> auth
          .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
          .requestMatchers(new AntPathRequestMatcher("/board/**")).permitAll()
          .requestMatchers(new AntPathRequestMatcher("/masool/**")).permitAll()
          .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
        )
      )
      .cors(corsConfigurer ->
              corsConfigurer.configurationSource(corsConf()))
      .csrf(AbstractHttpConfigurer::disable)
      .csrf(csrf -> csrf
           .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
//      .headers(headers ->  headers
//        .addHeaderWriter(new XFrameOptionsHeaderWriter(
//            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
      .formLogin((formLogin) ->  formLogin
        .loginPage("/board/login")
        .defaultSuccessUrl("/board/question/question_list"))
      .logout(logout -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/board/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true))
      ;
    return http.build();
  }
  */