/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web;

import org.eclipse.rdf4j.rio.RDFParserRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

public class ModelMessageConverterContextInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
    private static final Logger logger = LoggerFactory.getLogger(ModelMessageConverterContextInitializer.class);

    @Override
    public void initialize(GenericApplicationContext applicationContext) {
        for (var format : RDFParserRegistry.getInstance().getKeys()) {
            if (format.supportsRDFStar()) {
                continue;
            }

            logger.info("Registering a {} message converter with mime type {}", format.getName(), format.getDefaultMIMEType());

            applicationContext.registerBean("message-converter-".concat(format.getDefaultFileExtension()), ModelMessageConverter.class,
                    () -> new ModelMessageConverter(format));
        }
    }
}
