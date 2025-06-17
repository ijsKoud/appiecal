package nl.klrnbk.daan.appiecal.packages.security.idp.service

import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.OpenIdClient
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponseKeys
import org.springframework.scheduling.annotation.Scheduled
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.util.Base64

class OpenIdService(
    val client: OpenIdClient,
) {
    var publicKeys: List<RSAPublicKey> = getFreshJwks()

    @Scheduled(cron = "\${appiecal.spring.security.authorization.jwks.refresh-interval}")
    private fun updateJwks() {
        publicKeys = getFreshJwks()
    }

    fun getFreshJwks(): List<RSAPublicKey> {
        val response = client.getFreshJwks()
        return response.keys.map(::getPublicKey)
    }

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
