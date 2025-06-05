<<<<<<<< HEAD:src/main/kotlin/com/guestsby/GuestsbyApplication.kt
package com.guestsby
========
package kr.co.guestsby.api
>>>>>>>> a26fa387 (feat: project reset to guestsby):api/src/main/kotlin/kr/co/guestsby/api/ApiApplication.kt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
<<<<<<<< HEAD:src/main/kotlin/com/guestsby/GuestsbyApplication.kt
class GuestsbyApplication

fun main(args: Array<String>) {
	runApplication<GuestsbyApplication>(*args)
========
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
>>>>>>>> a26fa387 (feat: project reset to guestsby):api/src/main/kotlin/kr/co/guestsby/api/ApiApplication.kt
}
