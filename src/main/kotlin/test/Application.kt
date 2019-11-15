package test

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("test")
                .mainClass(Application.javaClass)
                .start()
    }
}