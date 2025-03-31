package com.example.demo.model;

public class FM_indexObject {
    private int[] sa;
    private int[] indexFCol;
    private int[][] tally;
    private String bwtStr;

    public FM_indexObject(int[] sa, int[] indexFCol, int[][] tally, String bwtStr){
        this.sa = sa;
        this.indexFCol = indexFCol;
        this.tally = tally;
        this.bwtStr = bwtStr;
    }

    public int[] getSA() {return sa;}
    public int[] getIndexFCol() {return indexFCol;}
    public int[][] getTally() {return tally;}
    public String getBWT(){return bwtStr;}
}
