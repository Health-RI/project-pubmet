/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class ModelMessageConverter extends AbstractHttpMessageConverter<Model> {
    private final RDFFormat format;

    public ModelMessageConverter(RDFFormat format) {
        super(parseMimetypes(format));
        this.format = format;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    @Override
    protected Model readInternal(Class<? extends Model> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Model statements, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // TODO confirm we need to close the outputstream
        try (var stream = outputMessage.getBody()) {
            Rio.write(statements, stream, format);
        } catch (RDFHandlerException e) {
            // TODO improve error message and logging
            throw new HttpMessageNotWritableException("Could not write RDF model", e);
        }
    }

    private static MediaType[] parseMimetypes(RDFFormat format) {
        return format.getMIMETypes()
                .stream()
                .map(MediaType::parseMediaType)
                .toArray(MediaType[]::new);
    }
}
