package com.demo.elasticsearch.services;

import com.demo.elasticsearch.domain.Communication;
import com.demo.elasticsearch.repositories.CommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CommunicationService {
    private final CommunicationRepository repository;

    @Autowired
    public CommunicationService(CommunicationRepository repository) {
        this.repository = repository;
    }

    /***
     * Saves a {@link Communication }  in to the persistence layer.
     * @param communication the communication to save.
     * @return a value indicating if object was saved or not.
     */
    public boolean save(Communication communication) {
        Communication result = repository.save(communication);
        return result.getId() != null;
    }


    public List<Communication> getByBusinessKey(String businessKey){
        return repository.findByBusinessKey(businessKey);
    }
    public List<Communication> getAll(){
        return Streamable.of(repository.findAll()).toList();
    }
}
