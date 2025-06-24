package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.clients.idp.IdpClient
import nl.klrnbk.daan.appiecal.apps.schedule.exceptions.MissingAccessTokenException
import org.springframework.stereotype.Service

@Service
class IdpService(
    private val idpClient: IdpClient,
) {
    fun getAccessToken(
        token: String,
        userId: String,
    ): String {
        val token = idpClient.getAccessToken(token, userId)
        if (token.isEmpty()) throw MissingAccessTokenException("Expected token to be returned but got empty string instead")

        return token
    }
}
