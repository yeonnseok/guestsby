package com.guestsby

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GuestsbyApplication

fun main(args: Array<String>) {
	runApplication<GuestsbyApplication>(*args)
}
