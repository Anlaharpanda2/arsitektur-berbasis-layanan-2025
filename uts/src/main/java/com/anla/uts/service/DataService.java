package com.anla.uts.service;

import com.anla.uts.model.JsonData;
import com.anla.uts.repository.JsonDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    @Autowired
    private XmlToJsonService xmlToJsonService;

    @Autowired
    private JsonDataRepository jsonDataRepository;

    @Transactional
    public void processAndSaveData(MultipartFile xmlFile) throws IOException {
        String jsonContent = xmlToJsonService.convertXmlToJson(xmlFile);

        // Clear old data and save the new one.
        jsonDataRepository.deleteAll();
        
        JsonData jsonData = new JsonData(jsonContent);
        jsonDataRepository.save(jsonData);
    }

    @Transactional(readOnly = true)
    public Optional<String> getLatestData() {
        // Find the first (and only) entry.
        List<JsonData> allData = jsonDataRepository.findAll();
        if (allData.isEmpty()) {
            return Optional.empty();
        } else {
            // Return the content of the first entry found
            return Optional.of(allData.get(0).getContent());
        }
    }
}
