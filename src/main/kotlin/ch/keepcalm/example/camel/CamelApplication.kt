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
        producerTemplate.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller")
    }

    @RequestMapping(value = ["/holiday"])
    fun startHolliday() {
        producerTemplate.sendBody("direct:holidayService",
                " <hs:GetHolidaysAvailable xmlns:hs=\"http://www.holidaywebservice.com/HolidayService_v2/\">\n" +
                        " <hs:countryCode>UnitedStates</hs:countryCode>\n" +
                        " </hs:GetHolidaysAvailable>"
        )
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
                .setHeader("operationName", constant("GetCustomer"))
                .to("spring-ws:http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx")
        //                .to("spring-ws:http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx?soapAction=http://foo.com&wsAddressingAction=http://bar.com")

    }
}

