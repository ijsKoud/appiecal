package nl.klrnbk.daan.appiecal.apps.schedule.datasource.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "shift_activity")
class ActivityModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = UUID.randomUUID().toString(),
    @Column(name = "paid") val paid: Boolean,
    @Column(name = "start_date") val startDate: LocalDateTime,
    @Column(name = "end_date") val endDate: LocalDateTime,
    @Column(name = "description") val description: String,
    @Column(name = "time_code") val timeCode: String,
    @Enumerated(EnumType.STRING) val department: ShiftDepartment,
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = true)
    val shift: ShiftModel?,
)
