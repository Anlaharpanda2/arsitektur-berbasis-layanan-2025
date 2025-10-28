package com.anla.uts.controller;

import com.anla.uts.service.XmlToJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ConversionController {

    @Autowired
    private XmlToJsonService conversionService;

    @PostMapping(value = "/konfersi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertXmlToJson(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\": \"File is empty\"}");
            }

            if (!file.getOriginalFilename().endsWith(".xml")) {
                return ResponseEntity.badRequest().body("{\"error\": \"File must be XML format\"}");
            }

            String jsonResult = conversionService.convertXmlToJson(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResult);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}