package org.example.gatewayservice.config

import io.jsonwebtoken.Jwts
import org.example.gatewayservice.exception.AuthorizationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Component
class AuthFilter
(
    @Value("\${jwt.secret-key}")
    val jwtSecret: String
): AbstractGatewayFilterFactory<AuthFilter.Config>(Config::class.java) {

    data class Config(val role: String)

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
                ?: throw AuthorizationException("Missing authorization information")
            val parts = authHeader.split(" ")
            if (parts.size != 2 || parts[0] != "Bearer") {
                throw AuthorizationException("Incorrect authorization structure")
            }
            val token = parts[1].trim()
            val jwtRoles = getRoles(token)
            checkNeededRoles(jwtRoles, config.role)
            val userId = getUserId(token)
            val request = exchange.request.mutate().header("x-auth-user-id", userId).build()
            val mutatedExchange = exchange.mutate().request(request).build()
            chain.filter(mutatedExchange)
        }
    }

    private fun getRoles(token: String): List<String> {
        val body = Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
        val claimRoles: List<String> = body["spring_sec_roles"] as List<String>

        return claimRoles.filter { it.startsWith("ROLE_") }
    }

    private fun getSignInKey(): Key {
        val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(jwtSecret))
        val keyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePublic(keySpec)
    }

    private fun checkNeededRoles(jwtRoles: List<String>, neededRole: String) {
        if (!jwtRoles.contains("ROLE_$neededRole")) {
            throw AccessDeniedException("Needed role $neededRole")
        }
    }

    private fun getUserId(token: String): String {
        val body = Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
        val userId: String = body["sub"] as String

        return userId
    }
}
