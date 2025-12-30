package nl.healthri.pubmet.core.service;

import nl.healthri.pubmet.core.api.MetadataProvider;
import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class MetadataProviderService implements MetadataProvider {
    private static final Logger logger = LoggerFactory.getLogger(nl.healthri.pubmet.core.service.MetadataProviderService.class);
    private final Map<String, Model> inMemoryModels = new HashMap<>();

    public MetadataProviderService() {}

    @Override
    public Optional<Model> getMetadata(String id) {
        return Optional.ofNullable(inMemoryModels.get(id));
    }

    @Override
    public Model uploadMetadata(String body, String contentType) throws IOException {
        logger.info("Uploading metadata");

        var reader = new StringReader(body);
        var format = Rio.getParserFormatForMIMEType(contentType)
                .orElseThrow(() -> new BadRequestException("Unsupported content type: " + contentType));

        var model = Rio.parse(reader, "", format);
        inMemoryModels.put("hello", model);

        logger.info("Successfully uploaded metadata. Total models: {}", inMemoryModels.size());
        return model;
    }
}