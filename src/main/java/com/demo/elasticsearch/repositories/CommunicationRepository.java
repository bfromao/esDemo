package com.demo.elasticsearch.repositories;

import com.demo.elasticsearch.domain.Communication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CommunicationRepository extends ElasticsearchRepository<Communication, String>{
    List<Communication> findByBusinessKey(String businessKey);
}
