package ch.keepcalm.example.camel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.apache.camel.ProducerTemplate
import org.springframework.web.bind.annotation.RestController
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@SpringBootApplication
class CamelApplication

fun main(args: Array<String>) {
	runApplication<CamelApplication>(*args)
}


@RestController
class CamelController(val producerTemplate: ProducerTemplate) {

	@RequestMapping(value = ["/"])
	fun startCamel() {
		producerTemplate!!.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller")
	}
}


@Component
class CamelRoutes : RouteBuilder() {
	@Throws(Exception::class)
	override fun configure() {
		from("direct:firstRoute")
				.log("Camel body: \${body}")
	}
}
