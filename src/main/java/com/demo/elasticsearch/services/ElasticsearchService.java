package com.demo.elasticsearch.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.demo.elasticsearch.exceptions.ElasticSearchException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    private ElasticsearchClient elasticsearchRestClient;

    /***
     * Checks of an index is present.
     * @param indexName index name.
     * @return {@link Boolean} representing if index is present or not.
     * @throws ElasticSearchException if ioexception occurs during processing.
     */
    public boolean hasIndex(String indexName) throws ElasticSearchException {

        boolean result = false;
        try {
            BooleanResponse operationResult = elasticsearchRestClient.indices().exists(ExistsRequest.of(e -> e.index(indexName)));
            result = operationResult.value();
            log.info("Exists index " + indexName + ": " + result);

        } catch (IOException e) {
            log.error("{}", e.getMessage());
            throw new ElasticSearchException("Error checking for index in elastic search.");
        }

        return result;
    }

    /***
     * Creates an index.
     * @param indexName index name.
     * @return {@link Boolean} representing if index is created or not.
     * @throws ElasticSearchException if ioexception occurs during processing.
     */
    public boolean createIndex(String indexName) throws ElasticSearchException {
        boolean result = false;
        if (hasIndex(indexName)) {
            throw new ElasticSearchException("Index already exists.");
        }

        try {
            CreateIndexResponse operationResult = elasticsearchRestClient.indices().create(CreateIndexRequest.of(e -> e.index(indexName)));
            result = operationResult.acknowledged();
            log.info("Index " + indexName + " created with result " + result + ".");
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            throw new ElasticSearchException("Error creating index " + indexName + "in elastic search.");
        }

        return result;
    }

    /***
     * Save document to index and generates an auto id.
     * @param indexName index name.
     * @param object object to add.
     * @return {@code Pair<Boolean, String> } first if save or not and the second the id.
     * @throws ElasticSearchException if ioexception occurs during processing.
     */
    public Pair<Boolean, String> saveDocumentToIndexAutoId(String indexName, HashMap<String, Object> object) throws ElasticSearchException {
        UUID timebaseUUID = Generators.timeBasedGenerator().generate();
        return saveDocumentToIndex(indexName, timebaseUUID.toString(), object);
    }

    /***
     * Save document to index and generates an uses object key id.
     * @param indexName index name.
     * @param object object to add.
     * @return {@code Pair<Boolean, String> } first if save or not and the second the id.
     * @throws ElasticSearchException if ioexception occurs during processing; if key id is not present in the object.
     */
    public Pair<Boolean, String> saveDocumentToIndexUseObjectId(String indexName, HashMap<String, Object> object) throws ElasticSearchException {
        if (!object.containsKey("id")) {
            log.error("Id value in object not found. - {}", object);
            throw new ElasticSearchException("Id value in object not found.");
        }
        return saveDocumentToIndex(indexName, String.valueOf(object.get("id")), object);
    }

    /***
     * Save document to index.
     * @param indexName index name.
     * @param id document id.
     * @param object object to add.
     * @return {@code Pair<Boolean, String> } first if save or not and the second the id.
     * @throws ElasticSearchException if ioexception occurs during processing.
     */
    private Pair<Boolean, String> saveDocumentToIndex(String indexName, String id, HashMap<String, Object> object) throws ElasticSearchException {
        Pair<Boolean, String> result = Pair.of(true, id);
        try {
            CreateResponse a = elasticsearchRestClient.create(s -> s.document(object).index(indexName).id(id));
            if (!a.result().name().equalsIgnoreCase("created")) {
                result = Pair.of(false, "Response was not created.");
            }
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            throw new ElasticSearchException("Error creating index " + indexName + "in elastic search.");
        }

        return result;
    }

    /***
     * Get a document by id in an index.
     * @param index index name.
     * @param id document id.
     * @return {@link ObjectNode} document.
     * @throws ElasticSearchException if ioexception occurs during processing.
     */
    public ObjectNode getById(String index, String id) throws ElasticSearchException {

        GetResponse<ObjectNode> response = null;
        try {
            response = elasticsearchRestClient.get(g -> g
                            .index(index)
                            .id(id),
                    ObjectNode.class
            );
        } catch (IOException e) {
            throw new ElasticSearchException("Error finding document by id.");
        }
        return response.source();
    }
}
