package com.example.demo.service;

public class SAlign {
	//Global Alignment
	public static Object[] globalAlign(String x, String y, int mismatch_penalty, int gap_penalty) {
		int[][] matrix = new int[y.length() + 1][x.length() + 1];

		// Initialize scoring matrix
		for (int i = 0; i <= y.length(); i++) {
			matrix[i][0] = gap_penalty * i;
		}
		for (int j = 0; j <= x.length(); j++) {
			matrix[0][j] = gap_penalty * j;
		}

		// Fill scoring matrix
		for (int i = 1; i <= y.length(); i++) {
			for (int j = 1; j <= x.length(); j++) {
				int matchMismatch;

				if (y.charAt(i - 1) != x.charAt(j - 1)) {
					matchMismatch = mismatch_penalty + matrix[i - 1][j - 1];
				}
				else {
					matchMismatch = matrix[i - 1][j - 1];
				}

				int insert = matrix[i - 1][j] + gap_penalty;
				int delete = matrix[i][j - 1] + gap_penalty;

				matrix[i][j] = Math.max(matchMismatch, Math.max(insert, delete));
			}
		}

		// Compute CIGAR string using traceback
		int i = y.length(), j = x.length();
		StringBuilder cigar = new StringBuilder();
		char prev_op = '?';
		int count = 0;

		//Loop until both i and j are 0
		while (i > 0 || j > 0) {
			char curr_op;
			
			//Match
			if(i > 0 && j > 0 && y.charAt(i-1) == x.charAt(j-1)) {
				curr_op = '=';
				i--; j--;
			}
			//Mismatch
			else if (i > 0 && j > 0 && (matrix[i][j] == matrix[i - 1][j - 1] + mismatch_penalty)) {
				curr_op = 'X';
				i--; j--;
			} 
			//Delete
			else if (i > 0 && matrix[i][j] == matrix[i - 1][j] + gap_penalty) {
				curr_op = 'D';
				i--;
			} 
			//Insert
			else if(j > 0 && matrix[i][j] == matrix[i][j - 1] + gap_penalty) {
				curr_op = 'I';
				j--;
			}
			//Insert
			else {
				System.out.println("Failure");
				return null;
			}

			// Update CIGAR
			if (curr_op == prev_op) {
				count++;
			} 
			else {
				if (prev_op != '?') {
					cigar.insert(0, count + "" + prev_op);
				}
				prev_op = curr_op;
				count = 1;
			}
		}
		// Add the last operation
		cigar.insert(0, count + "" + prev_op);

		return new Object[]{matrix[y.length()][x.length()], cigar.toString()};
		//return matrix[y.length()][x.length()] + "\t0\t" + y.length() + "\t" + cigar.toString();
	}


	//Fitting Alignment
	public static Object[] fittingAlign(String x, String y, int mismatch_penalty, int gap_penalty) {
		String tmp = x;
		x = y;
		y = tmp;
		int[][] matrix = new int[y.length() + 1][x.length() + 1];

		// Initialize scoring matrix
		for (int i = 0; i <= y.length(); i++) {
			matrix[i][0] = gap_penalty * i;
		}
		for (int j = 0; j <= x.length(); j++) {
			matrix[0][j] = 0;
		}

		// Fill scoring matrix
		for (int i = 1; i <= y.length(); i++) {
			for (int j = 1; j <= x.length(); j++) {
				int matchMismatch;

				if (y.charAt(i - 1) != x.charAt(j - 1)) {
					matchMismatch = mismatch_penalty + matrix[i - 1][j - 1];
				}
				else {
					matchMismatch = matrix[i - 1][j - 1];
				}

				int insert = matrix[i - 1][j] + gap_penalty;
				int delete = matrix[i][j - 1] + gap_penalty;

				matrix[i][j] = Math.max(matchMismatch, Math.max(insert, delete));
			}
		}

		// Compute CIGAR string using traceback
		int i = y.length(), j = x.length();
		StringBuilder cigar = new StringBuilder();
		char prev_op = '?';
		int count = 0;
		
		//Adjust j to be the max
		int max = Integer.MIN_VALUE, max_index = j;
		for(j = x.length(); j >= 0; j--) {
			if(matrix[i][j] > max) {
				max = matrix[i][j];
				max_index = j;
			}
		}
		j = max_index;

		//Loop until both i and j are 0
		while (i > 0 && j > 0) {
			char curr_op;
			
			//Match
			if(i > 0 && j > 0 && y.charAt(i-1) == x.charAt(j-1)) {
				curr_op = '=';
				i--; j--;
			}
			//Mismatch
			else if (i > 0 && j > 0 && (matrix[i][j] == matrix[i - 1][j - 1] + mismatch_penalty)) {
				curr_op = 'X';
				i--; j--;
			} 
			//Delete
			else if (i > 0 && matrix[i][j] == matrix[i - 1][j] + gap_penalty) {
				curr_op = 'I';
				i--;
			} 
			//Insert
			else {
				curr_op = 'D'; 
				j--;
			}

			// Update CIGAR
			if (curr_op == prev_op) {
				count++;
			} 
			else {
				if (prev_op != '?') {
					cigar.insert(0, count + "" + prev_op);
				}
				prev_op = curr_op;
				count = 1;
			}
		}
		// Add the last operation
		cigar.insert(0, count + "" + prev_op);

		return new Object[]{matrix[y.length()][max_index], j, max_index, cigar.toString()};
	}


