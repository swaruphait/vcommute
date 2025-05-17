package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmbeddingConverter {
     public double[] convertToArray(String embeddingJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(embeddingJson, double[].class);
    }
}
