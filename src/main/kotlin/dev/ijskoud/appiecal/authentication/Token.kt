package dev.ijskoud.appiecal.authentication

import java.util.*

data class Token(
    var refreshToken: String,
    var accessToken: String,
    var expireDate: Date)
{
    val isExpired: Boolean
        get() {
            return expireDate.before(Date())
        }

    companion object {
        /**
         * Creates a new token
         * @param refreshToken The refresh token
         * @param accessToken The access token
         * @param expireIn The time it takes before the access token is expired
         */
        fun create(refreshToken: String, accessToken: String, expireIn: Long): Token {
            val date = convertExpireIn(expireIn)
            return Token(refreshToken, accessToken, date)
        }

        /**
         * Converts the expireIn duration to a date
         * @param expireIn The expiration time that needs to be converted
         */
        fun convertExpireIn(expireIn: Long): Date {
            val expire = ((expireIn - (60 * 5)) * 1e3).toLong() // 5 mins for margin of error
            val expireDate = Date(System.currentTimeMillis() + expire)

            return expireDate
        }
    }
}
