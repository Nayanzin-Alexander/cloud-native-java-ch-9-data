package com.nayanzin.data;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class BaseEntity {

    private DateTime lastModified;

    private DateTime createdAt;
}
