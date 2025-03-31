package com.example.demo.model;

import java.util.ArrayList;

public class FM_QueryObject {
    private String query;
    private int match_len;
    private int occs;
    private ArrayList<Integer> occ_list;
    
    public FM_QueryObject(String query, int match_len, int occs, ArrayList<Integer> occ_list){
        this.query = query;
        this.match_len = match_len;
        this.occs = occs;
        this.occ_list = occ_list;
    }

    public String getQuery() {return query;}
    public int getMatch_len() {return match_len;}
    public int getOccs() {return occs;}
    public ArrayList<Integer> getOcc_list() {return occ_list;}
}
