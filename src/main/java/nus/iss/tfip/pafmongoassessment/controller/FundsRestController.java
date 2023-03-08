package nus.iss.tfip.pafmongoassessment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.tfip.pafmongoassessment.service.LogAuditService;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path = "/logs")
public class FundsRestController {

    @Autowired
    private LogAuditService logSvc;

    @GetMapping
    public ResponseEntity<List<Document>> getAllLogs() {
        List<Document> docs = logSvc.getAllLogs();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(docs);
    }
}
