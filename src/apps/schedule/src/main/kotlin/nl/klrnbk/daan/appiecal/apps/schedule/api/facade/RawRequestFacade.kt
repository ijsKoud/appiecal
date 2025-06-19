package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.service.GqlService
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RawRequestFacade(
    val gqlService: GqlService,
) {
    fun getRawSchedule(
        token: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<GqlScheduleResponseSchedule> = gqlService.getRawSchedule(token, startDate, endDate)
}
