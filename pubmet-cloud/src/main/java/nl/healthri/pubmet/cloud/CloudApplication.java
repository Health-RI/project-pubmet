/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud;

import nl.healthri.pubmet.core.api.MetadataProvider;
import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

@SpringBootApplication
public class CloudApplication {
    private static final Logger logger = LoggerFactory.getLogger(CloudApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Bean
    public MetadataProvider customProvider() {
        return new MetadataProvider() {
            @Override
            public Optional<Model> getMetadata(String id) {
                logger.info("Retrieving metadata with id: {}", id);

                var model = new LinkedHashModel();
                var subject = Values.iri("http://example.com/" + id);
                model.add(subject, RDFS.LABEL, Values.literal("hello world"));

                return Optional.of(model);
            }

            @Override
            public Model uploadMetadata(String body, String contentType) throws IOException {
                logger.info("Uploading metadata");

                var reader =  new StringReader(body);
                var format = Rio.getParserFormatForMIMEType(contentType).orElseThrow(BadRequestException::new);

                return Rio.parse(reader, "", format);
            }
        };
    }
}
