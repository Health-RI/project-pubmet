/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud;

import nl.healthri.pubmet.core.api.MetadataProvider;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class CloudApplication {
    private static final Logger logger = LoggerFactory.getLogger(CloudApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Bean
    public MetadataProvider customProvider() {
        return id -> {
            logger.info("From within custom provider, retrieving metadata with id {}", id);

            var model = new LinkedHashModel();
            var subject = Values.iri("http://example.com");
            var object = Values.literal("hello world");

            model.add(subject, RDFS.LABEL, object);

            return Optional.of(model);
        };
    }
}
