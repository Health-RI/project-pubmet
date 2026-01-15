/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.model.impl.LinkedHashModel
import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.model.vocabulary.RDFS
import org.eclipse.rdf4j.rio.RDFFormat
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.mock.http.MockHttpOutputMessage
import spock.lang.Specification

class ModelMessageConverterSpec extends Specification {

    def "write model format"(){
        given:
        var converter = new ModelMessageConverter(RDFFormat.TURTLE)
        var mockOutputMessage = new MockHttpOutputMessage()

        var model = new LinkedHashModel();
        var subject = Values.iri("http://example.com");
        var object = Values.literal("hello world");
        model.add(subject, RDFS.LABEL, object);

        when:
        converter.writeInternal(model, mockOutputMessage)

        then:
        var expected = '\r\n<http://example.com> <http://www.w3.org/2000/01/rdf-schema#label> "hello world" .\r\n'
        var actual = mockOutputMessage.getBodyAsString()
        expected == actual

    }

    def "write model format throw unsupported rdf exception"(){
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
