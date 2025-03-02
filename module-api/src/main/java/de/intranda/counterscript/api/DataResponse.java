package de.intranda.counterscript.api;

import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
public @Data class DataResponse {

    private String test;
}
