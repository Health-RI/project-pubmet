package nl.healthri.pubmet.core.domain;

import java.net.URL;
import java.util.UUID;

public class Index {
    public UUID id;
    public String title;
    public String email;
    public String name;
    public String description;
    public String organization;
    public URL url;
    public IndexType indexType;

    public Index(UUID id, String title, String email, String name, String description, String organization, URL url, IndexType indexType) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.url = url;
        this.indexType = indexType;
    }
}