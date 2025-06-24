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
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "shift")
class ShiftModel(
    /**
     * The unique id of this shift.
     *
     * For example `xxx-xxx-xxx-xxx`.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = UUID.randomUUID().toString(),
    /**
     * The id of the store.
     *
     * For example: `1148`.
     */
    @Column(name = "store_id")
    val storeId: String,
    /**
     * The start date of this shift.
     *
     * For example: `2025-04-28T00:00:00Z`.
     */
    @Column(name = "start_date")
    val startDate: LocalDateTime,
    /**
     * The end date of this shift.
     *
     * For example: `2025-04-28T00:00:00Z`.
     */
    @Column(name = "end_date")
    val endDate: LocalDateTime,
    /**
     * The department(s) that an employee is working in during this shift.
     *
     * For example: `Operatie, OperatieGekoeld`
     */
    @Enumerated(EnumType.STRING)
    val departments: List<ShiftDepartment>,
    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL])
    val activities: List<ActivityModel>,
)
