package com.anla.uts.controller;

import com.anla.uts.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/input", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleInput(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"File is empty\"}");
        }
        // Basic validation for XML file type
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".xml")) {
            return ResponseEntity.badRequest().body("{\"error\": \"File must be in XML format\"}");
        }

        try {
            dataService.processAndSaveData(file);
            return ResponseEntity.ok("{\"message\": \"Data processed and saved successfully\"}");
        } catch (Exception e) {
            // Log the exception for debugging purposes
            // log.error("Error processing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to process file: " + e.getMessage() + "}");
        }
    }

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getData() {
        return dataService.getLatestData()
                .map(data -> ResponseEntity.ok().body(data))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"No data found. Please POST an XML file to /api/input first.\"}"));
    }
}
