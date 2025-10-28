package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
public class Day {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Hour")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Hour> hours;
}
