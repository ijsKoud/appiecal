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
    /**
     * The unique id of this activity.
     *
     * For example `xxx-xxx-xxx-xxx`.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = UUID.randomUUID().toString(),
    /**
     * If this activity is paid for.
     *
     * For example: `true`.
     */
    @Column(name = "paid")
    val paid: Boolean,
    /**
     * The start date of this activity.
     *
     * For example: `2025-04-28T00:00:00Z`.
     */
    @Column(name = "start_date")
    val startDate: LocalDateTime,
    /**
     * The end date of this activity.
     *
     * For example: `2025-04-28T00:00:00Z`.
     */
    @Column(name = "end_date")
    val endDate: LocalDateTime,
    /**
     * The activity description.
     *
     * For example: `Werk`.
     */
    @Column(name = "description")
    val description: String,
    /**
     * The time code of this activity.
     *
     * For example: `WRK`.
     */
    @Column(name = "time_code")
    val timeCode: String,
    /**
     * The department related to this activity.
     *
     * For example: `Operatie`
     */
    @Enumerated(EnumType.STRING)
    val department: ShiftDepartment,
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = true)
    val shift: ShiftModel?,
)
