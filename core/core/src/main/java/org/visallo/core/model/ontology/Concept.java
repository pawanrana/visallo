package org.visallo.core.model.ontology;

import org.visallo.core.exception.VisalloException;
import org.visallo.web.clientapi.model.ClientApiOntology;
import org.atteo.evo.inflector.English;
import org.vertexium.Authorizations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public abstract class Concept {
    private final String parentConceptIRI;
    private final Collection<OntologyProperty> properties;

    protected Concept(String parentConceptIRI, Collection<OntologyProperty> properties) {
        this.parentConceptIRI = parentConceptIRI;
        this.properties = properties;
    }

    public abstract String getTitle();

    public abstract boolean hasGlyphIconResource();

    public abstract String getColor();

    public abstract String getDisplayName();

    public abstract String getDisplayType();

    public abstract String getTitleFormula();

    public abstract Boolean getSearchable();

    public abstract String getSubtitleFormula();

    public abstract String getTimeFormula();

    public abstract boolean getUserVisible();

    public abstract Map<String, String> getMetadata();

    public abstract List<String> getAddRelatedConceptWhiteList();

    public Collection<OntologyProperty> getProperties() {
        return properties;
    }

    public String getParentConceptIRI() {
        return this.parentConceptIRI;
    }

    public ClientApiOntology.Concept toClientApi() {
        try {
            ClientApiOntology.Concept concept = new ClientApiOntology.Concept();
            concept.setId(getIRI());
            concept.setTitle(getTitle());
            concept.setDisplayName(getDisplayName());
            if (getDisplayType() != null) {
                concept.setDisplayType(getDisplayType());
            }
            if (getTitleFormula() != null) {
                concept.setTitleFormula(getTitleFormula());
            }
            if (getSearchable() != null) {
                concept.setSearchable(getSearchable());
            }
            if (getSubtitleFormula() != null) {
                concept.setSubtitleFormula(getSubtitleFormula());
            }
            if (getTimeFormula() != null) {
                concept.setTimeFormula(getTimeFormula());
            }
            if (getParentConceptIRI() != null) {
                concept.setParentConcept(getParentConceptIRI());
            }
            if (getDisplayName() != null) {
                concept.setPluralDisplayName(English.plural(getDisplayName()));
            }
            if (!getUserVisible()) {
                concept.setUserVisible(getUserVisible());
            }
            if (hasGlyphIconResource()) {
                concept.setGlyphIconHref("resource?id=" + URLEncoder.encode(getIRI(), "utf8"));
            }
            if (getColor() != null) {
                concept.setColor(getColor());
            }
            if (getAddRelatedConceptWhiteList() != null) {
                concept.getAddRelatedConceptWhiteList().addAll(getAddRelatedConceptWhiteList());
            }
            if (getIntents() != null) {
                concept.getIntents().addAll(Arrays.asList(getIntents()));
            }
            if (this.properties != null) {
                for (OntologyProperty property : this.properties) {
                    concept.getProperties().add(property.getTitle());
                }
            }
            for (Map.Entry<String, String> additionalProperty : getMetadata().entrySet()) {
                concept.getMetadata().put(additionalProperty.getKey(), additionalProperty.getValue());
            }
            return concept;
        } catch (UnsupportedEncodingException e) {
            throw new VisalloException("bad encoding", e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getDisplayName(), getIRI());
    }

    public static Collection<ClientApiOntology.Concept> toClientApiConcepts(Iterable<Concept> concepts) {
        Collection<ClientApiOntology.Concept> results = new ArrayList<>();
        for (Concept concept : concepts) {
            results.add(concept.toClientApi());
        }
        return results;
    }

    public abstract void setProperty(String name, Object value, Authorizations authorizations);

    public abstract void removeProperty(String name, Authorizations authorizations);

    public abstract byte[] getGlyphIcon();

    public abstract byte[] getMapGlyphIcon();

    public abstract String getIRI();

    public abstract String[] getIntents();

    public abstract void addIntent(String intent, Authorizations authorizations);

    @Override
    public int hashCode() {
        return getIRI().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Concept)) {
            return false;
        }
        Concept other = (Concept) obj;
        return getIRI().equals(other.getIRI());
    }
}
