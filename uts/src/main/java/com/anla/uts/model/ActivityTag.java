package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

@Data
public class ActivityTag {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    private String value;
}
