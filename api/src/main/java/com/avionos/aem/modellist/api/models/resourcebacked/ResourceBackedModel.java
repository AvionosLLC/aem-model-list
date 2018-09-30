package com.avionos.aem.modellist.api.models.resourcebacked;

/**
 * Represents a model class instantiated from a Sling Resource.
 */
public interface ResourceBackedModel {

    String getType();

    String getPath();

    String getName();

}
