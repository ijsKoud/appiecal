package nl.klrnbk.daan.appiecal.apps.idp

import nl.klrnbk.daan.appiecal.packages.spring.AppiecalSpringApplicationAnnotation
import org.springframework.boot.runApplication

@AppiecalSpringApplicationAnnotation class IdpApplication

fun main(args: Array<String>) {
    runApplication<IdpApplication>(*args)
}
