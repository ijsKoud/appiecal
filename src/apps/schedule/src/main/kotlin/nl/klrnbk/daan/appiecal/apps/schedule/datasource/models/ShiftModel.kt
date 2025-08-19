package nl.klrnbk.daan.appiecal.apps.schedule.datasource.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.ShiftDepartment
import java.time.ZonedDateTime

@Entity
@Table(name = "shift")
class ShiftModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,

    @Column(name = "store_id")
    val storeId: String,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val startDate: ZonedDateTime,

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val endDate: ZonedDateTime,

    @Enumerated(EnumType.STRING)
    val departments: List<ShiftDepartment>,

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: ZonedDateTime,

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: ZonedDateTime,

    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL], orphanRemoval = true)
    var activities: MutableList<ActivityModel>,
) {
    fun isDateTimeEqual(other: ShiftModel): Boolean =
        startDate.isEqual(other.startDate) &&
            endDate.isEqual(other.endDate)

    fun equals(other: ShiftModel): Boolean =
        endDate.toInstant() == other.endDate.toInstant() ||
            startDate.toInstant() == other.startDate.toInstant() ||
            storeId == other.storeId ||
            departments == other.departments

    companion object {
        fun fromApiResponse(
            response: ScheduleShift,
            userId: String,
        ): ShiftModel {
            val shift =
                ShiftModel(
                    id = null,
                    userId = userId,
                    startDate = response.startDate,
                    endDate = response.endDate,
                    storeId = response.storeId,
                    departments = response.departments,
                    createdAt = ZonedDateTime.now(),
                    updatedAt = ZonedDateTime.now(),
                    activities = mutableListOf(),
                )

            val activities = response.activities.map { activity -> ActivityModel.fromApiResponse(activity, shift, userId) }
            shift.activities.addAll(activities)
            return shift
        }
    }
}
