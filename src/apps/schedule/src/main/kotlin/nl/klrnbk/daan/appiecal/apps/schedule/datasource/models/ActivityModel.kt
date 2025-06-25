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
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleActivity
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import java.time.LocalDateTime

@Entity
@Table(name = "shift_activity")
@Suppress("ktlint:standard:no-blank-line-in-list")
class ActivityModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "paid")
    val paid: Boolean,

    @Column(name = "start_date")
    val startDate: LocalDateTime,

    @Column(name = "end_date")
    val endDate: LocalDateTime,

    @Column(name = "description")
    val description: String,

    @Column(name = "time_code")
    val timeCode: String,

    @Enumerated(EnumType.STRING)
    val department: ShiftDepartment,

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    var shift: ShiftModel,
) {
    fun isDateTimeEqual(other: ActivityModel): Boolean = startDate == other.startDate && endDate == other.endDate

    fun equals(other: ActivityModel): Boolean =
        endDate != other.endDate ||
            startDate != other.startDate ||
            description != other.description ||
            department != other.department ||
            paid != other.paid ||
            timeCode != other.timeCode

    companion object {
        fun fromApiResponse(
            response: ScheduleActivity,
            shift: ShiftModel,
            userId: String,
        ): ActivityModel =
            ActivityModel(
                id = null,
                userId = userId,
                paid = response.paid,
                startDate = response.startDate,
                endDate = response.endDate,
                description = response.description,
                timeCode = response.timeCode,
                department = response.department,
                shift = shift,
            )
    }
}
