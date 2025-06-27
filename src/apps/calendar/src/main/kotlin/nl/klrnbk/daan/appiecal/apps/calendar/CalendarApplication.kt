package nl.klrnbk.daan.appiecal.apps.calendar

import nl.klrnbk.daan.appiecal.packages.spring.AppiecalSpringApplicationAnnotation
import org.springframework.boot.runApplication

@AppiecalSpringApplicationAnnotation class CalendarApplication

fun main(args: Array<String>) {
    runApplication<CalendarApplication>(*args)
}
