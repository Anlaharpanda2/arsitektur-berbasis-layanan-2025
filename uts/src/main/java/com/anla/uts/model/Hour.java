package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

@Data
public class Hour {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Activity")
    private String activity;

    @JacksonXmlProperty(localName = "Subject")
    private String subject;

    @JacksonXmlProperty(localName = "Activity_Tag")
    private String activityTag;

    @JacksonXmlProperty(localName = "Students")
    private String students;

    @JacksonXmlProperty(localName = "Room")
    private String room;
}
