package com.project.healthsync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.healthsync.entity.RecordEntry;
import com.project.healthsync.entity.User;
import com.project.healthsync.repository.RecordEntryRepository;

@Component
public class RecordEntryService {
    
    @Autowired
    private RecordEntryRepository recordEntryRepository;
    
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(RecordEntry recordEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            recordEntry.setDate(LocalDateTime.now());
            RecordEntry saved = recordEntryRepository.save(recordEntry);
            user.getRecordEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }

    public void saveEntry(RecordEntry recordEntry) {
        recordEntryRepository.save(recordEntry);
    }

    public List<RecordEntry> getAll() {
        return recordEntryRepository.findAll();
    }

    public Optional<RecordEntry> findById(ObjectId id) {
        return recordEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getRecordEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                recordEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }


}
