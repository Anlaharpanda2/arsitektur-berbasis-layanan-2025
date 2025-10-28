package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

@Data
public class Activity {
    @JacksonXmlProperty(isAttribute = true)
    private String id;

    private String value;
}