	public static void printMatrix(int[][] matrix) {
		String matrixString = "";

		for(int i = matrix.length - 1; i >= 0; i--) {
			for(int j = 0; j < matrix[i].length; j++) {
				System.out.print(i +"." +j + "\t");
				matrixString += matrix[i][j] + "\t";
			}
			System.out.println();
			matrixString += "\n";
		}

		System.out.println(matrixString);
	}
	
	public static void debugHelper(String x, String y, int mismatch_penalty, int gap_penalty) {
		String tmp = x;
		x = y;
		y = tmp;
		int[][] matrix = new int[y.length() + 1][x.length() + 1];

		// Initialize scoring matrix
		for (int i = 0; i <= y.length(); i++) {
			matrix[i][0] = gap_penalty * i;
		}
		for (int j = 0; j <= x.length(); j++) {
			matrix[0][j] = 0;
		}

		// Fill scoring matrix
		for (int i = 1; i <= y.length(); i++) {
			for (int j = 1; j <= x.length(); j++) {
				int matchMismatch;

				if (y.charAt(i - 1) != x.charAt(j - 1)) {
					matchMismatch = mismatch_penalty + matrix[i - 1][j - 1];
				}
				else {
					matchMismatch = matrix[i - 1][j - 1];
				}

				int insert = matrix[i - 1][j] + gap_penalty;
				int delete = matrix[i][j - 1] + gap_penalty;

				matrix[i][j] = Math.max(matchMismatch, Math.max(insert, delete));
			}
		}

		// Compute CIGAR string using traceback
		int i = y.length(), j = x.length();
		StringBuilder cigar = new StringBuilder();
		char prev_op = '?';
		int count = 0;
		
		//Adjust j to be the max
		int max = Integer.MIN_VALUE, max_index = j;
		for(j = x.length(); j >= 0; j--) {
			if(matrix[i][j] > max) {
				max = matrix[i][j];
				max_index = j;
			}
		}
		j = max_index;

		//Loop until both i and j are 0
		while (i > 0 && j > 0) {
			char curr_op;
			
			//Match
			if(i > 0 && j > 0 && y.charAt(i-1) == x.charAt(j-1)) {
				curr_op = '=';
				i--; j--;
			}
			//Mismatch
			else if (i > 0 && j > 0 && (matrix[i][j] == matrix[i - 1][j - 1] + mismatch_penalty)) {
				curr_op = 'X';
				i--; j--;
			} 
			//Delete
			else if (i > 0 && matrix[i][j] == matrix[i - 1][j] + gap_penalty) {
				curr_op = 'D';
				i--;
			} 
			//Insert
			else {
				curr_op = 'I'; 
				j--;
			}

			// Update CIGAR
			if (curr_op == prev_op) {
				count++;
			} 
			else {
				if (prev_op != '?') {
					cigar.insert(0, count + "" + prev_op);
				}
				prev_op = curr_op;
				count = 1;
			}
		}
		// Add the last operation
		cigar.insert(0, count + "" + prev_op);

		printMatrix(matrix);
		System.out.println(x);
		System.out.println(y);
		System.out.println(matrix[y.length()][max_index] + "\t" + j + "\t" + max_index + "\t" + cigar.toString());
	}
}
