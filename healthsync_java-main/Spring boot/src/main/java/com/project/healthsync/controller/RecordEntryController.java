package com.project.healthsync.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.healthsync.entity.RecordEntry;
import com.project.healthsync.entity.User;
import com.project.healthsync.service.RecordEntryService;
import com.project.healthsync.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/record")
public class RecordEntryController {
    
    @Autowired
    private RecordEntryService recordEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllRecordEntriesOfUser() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user = userService.findByUserName(userName);
        List<RecordEntry> all = user.getRecordEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<RecordEntry> collect = user.getRecordEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<RecordEntry> journalEntry = recordEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<RecordEntry> createEntry(@RequestBody RecordEntry myEntry) {
        try {
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            recordEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteRecordEntryById(@PathVariable ObjectId myId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed = recordEntryService.deleteById(myId, userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateRecordById(@PathVariable ObjectId myId, @RequestBody RecordEntry newEntry) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user = userService.findByUserName(userName);
        List<RecordEntry> collect = user.getRecordEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
        Optional<RecordEntry> recordEntry = recordEntryService.findById(myId);
        if(recordEntry.isPresent()){
            RecordEntry old=recordEntry.get();
            old.setName(newEntry.getName()!=null && !newEntry.getName().equals("")? newEntry.getName():old.getName());
            old.setSymptoms(newEntry.getSymptoms()!=null && !newEntry.getSymptoms().equals("")? newEntry.getSymptoms():old.getSymptoms());
            recordEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }   
    }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
