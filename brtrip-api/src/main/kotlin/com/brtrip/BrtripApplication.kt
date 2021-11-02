package com.brtrip

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BrtripApplication

fun main(args: Array<String>) {
	runApplication<BrtripApplication>(*args)
}
