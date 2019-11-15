package test

import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexTest {

    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    lateinit var importantRepository: ImportantRepository

    @BeforeAll
    fun initialize() {
        RestAssured.requestSpecification = RequestSpecBuilder()
                .setBaseUri(server.url.toString())
                .setContentType(ContentType.JSON)
                .build()
    }

    @Test
    fun `should get data from Client instead of Cache` () {
        every { importantRepository.findByFin("fin") } returns null
        RestAssured.given().request()
                .contentType(ContentType.JSON)
                .`when`().get("/")
                .then()
                .statusCode(HttpStatus.OK.code)
                .body("message", Matchers.`is`("ImportantFromClient - fin"))
    }

    @MockBean(value = ImportantRepository::class)
    fun mockRepo() : ImportantRepository = mockk()
}