package com.project.healthsync.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.healthsync.entity.User;


public interface UserRepository extends MongoRepository<User,ObjectId>{

    User findByusername(String username);

    void deleteByusername(String name);

}
