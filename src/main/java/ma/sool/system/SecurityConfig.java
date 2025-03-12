//package ma.sool.system;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//  @Bean
//  PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  CorsConfigurationSource corsConf() {
//    return request -> {
//      CorsConfiguration config = new CorsConfiguration();
//      config.setAllowedHeaders(Collections.singletonList("*"));
//      config.setAllowedMethods(Collections.singletonList("*"));
//      config.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://127.0.0.1:8080"));
//      config.setMaxAge(3600L);
//      config.setAllowCredentials(true);
//
//      return config;
//    };
//  }
//  @Bean
//  SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//    http
//      .authorizeHttpRequests((auth -> auth
//          .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
//          .requestMatchers(new AntPathRequestMatcher("/board/**")).permitAll()
//          .requestMatchers(new AntPathRequestMatcher("/masool/**")).permitAll()
//          .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
//        )
//      )
//      .cors(corsConfigurer ->
//              corsConfigurer.configurationSource(corsConf()))
//      .csrf(AbstractHttpConfigurer::disable)
///*      .csrf(csrf -> csrf
//           .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
//      .headers(headers ->  headers
//        .addHeaderWriter(new XFrameOptionsHeaderWriter(
//            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) */
//      .formLogin((formLogin) ->  formLogin
//        .loginPage("/board/login")
//        .defaultSuccessUrl("/board/question/question_list"))
//      .logout(logout -> logout
//        .logoutRequestMatcher(new AntPathRequestMatcher("/board/logout"))
//        .logoutSuccessUrl("/")
//        .invalidateHttpSession(true))
//      ;
//    return http.build();
//  }
//
//  @Bean
//  AuthenticationManager authenticationManager(
//          AuthenticationConfiguration authenticationConfiguration)
//          throws Exception{
//    return authenticationConfiguration.getAuthenticationManager();
//  }
//}
