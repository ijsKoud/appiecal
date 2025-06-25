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
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "shift")
class ShiftModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: String?,
    @Column(name = "store_id") val storeId: String,
    @Column(name = "start_date") val startDate: LocalDateTime,
    @Column(name = "end_date") val endDate: LocalDateTime,
    @Enumerated(EnumType.STRING) val departments: List<ShiftDepartment>,
    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL]) var activities: MutableList<ActivityModel>,
) {
    fun isDateEqual(other: ShiftModel): Boolean =
        startDate.toLocalDate() == other.startDate.toLocalDate() && endDate.toLocalDate() == other.endDate.toLocalDate()

    fun equals(other: ShiftModel): Boolean =
        endDate != other.endDate ||
            startDate != other.startDate ||
            storeId != other.storeId ||
            departments != other.departments

    companion object {
        fun fromApiResponse(response: ScheduleShift): ShiftModel {
            val shift =
                ShiftModel(
                    id = null,
                    startDate = response.startDate,
                    endDate = response.endDate,
                    storeId = response.storeId,
                    departments = response.departments,
                    activities = mutableListOf(),
                )

            val activities = response.activities.map { activity -> ActivityModel.fromApiResponse(activity, shift) }
            shift.activities.addAll(activities)
            return shift
        }
    }
}
