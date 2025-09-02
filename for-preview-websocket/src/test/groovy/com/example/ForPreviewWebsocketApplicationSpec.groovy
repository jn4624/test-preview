package com.example

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ForPreviewWebsocketApplicationSpec extends Specification {

	void "contextLoads"() {
        expect:
        true
	}
}
