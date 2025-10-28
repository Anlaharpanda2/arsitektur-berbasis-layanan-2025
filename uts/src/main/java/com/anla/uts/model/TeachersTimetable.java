package com.anla.uts.model;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "Teachers_Timetable")
public class TeachersTimetable {
    @JacksonXmlProperty(localName = "Teacher")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Teacher> teachers;
}