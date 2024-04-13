package ru.iu3.backend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.repositories.MuseumRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/museums")
    public List
    getAllMuseums() {
        return museumRepository.findAll();
    }

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum)
            throws Exception {
        try {
            Museum nc = museumRepository.save(museum);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("museums.name_UNIQUE"))
                error = "museum already exists";
            else if (ex.getMessage().contains("not-null"))
                error = "museum name cannot be null";
            else
                error = "undefined error";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
            //return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumId,
                                                 @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum>
                cc = museumRepository.findById(museumId);
        if (cc.isPresent()) {
            museum = cc.get();
            museum.name = museumDetails.name;
            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "museum not found");
        }
    }


    @DeleteMapping("/museums/{id}")
    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long museumId) {
        Optional<Museum>
                museum = museumRepository.findById(museumId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (museum.isPresent()) {
            museumRepository.delete(museum.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
