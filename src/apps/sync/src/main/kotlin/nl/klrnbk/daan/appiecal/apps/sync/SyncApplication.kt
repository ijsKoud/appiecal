package nl.klrnbk.daan.appiecal.apps.sync

import nl.klrnbk.daan.appiecal.packages.spring.AppiecalSpringApplicationAnnotation
import org.springframework.boot.runApplication

@AppiecalSpringApplicationAnnotation class SyncApplication

fun main(args: Array<String>) {
    runApplication<SyncApplication>(*args)
}
