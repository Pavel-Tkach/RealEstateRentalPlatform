package org.example.gatewayservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange { exchange ->
                exchange
                    .pathMatchers("/eureka/**").permitAll()
                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .pathMatchers(HttpMethod.GET, "/properties/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/properties/recommendations").permitAll()
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { resource ->
                resource.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) }
            }
            .cors(Customizer.withDefaults())
            .oauth2Login(Customizer.withDefaults())

        return http.build()
    }

    @Bean
    fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return CorsWebFilter(source)
    }


    private fun jwtAuthenticationConverter(): (Jwt) -> Mono<AbstractAuthenticationToken>? {
        return { jwt ->
            val converter = JwtAuthenticationConverter()
            converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
            converter.convert(jwt)?.let { Mono.just(it) }
        }
    }

    private fun jwtGrantedAuthoritiesConverter(): JwtGrantedAuthoritiesConverter {
        val converter = JwtGrantedAuthoritiesConverter()
        converter.setAuthorityPrefix("ROLE_")
        converter.setAuthoritiesClaimName("spring_sec_roles")

        return converter
    }
}
