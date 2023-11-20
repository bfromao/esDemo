package com.demo.elasticsearch.controllers;

import com.demo.elasticsearch.exceptions.ElasticSearchException;
import com.demo.elasticsearch.domain.Communication;
import com.demo.elasticsearch.models.CreateDocumentResponse;
import com.demo.elasticsearch.models.CreateIndexRequest;
import com.demo.elasticsearch.models.GetDocumentResponse;
import com.demo.elasticsearch.models.StandardResponse;
import com.demo.elasticsearch.services.ElasticsearchService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ElasticSearchController {

    @Autowired
    ElasticsearchService elasticsearchService;


    @GetMapping("hasIndex")
    ResponseEntity<StandardResponse> getAll(@RequestParam String indexName) {
        HttpStatus responseHttpStatus;
        StandardResponse responseBody;
        try {
            var hasIndex = elasticsearchService.hasIndex(indexName);
            responseHttpStatus = HttpStatus.OK;
            responseBody = new StandardResponse(hasIndex, null);
        } catch (ElasticSearchException e) {
            responseHttpStatus = HttpStatus.NO_CONTENT;
            responseBody = new StandardResponse(false, e.getMessage());
        } catch (Exception ee) {
            responseHttpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseBody = new StandardResponse(false, "Unknown exception.");
        }
        return ResponseEntity.status(responseHttpStatus).body(responseBody);
    }

    @PostMapping("createIndex")
    ResponseEntity<StandardResponse> createIndex(@RequestBody CreateIndexRequest request) {
        HttpStatus responseHttpStatus;
        StandardResponse responseBody;
        try {
            var hasIndex = elasticsearchService.createIndex(request.getIndex());
            responseHttpStatus = HttpStatus.OK;
            responseBody = new StandardResponse(hasIndex, null);
        } catch (ElasticSearchException e) {
            responseHttpStatus = HttpStatus.NO_CONTENT;
            responseBody = new StandardResponse(false, e.getMessage());
        } catch (Exception ee) {
            responseHttpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseBody = new StandardResponse(false, "Unknown exception.");
        }
        return ResponseEntity.status(responseHttpStatus).body(responseBody);
    }

    @PostMapping("addDocument/autoId/{index}")
    ResponseEntity<CreateDocumentResponse> addDocumentAutoId(@PathVariable String index, @RequestBody HashMap<String, Object> request) {
        HttpStatus responseHttpStatus;
        CreateDocumentResponse responseBody = new CreateDocumentResponse();
        try {
            var hasIndex = elasticsearchService.saveDocumentToIndexAutoId(index, request);
            if (hasIndex.getFirst()) {
                responseBody.setSuccess();
                responseBody.setDocumentId(hasIndex.getSecond());
            } else {
                responseBody.setFail(hasIndex.getSecond());
            }
            responseHttpStatus = HttpStatus.OK;
        } catch (ElasticSearchException e) {
            responseHttpStatus = HttpStatus.NO_CONTENT;
            responseBody.setFail(e.getMessage());
        } catch (Exception ee) {
            responseHttpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseBody.setFail("Unknown exception.");
        }
        return ResponseEntity.status(responseHttpStatus).body(responseBody);
    }

    @PostMapping("addDocument/{index}")
    ResponseEntity<CreateDocumentResponse> addDocument(@PathVariable String index, @RequestBody HashMap<String, Object> request) {
        HttpStatus responseHttpStatus;
        CreateDocumentResponse responseBody = new CreateDocumentResponse();
        try {
            var saveDocumentResult = elasticsearchService.saveDocumentToIndexUseObjectId(index, request);
            if (saveDocumentResult.getFirst()) {
                responseBody.setSuccess();
                responseBody.setDocumentId(saveDocumentResult.getSecond());
            } else {
                responseBody.setFail(saveDocumentResult.getSecond());
            }
            responseHttpStatus = HttpStatus.OK;
        } catch (ElasticSearchException e) {
            responseHttpStatus = HttpStatus.NO_CONTENT;
            responseBody.setFail(e.getMessage());
        } catch (Exception ee) {
            responseHttpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseBody.setFail("Unknown exception.");
        }
        return ResponseEntity.status(responseHttpStatus).body(responseBody);
    }

    @GetMapping("findDocument/{index}/{id}")
    ResponseEntity<GetDocumentResponse>  listById(@PathVariable String index, @PathVariable String id) {
        HttpStatus responseHttpStatus;
        GetDocumentResponse responseBody = new GetDocumentResponse();
        try {
            responseBody.setDocument(elasticsearchService.getById(index, id));
            responseBody.setSuccess();
            responseHttpStatus = HttpStatus.OK;
        } catch (ElasticSearchException e) {
            responseHttpStatus = HttpStatus.NO_CONTENT;
            responseBody.setFail(e.getMessage());
        } catch (Exception ee) {
            responseHttpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseBody.setFail("Unknown exception.");
        }
        return ResponseEntity.status(responseHttpStatus).body(responseBody);

    }


}
