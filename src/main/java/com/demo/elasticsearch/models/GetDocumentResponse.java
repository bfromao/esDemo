package com.demo.elasticsearch.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDocumentResponse extends StandardResponse {
    private ObjectNode document;
}
