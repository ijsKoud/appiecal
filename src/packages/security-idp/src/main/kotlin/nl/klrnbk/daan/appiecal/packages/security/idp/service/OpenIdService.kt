package nl.klrnbk.daan.appiecal.packages.security.idp.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.OpenIdClient
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponseKeys
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.util.Base64

@Service
class OpenIdService(
    val client: OpenIdClient,
) {
    var jwks: List<OpenIdJwksResponseKeys> = getFreshJwks()

    @Scheduled(cron = "\${appiecal.spring.security.authorization.jwks.refresh-interval}")
    private fun updateJwks() {
        jwks = getFreshJwks()
    }

    fun verifyJwt(token: String): DecodedJWT? {
        val jwt = JWT.decode(token)
        val kid = jwt.keyId ?: throw RuntimeException("No 'kid' in token header")

        val jwk = jwks.find { it.kid == kid } ?: throw RuntimeException("No matching JWK found for kid=$kid")
        val publicKey = getPublicKey(jwk)

        val algorithm = Algorithm.RSA256(publicKey, null)
        val verifier =
            JWT
                .require(algorithm)
                .build()

        return verifier.verify(token)
    }

    fun getFreshJwks(): List<OpenIdJwksResponseKeys> = client.getFreshJwks().keys

    private fun getPublicKey(jwk: OpenIdJwksResponseKeys): RSAPublicKey {
        val nBytes = Base64.getUrlDecoder().decode(jwk.n)
        val eBytes = Base64.getUrlDecoder().decode(jwk.e)
        val spec =
            java.security.spec.RSAPublicKeySpec(
                java.math.BigInteger(1, nBytes),
                java.math.BigInteger(1, eBytes),
            )
        return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
    }
}
