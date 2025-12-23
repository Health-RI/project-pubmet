/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.MockHttpOutputMessage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelMessageConverterTest {

    private ModelMessageConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ModelMessageConverter(RDFFormat.TURTLE);
    }

    private Model createSampleModel() {
        var model = new LinkedHashModel();
        var subject = Values.iri("http://example.com");
        model.add(subject, RDFS.LABEL, Values.literal("hello world"));

        return model;
    }

    @Test
    void GivenValidModel_WhenWriteInternal_ReturnModelContentAsBody() throws IOException {
        // Arrange
        var expected = "\r\n<http://example.com> <http://www.w3.org/2000/01/rdf-schema#label> \"hello world\" .\r\n";
        var model = createSampleModel();
        var message = new MockHttpOutputMessage();

        // Act
        converter.writeInternal(model, message);

        // Assert
        var actual = message.getBodyAsString();
        assertEquals(expected, actual);
    }
}