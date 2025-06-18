package nl.klrnbk.daan.appiecal.apps.idp.datasource.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "azure_entra_user_idp_link")
class AzureEntraUserIdpLinkModel(
    @Id val id: String,
    @Column(name = "expiration_date") val expirationDate: LocalDateTime,
    @Column("refresh_token", length = 4096) val refreshToken: String,
    @Column("access_token", length = 4096) val accessToken: String,
)
