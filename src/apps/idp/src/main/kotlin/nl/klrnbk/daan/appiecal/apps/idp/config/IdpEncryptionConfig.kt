package nl.klrnbk.daan.appiecal.apps.idp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "appiecal.idp")
data class IdpEncryptionConfig(
    val encryptionKey: String,
    val keySize: Int = 128,
    val tagSize: Int = 128,
    val saltSize: Int = 16,
    val nonceSize: Int = 12,
    val iterations: Int = 32767,
    val algorithmName: String = "PBKDF2WithHmacSHA256",
    val transformationType: String = "AES/GCM/NoPadding",
)
