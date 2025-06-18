package nl.klrnbk.daan.appiecal.apps.idp.helpers

import nl.klrnbk.daan.appiecal.apps.idp.config.IdpEncryptionConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@Component
@EnableConfigurationProperties(IdpEncryptionConfig::class)
class EncryptionHelper(
    private val config: IdpEncryptionConfig,
) {
    val cipher: Cipher = Cipher.getInstance(config.transformationType)

    fun encryptStr(content: String): String {
        val salt = getRandomBytesArray(config.saltSize)
        val password = config.encryptionKey.toCharArray()

        val pwSpec = PBEKeySpec(password, salt, config.iterations, config.keySize)
        val keyFactory = SecretKeyFactory.getInstance(config.algorithmName)
        val key: ByteArray = keyFactory.generateSecret(pwSpec).encoded

        val contentByteArray = content.toByteArray(StandardCharsets.UTF_8)
        val ciphertextAndNonce: ByteArray = encrypt(contentByteArray, key)

        val ciphertextAndNonceAndSalt = ByteArray(salt.size + ciphertextAndNonce.size)
        System.arraycopy(salt, 0, ciphertextAndNonceAndSalt, 0, salt.size)
        System.arraycopy(ciphertextAndNonce, 0, ciphertextAndNonceAndSalt, salt.size, ciphertextAndNonce.size)

        return Base64.getEncoder().encodeToString(ciphertextAndNonceAndSalt)
    }

    fun decryptStr(encryptedContent: String): String {
        val ciphertextAndNonceAndSalt: ByteArray = Base64.getDecoder().decode(encryptedContent)

        val salt = ByteArray(config.saltSize)
        val ciphertextAndNonce = ByteArray(ciphertextAndNonceAndSalt.size - config.saltSize)
        System.arraycopy(ciphertextAndNonceAndSalt, 0, salt, 0, salt.size)
        System.arraycopy(ciphertextAndNonceAndSalt, salt.size, ciphertextAndNonce, 0, ciphertextAndNonce.size)

        val pwSpec = PBEKeySpec(config.encryptionKey.toCharArray(), salt, config.iterations, config.keySize)
        val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(config.algorithmName)
        val key: ByteArray = keyFactory.generateSecret(pwSpec).encoded

        return String(decrypt(ciphertextAndNonce, key))
    }

    fun encrypt(
        plaintext: ByteArray,
        key: ByteArray,
    ): ByteArray {
        val nonce = getRandomBytesArray(config.nonceSize)
        val secretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(config.tagSize, nonce))

        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val ciphertextAndNonce = ByteArray(nonce.size + ciphertext.size)
        System.arraycopy(nonce, 0, ciphertextAndNonce, 0, nonce.size)
        System.arraycopy(ciphertext, 0, ciphertextAndNonce, nonce.size, ciphertext.size)

        return ciphertextAndNonce
    }

    fun decrypt(
        ciphertextAndNonce: ByteArray,
        key: ByteArray,
    ): ByteArray {
        val nonce = ByteArray(config.nonceSize)
        val ciphertext = ByteArray(ciphertextAndNonce.size - config.nonceSize)
        System.arraycopy(ciphertextAndNonce, 0, nonce, 0, nonce.size)
        System.arraycopy(ciphertextAndNonce, nonce.size, ciphertext, 0, ciphertext.size)

        val secretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(config.tagSize, nonce))
        return cipher.doFinal(ciphertext)
    }

    private fun getRandomBytesArray(size: Int): ByteArray {
        val rand = SecureRandom()
        val byteArray = ByteArray(size)
        rand.nextBytes(byteArray)

        return byteArray
    }
}
