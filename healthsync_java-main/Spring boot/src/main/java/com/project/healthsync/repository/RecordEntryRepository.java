package com.project.healthsync.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.healthsync.entity.RecordEntry;

public interface RecordEntryRepository extends MongoRepository<RecordEntry,ObjectId>{

}
