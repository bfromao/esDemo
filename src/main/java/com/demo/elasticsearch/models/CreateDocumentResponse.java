package com.demo.elasticsearch.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentResponse extends StandardResponse {

    private String documentId;

}
