package dev.ijskoud.appiecal.store.auth

import com.google.gson.Gson
import dev.ijskoud.appiecal.authentication.Token
import dev.ijskoud.appiecal.store.Store
import java.nio.file.Paths

/**
 * The authentication token store
 */
class AuthStore : Store<Token>(
    data = null,
    path = Paths.get(System.getProperty("user.dir"), "data", "auth.json").toString()
) {
    companion object {
        private var instance: AuthStore? = null

        fun getInstance(): AuthStore {
            if (instance == null) {
                instance = AuthStore()
            }

            return instance!!
        }
    }

    override fun fromJson(data: String): Token {
        return Gson().fromJson(data, Token::class.java)
    }
}
