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
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleActivity
import java.time.ZonedDateTime

@Entity
@Table(name = "shift_activity")
class ActivityModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "paid")
    val paid: Boolean,

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val startDate: ZonedDateTime,

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val endDate: ZonedDateTime,

    @Column(name = "description")
    val description: String,

    @Column(name = "time_code")
    val timeCode: String,

    @Enumerated(EnumType.STRING)
    val department: ShiftDepartment,

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: ZonedDateTime,

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: ZonedDateTime,

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    var shift: ShiftModel,
) {
    fun isDateTimeEqual(other: ActivityModel): Boolean =
        startDate.toInstant() == other.startDate.toInstant() && endDate.toInstant() == other.endDate.toInstant()

    fun equals(other: ActivityModel): Boolean =
        endDate.toInstant() == other.endDate.toInstant() ||
            startDate.toInstant() == other.startDate.toInstant() ||
            description == other.description ||
            department == other.department ||
            paid == other.paid ||
            timeCode == other.timeCode

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
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                shift = shift,
            )
    }
}
