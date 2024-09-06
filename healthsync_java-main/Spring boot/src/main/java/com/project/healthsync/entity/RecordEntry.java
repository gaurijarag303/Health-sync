package com.project.healthsync.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="records")
@Data
@NoArgsConstructor
public class RecordEntry {
    @Id
    private ObjectId id;

    @NonNull
    private String name;
    private String symptoms;
    private LocalDateTime date;
}
