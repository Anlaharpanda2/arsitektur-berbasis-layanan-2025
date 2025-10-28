package com.anla.uts.service;

import com.anla.uts.model.TeachersTimetable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class XmlToJsonService {
    
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    public String convertXmlToJson(MultipartFile file) throws IOException {
        // Parse XML
        TeachersTimetable timetable = xmlMapper.readValue(
            file.getInputStream(), 
            TeachersTimetable.class
        );
        
        // Convert to JSON
        return jsonMapper.writeValueAsString(timetable);
    }
    
    public byte[] convertXmlToJsonBytes(MultipartFile file) throws IOException {
        String json = convertXmlToJson(file);
        return json.getBytes();
    }
}