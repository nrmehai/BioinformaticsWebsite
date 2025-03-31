package com.example.demo.controller;

import com.example.demo.service.FM_index;
//import com.example.demo.service.GenomeService;
import com.example.demo.service.SAlign;
import com.example.demo.model.GlobalAlignResponse;
//import com.example.demo.model.GenomeObject;
import com.example.demo.model.FM_QueryObject;
import com.example.demo.model.FM_indexObject;
import com.example.demo.model.FittingAlignResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Optional;

// Serves REST API endpoints
@RestController
public class AlgorithmController {
    //@Autowired
    //private GenomeService genomeService;


    @GetMapping("/global-alignment")
    public GlobalAlignResponse globalAlignment(@RequestParam String sequence1, @RequestParam String sequence2, @RequestParam Integer mismatch_penalty, @RequestParam Integer gap_penalty) {
        Object[] alignmentResponse = SAlign.globalAlign(sequence1, sequence2, mismatch_penalty * -1, gap_penalty * -1);
        return new GlobalAlignResponse((Integer) alignmentResponse[0], (String) alignmentResponse[1]);
    }

    @GetMapping("/fitting-alignment")
    public FittingAlignResponse fittingAlignment(@RequestParam String sequence1, @RequestParam String sequence2, @RequestParam Integer mismatch_penalty, @RequestParam Integer gap_penalty){
        Object[] alignmentResponse = SAlign.fittingAlign(sequence1, sequence2, mismatch_penalty * -1, gap_penalty * -1);
        return new FittingAlignResponse((Integer) alignmentResponse[0], (Integer) alignmentResponse[1], (Integer) alignmentResponse[2], (String) alignmentResponse[3]);
    }

    @GetMapping("/fm-index")
    public FM_indexObject fmIndex(@RequestParam String genome){
        Object[] response = FM_index.buildFM(genome);
        if(response == null){
            return null;
        }
        return new FM_indexObject((int[]) response[0], (int[]) response[1], (int[][]) response[2], (String) response[3]);
    }

    @GetMapping("/partial-query")
    public FM_QueryObject partialQuery(@RequestParam String query){
        Object[] response = FM_index.partialQueryOutput(query);
        if(response == null){
            return null;
        }
        return new FM_QueryObject((String) response[0], (Integer) response[1], (Integer) response[2], (ArrayList<Integer>) response[3]);
    }

    @GetMapping("/complete-query")
    public FM_QueryObject completeQuery(@RequestParam String query){
        Object[] response = FM_index.completeQueryOutput(query);
        if(response == null){
            return null;
        }
        return new FM_QueryObject((String) response[0], (Integer) response[1], (Integer) response[2], (ArrayList<Integer>) response[3]);
    }
/** 
    // GET - If ID is provided, return that specific genome; if not, return all genomes
    @GetMapping("/database")
    public ResponseEntity<?> getGenome(@RequestParam Optional<Integer> id) {
        if (id.isPresent()) {
            Optional<GenomeObject> genome = genomeService.getGenomeById(id.get());
            return genome.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } else {
            // Return all genomes if no ID is provided
            return ResponseEntity.ok(genomeService.getAllGenomes());
        }
    }

    // POST - Create a new genome
    @PostMapping("/database")
    public ResponseEntity<GenomeObject> createGenome(@RequestBody GenomeObject genomeObject) {
        GenomeObject createdGenome = genomeService.createOrUpdateGenome(genomeObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenome);
    }

    // PUT - Update genome if ID is present
    @PutMapping("database/{id}")
    public ResponseEntity<GenomeObject> updateGenome(@PathVariable int id, @RequestBody GenomeObject genomeObject) {
        GenomeObject updatedGenome = genomeService.createOrUpdateGenome(genomeObject);
        return ResponseEntity.ok(updatedGenome);
    }

    // DELETE - Delete genome by ID
    @DeleteMapping("database/{id}")
    public ResponseEntity<Void> deleteGenome(@PathVariable int id) {
        genomeService.deleteGenome(id);
        return ResponseEntity.noContent().build();
    }
        */
}
