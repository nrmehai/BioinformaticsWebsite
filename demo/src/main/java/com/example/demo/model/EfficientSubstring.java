package com.example.demo.model;

public class EfficientSubstring {
    private StringBuilder sb;

    public EfficientSubstring(String initial) {
        sb = new StringBuilder(initial);
    }

    // O(1) complexity
    public void update() {
    	sb.append(sb.charAt(0)); // Put first character at the end
        if (sb.length() > 0) {
            sb.deleteCharAt(0); // Remove the first character so it "rotates"
        }
    }

    public String toString() {
        return sb.toString();
    }
}
