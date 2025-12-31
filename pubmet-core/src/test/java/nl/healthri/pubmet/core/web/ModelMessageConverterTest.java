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
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.http.MockHttpOutputMessage;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class ModelMessageConverterTest {

    private Model createSampleModel() {
        var model = new LinkedHashModel();
        var subject = Values.iri("http://example.com");
        model.add(subject, RDFS.LABEL, Values.literal("hello world"));

        return model;
    }

    @Test
    void GivenModelClass_WhenSupports_ReturnTrue(){
        // Arrange & Act
        var converter = new ModelMessageConverter(RDFFormat.TURTLE);

        // Assert (true)
        assertThat(converter.supports(Model.class)).isTrue();

        // Assert (false)
        assertThat(converter.supports(Map.class)).isFalse();
        assertThat(converter.supports(String.class)).isFalse();
        assertThat(converter.supports(Object.class)).isFalse();
    }

    @Test
    void GivenValidModel_WhenWriteInternal_ReturnModelContentAsBody() throws IOException {
        // Arrange
        var converter = new ModelMessageConverter(RDFFormat.TURTLE);
        var expected = "\r\n<http://example.com> <http://www.w3.org/2000/01/rdf-schema#label> \"hello world\" .\r\n";
        var model = createSampleModel();
        var message = new MockHttpOutputMessage();

        // Act
        converter.writeInternal(model, message);

        // Assert
        var actual = message.getBodyAsString();
        assertEquals(expected, actual);
    }


    @Test
    void GivenConverterWithUnsupportedFormat_WhenWriteInternal_ReturnHttpNotWritableException() {
        // Arrange
        var invalidFormatConverter = new ModelMessageConverter(RDFFormat.HDT);
        var expected = HttpMessageNotWritableException.class;
        var model = new LinkedHashModel();
        var message = new MockHttpOutputMessage();

        // Act & Assert
        assertThrows(expected, () ->
                invalidFormatConverter.writeInternal(model, message)
        );
    }

    @Test
    void GivenNewTurtleMimeTypes_WhenConstructorCalled_AddAndReturnTurtleMimeTypes() {
        // Arrange
        var format = RDFFormat.TURTLE;
        var expectedMimeTypes = format.getMIMETypes();

        // Act
        var converter = new ModelMessageConverter(format);

        // Assert
        var actualMimeTypes = converter.getSupportedMediaTypes();
        assertEquals(expectedMimeTypes.size(), actualMimeTypes.size(), "Should have the same number of media types as the RDF format");
        for (String mimeType : expectedMimeTypes) {
            assertTrue(actualMimeTypes.stream().anyMatch(mediaType -> mediaType.toString().equals(mimeType)) );
        }
    }
}