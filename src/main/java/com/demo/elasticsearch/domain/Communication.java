package com.demo.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.time.LocalDateTime;
import org.springframework.data.elasticsearch.annotations.DateFormat;

@Getter
@Setter
@Document(indexName = "communication")
public class Communication {

    @Id
    private String id;
    private String businessKey;

//@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
   @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;
    private String url;
    private String request;
    private String response;
    private int httpStatus;
    private int seconds;

}
