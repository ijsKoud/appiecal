package nl.klrnbk.daan.appiecal.apps.schedule

import nl.klrnbk.daan.appiecal.packages.spring.AppiecalSpringApplicationAnnotation
import org.springframework.boot.runApplication

@AppiecalSpringApplicationAnnotation class ScheduleApplication

fun main(args: Array<String>) {
    runApplication<ScheduleApplication>(*args)
}
