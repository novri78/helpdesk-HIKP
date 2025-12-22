package com.example.helpdesk.backend.security;

import com.example.helpdesk.backend.service.CustomUserDetailsService;
import com.example.helpdesk.backend.util.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

//    private final CustomUserDetailsService userDetailsService;
//    private final JwtTokenUtil jwtTokenUtil; // Tambahkan ini
//    private final UserRepository userRepository; // Untuk mengambil data User saat sukses
//    public JwtRequestFilter jwtRequestFilter;
//
//    @Autowired
//    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
//        this.userDetailsService = userDetailsService;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
//                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
//                // 1. Tambahkan Stateless Session (Penting untuk REST API)
//                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // API address : "/api/auth/register", "/api/auth/login",
//                //                        "/api/users", "/api/users/{id}", "/api/users/{id}/approve",
//                //                        "/api/tickets", "/api/tickets/{id}", "/api/chat",
//                //                        "/api/category", "/api/category/{id}","/api/users/{token}"
//                .antMatchers(
//                        "/api/auth/register", "/api/auth/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(jwtRequestFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
//                .formLogin()
//                    .usernameParameter("email") // Memberitahu Spring untuk mencari key "email" sebagai identitas
//                    .passwordParameter("password")
////                    .loginPage("/")
//                .loginProcessingUrl("/api/auth/login")
//                .failureHandler((request, response, exception) -> {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.setContentType("application/json;charset=UTF-8");
//
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("status", 401);
//                    // Ubah di sini agar sesuai dengan harapan Test Case
//                    data.put("message", "Username atau Password salah");
//                    data.put("error", "Bad Credentials");
//
//                    response.getWriter().write(new ObjectMapper().writeValueAsString(data));
//                })
//                // Contoh logic sederhana di SecurityConfig untuk handle JSON Login
//                .successHandler((request, response, authentication) -> {
//                    // 3. GENERATE TOKEN ASLI DI SINI
//                    String email = authentication.getName();
//                    com.example.helpdesk.backend.model.User user = userRepository.findByEmail(email)
//                            .orElseThrow(() -> new RuntimeException("User not found"));
//
//                    String token = jwtTokenUtil.generateToken(user); // Panggil util Anda!
//
//                    // 4. Kirim respon JSON yang benar
//                    response.setStatus(200);
//                    response.setContentType("application/json");
//
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("status", 200);
//                    data.put("token", token);
//                    data.put("message", "Success Login With Valid Email & Password");
//
//                    response.getWriter().write(new ObjectMapper().writeValueAsString(data));
//                })
//                //.defaultSuccessUrl("/dashboard")
//                //.failureUrl("/login?error=true")
//                .and()
//                .logout().logoutSuccessUrl("/login?logout=true");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder ();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//        // Izinkan origin spesifik yang menyebabkan error di log Anda
//        config.setAllowedOrigins(List.of("http://localhost:8081"));
//        config.setAllowedHeaders(Collections.singletonList("*"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
//
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter; // Filter yang kita buat sebelumnya

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // WAJIB: Expose AuthenticationManager sebagai Bean agar bisa digunakan di Service/Controller jika perlu
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                // PENTING: Ubah ke STATELESS karena kita pakai JWT (tidak pakai Session)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized (Token is missing or invalid)"))
                .and()
                .authorizeRequests()
                // Endpoint publik
                .antMatchers("/api/auth/**").permitAll()
                // Jika controller login Anda di @PostMapping("/login") tanpa prefix /api/auth:
                .antMatchers("/login").permitAll()
                // --- TAMBAHKAN BARIS INI UNTUK SWAGGER ---
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                // Endpoint sisanya wajib TOKEN
                .anyRequest().authenticated();

        // PASANG FILTER JWT: Ini yang akan membaca token di setiap request selain login/register
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://localhost:3000")); // React/Vue port
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
