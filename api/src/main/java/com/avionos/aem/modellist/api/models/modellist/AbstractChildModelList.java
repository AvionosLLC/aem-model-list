package com.avionos.aem.modellist.api.models.modellist;

import com.avionos.aem.modellist.api.models.resourcebacked.ResourceBackedModel;
import com.google.common.collect.Lists;
import org.apache.sling.api.resource.Resource;

import java.util.List;

public abstract class AbstractChildModelList<T extends ResourceBackedModel> implements ModelList<T> {

    private List<T> items;

    @Override
    public List<T> getItems() {
        if (items != null) {
            return items;
        }

        items = Lists.newArrayList();

        getResource().getChildren().forEach(currentChild -> {
            T currentItem = currentChild.adaptTo(getType());

            if (currentItem != null) {
                items.add(currentItem);
            }
        });

        return items;
    }

    public abstract Class<T> getType();

    public abstract Resource getResource();

}

