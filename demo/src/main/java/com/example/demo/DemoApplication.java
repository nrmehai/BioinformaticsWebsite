package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
/** 
 * package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.GenomeObject;
import com.example.demo.repository.GenomeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenomeService {

    private GenomeRepository genomeRepository;

    // Get genome by ID
    public Optional<GenomeObject> getGenomeById(int id) {
        return genomeRepository.findById(id);
    }

    // Get all genomes
    public List<GenomeObject> getAllGenomes() {
        return (List<GenomeObject>) genomeRepository.findAll();
    }

    // Create or update genome
    public GenomeObject createOrUpdateGenome(GenomeObject genomeObject) {
        return genomeRepository.save(genomeObject);  // save will insert or update
    }

    // Delete genome by ID
    public void deleteGenome(int id) {
        genomeRepository.deleteById(id);
    }
}

*/