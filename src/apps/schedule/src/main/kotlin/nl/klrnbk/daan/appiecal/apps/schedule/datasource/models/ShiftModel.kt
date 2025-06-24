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
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String?,
    @Column(name = "store_id") val storeId: String,
    @Column(name = "start_date") val startDate: LocalDateTime,
    @Column(name = "end_date") val endDate: LocalDateTime,
    @Enumerated(EnumType.STRING) val departments: List<ShiftDepartment>,
    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL]) val activities: List<ActivityModel>,
)
