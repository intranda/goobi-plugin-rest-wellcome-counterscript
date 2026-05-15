package org.goobi.api.rest;

import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
public @Data class DataResponse {

    private String test;
}
