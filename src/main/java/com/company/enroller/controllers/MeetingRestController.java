package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;

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
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        meetingService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(
            @PathVariable("id") Long id,
            @RequestBody Meeting meeting) {

        Meeting foundMeeting = meetingService.findById(id);

        if (foundMeeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        foundMeeting.setTitle(meeting.getTitle());
        foundMeeting.setDescription(meeting.getDescription());
        foundMeeting.setDate(meeting.getDate());

        meetingService.update(foundMeeting);

        return new ResponseEntity<>(foundMeeting, HttpStatus.OK);
    }
    @PostMapping("/{id}/participants")
    public ResponseEntity<?> addParticipantToMeeting(
            @PathVariable Long id,
            @RequestParam String login) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Participant participant = participantService.findByLogin(login);

        if (participant == null) {
            return new ResponseEntity<>("Participant not found", HttpStatus.NOT_FOUND);
        }

        meeting.addParticipant(participant);

        meetingService.update(meeting);

        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }
    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getMeetingParticipants(@PathVariable Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(meeting.getParticipants());
    }
    @DeleteMapping("/{id}/participants/{login}")
    public ResponseEntity<?> removeParticipantFromMeeting(
            @PathVariable Long id,
            @PathVariable String login) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Participant participant = participantService.findByLogin(login);

        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        meeting.removeParticipant(participant);

        meetingService.update(meeting);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/")
    public String home() {
        return "AGH MWO Enroller API is running";
    }
}