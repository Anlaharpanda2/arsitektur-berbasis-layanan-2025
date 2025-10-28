package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
public class Teacher {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Day")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Day> days;
}
