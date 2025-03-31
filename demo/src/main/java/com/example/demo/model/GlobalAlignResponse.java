package com.example.demo.model;

public class GlobalAlignResponse {
    private int score;
    private String cigar_string;

    public GlobalAlignResponse(int score, String cigar_string) {
        this.score = score;
        this.cigar_string = cigar_string;
    }

    public int getScore() { return score; }
    public String getCIGAR() { return cigar_string; }
    public String toString() {
        return getScore() + "\t" + getCIGAR();
    }
}

/** 
package com.example.demo.model;

@Entity
public class GenomeObject {
    private String genome;
    private String name;
    private int id;

    public GenomeObject(String genome, String name, int id){
        this.genome = genome;
        this.name = name;
        this.id = id;
    }

    public String getGenome() {return genome;}
    public String getName() {return name;}
    public int getId() {return id;}

    public void setName(String name) {this.name = name;}
    public void setGenome(String genome) {this.genome = genome;}
    
}*/
