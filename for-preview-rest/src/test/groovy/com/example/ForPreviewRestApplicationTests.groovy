package com.example

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ForPreviewRestApplicationTests extends Specification {

    def "contextLoads"() {
        expect:
        true
    }
}
