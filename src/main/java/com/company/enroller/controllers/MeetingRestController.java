package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @GetMapping
    public ResponseEntity<Collection<Meeting>> getMeetings() {
        return ResponseEntity.ok(meetingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(meeting);
    }

    @PostMapping
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {

        if (meeting.getId() != 0) {
            Meeting foundMeeting = meetingService.findById(meeting.getId());

            if (foundMeeting != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Already exists");
            }
        }

        meetingService.add(meeting);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(meeting);
    }

}