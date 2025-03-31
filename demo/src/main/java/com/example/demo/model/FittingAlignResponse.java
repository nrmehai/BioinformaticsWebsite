package com.example.demo.model;

public class FittingAlignResponse {
    private int score;
    private int y_start;
    private int y_end;
    private String cigar_string;

    public FittingAlignResponse(int score, int y_start, int y_end, String cigar_string) {
        this.score = score;
        this.y_start = y_start;
        this.y_end = y_end;
        this.cigar_string = cigar_string;
    }

    public int getScore() { return score; }
    public int getY_start() {return y_start;}
    public int getY_end() {return y_end;}
    public String getCIGAR() { return cigar_string; }
    public String toString() {
        return getScore() + "\t" + getCIGAR();
    }
}