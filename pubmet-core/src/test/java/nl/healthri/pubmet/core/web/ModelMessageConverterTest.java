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
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static nl.healthri.pubmet.core.TestConstants.TEST_INVALID_TURTLE;
import static nl.healthri.pubmet.core.TestConstants.TEST_TURTLE;
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
        var expectedModel = createSampleModel();
        var message = new MockHttpOutputMessage();

        // Act
        converter.writeInternal(expectedModel, message);

        // Assert
        var messageContent = message.getBodyAsString();
        var actual = Rio.parse(
                new StringReader(messageContent),
                "",
                RDFFormat.TURTLE
        );

        assertNotNull(actual);
        assertEquals(expectedModel, actual);
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
            assertTrue(actualMimeTypes.stream().map(MediaType::toString).anyMatch(mimeType::equals));
        }
    }

    @Test
    void GivenEmptyInputMessage_WhenReadInternal_ReturnEmptyModel() throws IOException {
        // Arrange
        var converter = new ModelMessageConverter(RDFFormat.TURTLE);
        var message = new MockHttpInputMessage("".getBytes());

        // Act
        Model result = converter.readInternal(Model.class, message);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void GivenInputMessage_WhenReadInternal_ReturnModel() throws IOException {
        // Arrange
        var converter = new ModelMessageConverter(RDFFormat.TURTLE);
        var inputMessage = TEST_TURTLE;
        var message = new MockHttpInputMessage(inputMessage.getBytes());

        // Act
        var result = converter.readInternal(Model.class, message);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(
                Values.iri("http://example.com"),
                RDFS.LABEL,
                Values.literal("hello world")
        ));
    }

    @Test
    void GivenInvalidInputMessageFormat_WhenReadInternal_ThrowsRdfParseException() {
        // Arrange
        var converter = new ModelMessageConverter(RDFFormat.TURTLE);
        var invalidInputMessage = TEST_INVALID_TURTLE;
        var message = new MockHttpInputMessage(invalidInputMessage.getBytes());

        // Act & Assert
        assertThrows(RDFParseException.class, () ->
                converter.readInternal(Model.class, message)
        );
    }
}