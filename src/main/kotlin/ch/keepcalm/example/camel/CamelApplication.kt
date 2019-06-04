package ch.keepcalm.example.camel

import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class CamelApplication

fun main(args: Array<String>) {
    runApplication<CamelApplication>(*args)
}


@RestController
class CamelController(val producerTemplate: ProducerTemplate) {

    @RequestMapping(value = ["/"])
    fun startCamel() {
        producerTemplate.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller")
    }

    @RequestMapping(value = ["/holiday"])
    fun startHolliday() {
        val result = producerTemplate.requestBody("direct:holidayService",
                " <hs:GetHolidaysAvailable xmlns:hs=\"http://www.holidaywebservice.com/HolidayService_v2/\">\n" +
                        " <hs:countryCode>UnitedStates</hs:countryCode>\n" +
                        " </hs:GetHolidaysAvailable>"
        )
        println(result)
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

@Component
class HolidayRoutes : RouteBuilder() {
    @Throws(Exception::class)
    override fun configure() {
        from("direct:holidayService")
                .removeHeaders("*")
                .setHeader("operationName", constant("GetHolidaysAvailable"))
                .setHeader("CamelSpringWebserviceSoapAction", constant("http://www.holidaywebservice.com/HolidayService_v2/GetCountriesAvailable"))
//                .to("spring-ws:http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx?soapAction=http://www.holidaywebservice.com/HolidayService_v2/GetCountriesAvailable")
                .to("spring-ws:http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx")
                .log("Camel body: \${body.class} \${body}")
    }
}

