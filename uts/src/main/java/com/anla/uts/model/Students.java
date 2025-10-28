package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

@Data
public class Students {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    private String value;
}
