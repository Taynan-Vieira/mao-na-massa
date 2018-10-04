package br.com.devdojo.examgenerator.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EndpointUtil implements Serializable {
    public ResponseEntity<?> returnObjectOrNotFound(Object object){
        return object == null ? new ResponseEntity<>(NOT_FOUND) : new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> returnObjectOrNotFound(List<?> list){
        return list == null || list.isEmpty() ? new ResponseEntity<>(NOT_FOUND) : new ResponseEntity<>(list, HttpStatus.OK);
    }
}
