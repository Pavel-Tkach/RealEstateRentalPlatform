package org.example.gatewayservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange { exchange ->
                exchange.pathMatchers("/eureka/").permitAll()
                    .pathMatchers("/secured/").hasRole("ADMIN")
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { resource ->
                resource.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) }
            }

        return http.build()
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
