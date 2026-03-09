package com.example.studentManagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // CORS тохиргоог идэвхжүүлэх
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // 1. OPTIONS хүсэлтийг бүгдийг нь зөвшөөрөх (МАШ ЧУХАЛ)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 2. Auth API-г зөвшөөрөх
                        .requestMatchers("/api/auth/**").permitAll()
                        // 3. Бусад бүх хүсэлтийг одоогоор нээлттэй үлдээе
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Frontend хаягаа яг зөв байгаа эсэхийг шалгаарай (localhost:3000)
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Бүх header-ийг зөвшөөрөх
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        // Энэ тохиргоог хэр удаан барихыг зааж өгнө (секундээр)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}