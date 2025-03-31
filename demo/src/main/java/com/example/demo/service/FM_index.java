package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.demo.model.EfficientSubstring;

public class FM_index {

	static int[] sa = null;
	static int[] indexFCol = null;
	static int[][] tally = null;
	static String bwtStr = "";

	private static int match_len;
	private static ArrayList<Integer> occ_list;
	private static String query;

    public static Object[] buildFM(String genome){
        //build suffix array O(n)
        sa = SuffixArray.constructSuffixArray(genome);

        //build BWT O(n) complexity
		indexFCol = new int[5];
		StringBuffer bwtStr_buf = new StringBuffer();
		tally = new int[4][sa.length];
		int[] charCount = new int[4];
		for(int i = 0; i < sa.length; i++) {
			char c = 0;
			
			if(sa[i] - 1 == -1) {
				c = genome.charAt(sa.length-1);
			}
			else {
				c = genome.charAt(sa[i] - 1);
			}
			
			//Update number of each character for the first column of the FMIndex
			switch(c) {
			case '$':
				indexFCol[0]++;
				break;
			case 'A':
				indexFCol[1]++;
				charCount[0]++;
				break;
			case 'C':
				indexFCol[2]++;
				charCount[1]++;
				break;
			case 'G':
				indexFCol[3]++;
				charCount[2]++;
				break;
			case 'T':
				indexFCol[4]++;
				charCount[3]++;
				break;
				
			default:
				return new Object[] {null, null, null, null};
			
			}
			
			tally[0][i] = charCount[0];
			tally[1][i] = charCount[1];
			tally[2][i] = charCount[2];
			tally[3][i] = charCount[3];
			bwtStr_buf.append(c);
		}
		bwtStr = bwtStr_buf.toString();
		
		
		//build BWM
		boolean bwm = false;
		if(bwm) {
			List<String> rotations = new ArrayList<String>();
			rotations.add(genome);
			for(int i = 0; i < genome.length() - 1; i++) {
				EfficientSubstring newRotation = new EfficientSubstring(rotations.get(i));
				newRotation.update();
				rotations.add(newRotation.toString());
			}
			Collections.sort(rotations);
			
			System.out.println(rotations.toString());
		}


            if(indexFCol[0] != 1 || genome.charAt(genome.length() -1) != '$'){
                return new Object[] {null, null, null, null};
            }


        return new Object[] {sa, indexFCol, tally, bwtStr};
    }

    public static Object[] partialQueryOutput(String input){
		query = input;
		occ_list = new ArrayList<Integer>();
		partialQuery();
        return new Object[] {query, match_len, occ_list.size(), occ_list};
    }

	public static Object[] completeQueryOutput(String input){
		query = input;
		occ_list = new ArrayList<Integer>();
		completeQuery();
		return new Object[] {query, match_len, occ_list.size(), occ_list};
	}

	public static void partialQuery() {
		char c = query.charAt(query.length() - 1);
		int colSkip = 0;
		int colEnd = 0;

		//If the character is not in the genome
		if(c != '$' && !tallyCheck(c)) {
			System.err.println("Is not in the genome");
			return;
		}

		//if the character is in the genome but have to check the next character
		//Get the Beginning and End index for the Column according to the character
		switch (c) {
		case 'T':
			colSkip += indexFCol[3];
			colEnd += indexFCol[4];
		case 'G':
			colSkip += indexFCol[2];
			colEnd += indexFCol[3];
		case 'C':
			colSkip += indexFCol[1];
			colEnd += indexFCol[2];
		case 'A':
			colSkip += indexFCol[0];
			colEnd += indexFCol[1];
			
			//No need for default case. tallyCheck gets rid of any other character
		}
		match_len = 1;


		//Go through the query in reverse
		for(int i = query.length() - 2; i >= 0; i--) {
			c = query.charAt(i);
			//If the character is not in the genome
			if(c != '$' && !tallyCheck(c)) {
				System.err.println("Is not in the genome");
				return;
			}
			int index = letterNum(c);

			//Shorten the window for the character
			//find where letter count increases
			int tmp = colSkip; //use tmp so when it breaks. Can still get partial matches
			while (tally[index][tmp] == tally[index][tmp - 1])
			{
				tmp++;
			}

			//Check if tmp is out of range. If so, break to get the partial matches
			if(colEnd < tmp) {
				break;
			}
			colSkip = tmp;

			// Move colEnd back till where letter count decreases
			while (tally[index][colEnd] == tally[index][colEnd - 1])
			{
				colEnd--;
			}

			//Reduces the range using the ranks
			int[] arr = rankToIndex(c, getRank(c, colSkip) - 1, getRank(c, colEnd) - 1);
			colSkip = arr[0];
			colEnd = arr[1];

			match_len++;
		}

		//add occ indexes
		for(int i = colSkip; i <= colEnd; ++i) {
			occ_list.add(sa[i]);
		}
	}

