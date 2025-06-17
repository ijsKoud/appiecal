package nl.klrnbk.daan.appiecal.apps.idp.datasource.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class AzureEntraUserIdpLink(
    @Id val id: String,
    @Column(name = "expiration_date") val expirationDate: LocalDateTime,
    @Column("refresh_token", length = 2048) val refreshToken: String,
    @Column("access_token", length = 2048) val accessToken: String,
)
