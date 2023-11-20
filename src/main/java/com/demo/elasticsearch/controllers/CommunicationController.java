package com.demo.elasticsearch.controllers;

import com.demo.elasticsearch.services.CommunicationService;
import com.demo.elasticsearch.domain.Communication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communication")
@Slf4j
public class CommunicationController {

    @Autowired
    private CommunicationService communicationService;

    @PostMapping("save")
    ResponseEntity<Object> save(@RequestBody Communication communication) {
        boolean result = communicationService.save(communication);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("getAll")
    List<Communication> getAll() {
        List<Communication> elasticsearchResult = communicationService.getAll();
        log.info("Communication Controller getAll record count {}.", elasticsearchResult.size());
        return elasticsearchResult;
    }

    @GetMapping("get")
    List<Communication> getByBusinessKey(@RequestParam String businessKey) {
        List<Communication> elasticsearchResult = communicationService.getByBusinessKey(businessKey);
        log.info("Communication Controller get by businessKey {} record count {}.", businessKey, elasticsearchResult.size());
        return elasticsearchResult;
    }


}
