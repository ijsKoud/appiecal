package nl.klrnbk.daan.appiecal.apps.sync.api.datasource.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "automatic_sync")
data class AutomaticSyncModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "active")
    val active: Boolean,
)
