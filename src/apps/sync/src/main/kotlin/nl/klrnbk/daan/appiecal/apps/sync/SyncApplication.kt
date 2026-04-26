package nl.klrnbk.daan.appiecal.apps.sync

import nl.klrnbk.daan.appiecal.packages.spring.AppiecalSpringApplicationAnnotation
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@AppiecalSpringApplicationAnnotation
@EnableScheduling
class SyncApplication

fun main(args: Array<String>) {
    runApplication<SyncApplication>(*args)
}
