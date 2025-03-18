package ma.sool.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

  @Value("${api.base-url}")
  private String baseUrl;

  private final CorsConfig corsConfig;

    public SecurityConfiguration(CorsConfig config, CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.GET, baseUrl+"/arts/**").permitAll()
            .requestMatchers(HttpMethod.GET, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.POST, baseUrl+"/users").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.PUT, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(HttpMethod.DELETE, baseUrl+"/users/**").hasAuthority("ROLE_admin")
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
            .anyRequest().authenticated() // 항상 마지막에 넣어야 함
      )
        .headers(headers -> headers.frameOptions().disable())
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
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