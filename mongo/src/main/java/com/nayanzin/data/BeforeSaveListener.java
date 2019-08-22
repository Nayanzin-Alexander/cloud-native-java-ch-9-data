package com.nayanzin.data;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<BaseEntity> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseEntity> event) {

        DateTime timestamp = new DateTime();

        BaseEntity source = event.getSource();

        if (isNull(source.getCreatedAt())) {
            source.setCreatedAt(timestamp);
        }

        source.setLastModified(timestamp);

        super.onBeforeSave(event);
    }
}
