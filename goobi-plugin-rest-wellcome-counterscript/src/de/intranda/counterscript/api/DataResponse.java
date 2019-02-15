package de.intranda.counterscript.api;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
public @Data class DataResponse {

    private String test;
}
