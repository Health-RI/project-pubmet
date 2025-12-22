/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.model.impl.LinkedHashModel
import org.eclipse.rdf4j.rio.RDFFormat
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.HttpMessageNotWritableException
import spock.lang.Specification

class ModelMessageConverterSpec extends Specification {

    def "write model format"(){
        given:
        var converter = new ModelMessageConverter(RDFFormat.TURTLE)
        var message = Mock(HttpOutputMessage)

        when:
        var model = new LinkedHashModel()

        var outputMessage = message
        converter.writeInternal(model, outputMessage)

        then:
        thrown HttpMessageNotWritableException
    }

    def "verify supports behaviour"() {
        given:
        def converter = new ModelMessageConverter(RDFFormat.TURTLE)

        expect:
        converter.supports(sth) == result

        where:
        sth   || result
        Model || true
        String || false
        Map || false
    }
}