	public static void completeQuery() {

		char c = query.charAt(query.length() - 1);
		int colSkip = 0;
		int colEnd = 0;

		//If the character is not in the genome
		if(c != '$' && !tallyCheck(c)) {
			System.err.println("Is not in the genome");
			return;
		}

		//if the character is in the genome but have to check the next character
		//Get the Beginning and End index for the Column according to the character
		switch (c) {
		case 'T':
			colSkip += indexFCol[3];
			colEnd += indexFCol[4];
		case 'G':
			colSkip += indexFCol[2];
			colEnd += indexFCol[3];
		case 'C':
			colSkip += indexFCol[1];
			colEnd += indexFCol[2];
		case 'A':
			colSkip += indexFCol[0];
			colEnd += indexFCol[1];

			//No need for default case. tallyCheck gets rid of any other character
		}
		match_len = 1;

		//Go through the query in reverse
		for(int i = query.length() - 2; i >= 0; i--) {
			c = query.charAt(i);
			//If the character is not in the genome
			if(c != '$' && !tallyCheck(c)) {
				System.err.println("Is not in the genome");
				return;
			}
			int index = letterNum(c);

			//Shorten the window for the character
			//find where letter count increases
			while (tally[index][colSkip] == tally[index][colSkip - 1]) {
				colSkip++;
			}

			// Check if colSkip is out of range. If so, no match
			if (colEnd < colSkip) {
				match_len = 0;
				break;
			}

			// Move colEnd back till where letter count decreases
			while (tally[index][colEnd] == tally[index][colEnd - 1]) {
				colEnd--;
			}

			//Reduces the range using the ranks
			int[] arr = rankToIndex(c, getRank(c, colSkip) - 1, getRank(c, colEnd) - 1);
			colSkip = arr[0];
			colEnd = arr[1];

			match_len++;
		}

		//add occ indexes 
		for(int i = colSkip; i <= colEnd; ++i) {
			occ_list.add(sa[i]);
		}
	}

	//Turns Letters to a Value for the tally table O(1)
	private static int letterNum(char c) {
		int letter = -1;
		switch(c) {

		case 'T':
			letter = 3;
			break;
		case 'G':
			letter = 2;
			break;
		case 'C':
			letter = 1;
			break;
		case 'A':
			letter = 0;
			break;
		}

		return letter;
	}

	//gets the rank O(1)
	static int getRank(char c, int j) {
		int letterNum = letterNum(c);
		//if the letter is $
		if(letterNum == -1) {
			return 1;
		}
		return tally[letterNum][j];
	}


	//gets the index for start and end of the sorted string O(1)
	public static int[] letterRange(char c) 
	{
		int l = 0;
		int r = 0;
		switch (c) {
		case 'T':
			l += indexFCol[3];
			r += indexFCol[4];
		case 'G':
			l += indexFCol[2];
			r += indexFCol[3];
		case 'C':
			l += indexFCol[1];
			r += indexFCol[2];
		case 'A':
			l += indexFCol[0];
			r += indexFCol[1];
		}

		int[] arr = new int[2];
		arr[0] = l;
		arr[1] = r;

		return arr;
	}

	//gets the index given the ranks O(1)
	static int[] rankToIndex(char c, int rankL, int rankR) {
		int l = 0;
		switch(c) {
		case 'T':
			l += indexFCol[3];
		case 'G':
			l += indexFCol[2];
		case 'C':
			l += indexFCol[1];
		case 'A':
			l += indexFCol[0];
			break;
		case '$':
			return null;
		}
		return (new int[] {l+rankL, l+rankR});
	}

	//is used to check if the letter is in the genome O(1)
	static boolean tallyCheck(char c) {
		int letter = letterNum(c);
		if(letter == -1) {
			return false;
		}

		boolean hasLetter = false;
		if(tally[letter].length > 0) {
			hasLetter = true;
		}
		return hasLetter;
	}
}
