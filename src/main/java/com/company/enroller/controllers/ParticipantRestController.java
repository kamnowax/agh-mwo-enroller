package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;


@RestController
@RequestMapping("/participants") //endpoint
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;


//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> getParticipants() {
//		Collection<Participant> participants = participantService.getAll();
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}
    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("login") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant != null) {
            return new ResponseEntity<>("Already registered", HttpStatus.CONFLICT);
        }
        participantService.add(participant);
        return new ResponseEntity<>(participant, HttpStatus.CREATED);    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("login") String login) {

        Participant participant = participantService.findByLogin(login);

        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        participantService.delete(login);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(
            @PathVariable("login") String login,
            @RequestBody Participant participant) {

        Participant foundParticipant = participantService.findByLogin(login);

        if (foundParticipant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        foundParticipant.setPassword(participant.getPassword());

        participantService.update(foundParticipant);

        return new ResponseEntity<>(foundParticipant, HttpStatus.OK);
    }
//    @GetMapping
//    public ResponseEntity<?> getParticipantsSorted(
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(required = false) String sortOrder) {
//
//        Collection<Participant> participants =
//                participantService.getAll(sortBy, sortOrder);
//
//        return ResponseEntity.ok(participants);
//    }
    @GetMapping
    public ResponseEntity<?> getParticipantsSortedByLogin(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String key) {

        Collection<Participant> participants =
                participantService.getAll(sortBy, sortOrder, key);

        return ResponseEntity.ok(participants);
    }

    }



