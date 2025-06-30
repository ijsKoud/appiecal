package nl.klrnbk.daan.appiecal.apps.calendar.datasource.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "calendar_credentials")
data class CalendarCredentialsModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "base_url")
    val baseUrl: String,

    @Column(name = "principal_url")
    val principalUrl: String,

    @Column(name = "calendar_home_url")
    val calendarHomeUrl: String,

    @Column(name = "calendar_url")
    val calendarUrl: String?,
)
