package com.example.myapplication;

import android.util.Log;

public class Binoxxo_Matrix {

    int rows;
    int cols;
    int init = 9;
    int[][] matrix;

    public String valid_error = "";

    public Binoxxo_Matrix(int rows, int cols, int init) {
        if (rows % 2 != 0 || rows != cols) {
            throw new RuntimeException("Need even number of rows and columns and they must be equal.");
        }

        this.rows = rows;
        this.cols = cols;
        this.init = init;
        this.matrix = new int[rows][cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = this.init;
            }
        }
    }

    public int[][] get_matrix() {
        //TODO: brauch ich hier deepcopy?
        return this.matrix;
    }

    public int get_entry(int row, int col) {
        return this.matrix[row][col];
    }

    public void set_entry(int row, int col, int entry) {
        this.matrix[row][col] = entry;
    }

    public void delete_entry(int row, int col) {
        this.matrix[row][col] = this.init;
    }

    public int[][] transpose() {
        int[][] trans = new int[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                trans[i][j] = this.matrix[j][i];
            }
        }
        return trans;
    }

    //check validity of binoxxo rules

    //does not count init values
    private boolean has_not_three_in_a_rowcol() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                if (col > 0 && col < this.cols - 1) {
                    if (this.matrix[row][col - 1] == this.matrix[row][col] && this.matrix[row][col] == this.matrix[row][col + 1] && this.matrix[row][col] != this.init) {
                        return false;
                    }
                }
                if (row > 0 && row < this.rows - 1) {
                    if (this.matrix[row - 1][col] == this.matrix[row][col] && this.matrix[row][col] == this.matrix[row + 1][col] && this.matrix[row][col] != this.init) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean rule_three() {
        return this.has_not_three_in_a_rowcol();
    }

    private boolean has_exactly_four_xo_in_rowcol() {
        //check for matrix and transposed
        int[][] trans = this.transpose();
        int[] count = {0, 0, 0, 0};

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.matrix[i][j] == 0) {
                    count[0] += 1;
                } else if (this.matrix[i][j] == 1) {
                    count[1] += 1;
                }
                if (trans[i][j] == 0) {
                    count[2] += 1;
                } else if (trans[i][j] == 1) {
                    count[3] += 1;
                }
            }

            for (int a = 0; a < 4; a++) {
                if (count[a] > this.rows / 2) {
                    return false;
                }
                count[a] = 0;
            }
        }
        return true;
    }

    public boolean rule_four() {
        return this.has_exactly_four_xo_in_rowcol();
    }

    private boolean has_unique_rows(int[][] matrix) {
        /* CHECK MATRIX FOR UNIQUE ROWS
        * Iterate over row pairs and check for each column
        * if the entry is equal. If so, we continue until
        * we reach the end of the column. This means, that
        * the two rows are the same and thus, not all rows
        * are unique.
        * In case of an unequal entry, we can break the
        * current loop to save computation time.
        *
        * Args:
        *   matrix: Integer matrix
        *
        * Return:
        *   boolean: Boolean showing if rows are unique or not
        * */
        int rows = matrix.length;
        int cols = matrix[0].length;
        // Check each row against each row
        for (int row1 = 0; row1 < rows; row1++) {
            for (int row2 = row1 + 1; row2 < rows; row2++) {
                for (int col = 0; col < cols; col++) {
                    if (matrix[row1][col] == matrix[row2][col]) {
                        if (col == cols - 1) {
                            return false;
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return true;
    }

    public boolean rule_unique() {
        /* CHECK UNIQUENESS RULE
        * Check on uniqueness in rows and columns by
        * checking for uniqueness in rows for matrix
        * and transposed matrix.
        * */
        int[][] transpose = this.transpose();
        return (this.has_unique_rows(this.matrix) && this.has_unique_rows(transpose));
    }

    public boolean is_valid() {
        if (this.rule_three() && this.rule_four() && this.rule_unique()) {
            this.valid_error = "";
            return true;
        } else {
            this.valid_error = "";
            if (!this.rule_three()) {
                this.valid_error += "There are either more than two 'X' or more than two 'O' next to each other.\n";
                //Log.i("rules","THREE");
            }
            if (!this.rule_four()) {
                this.valid_error += "There may are more than 4 'X' or 'O' in a row or column or not exactly 4 'X' and 4 'O' in a complete row or column.\n";
                //Log.i("rules","FOUR");
            }
            if (!this.rule_unique()) {
                this.valid_error += "There are two complete equal rows or columns.";
                //Log.i("rules","UNIQUE");
            }
        }
        return false;
    }

    public void print_matrix() {
        Log.i("matrix", Integer.toString(this.rows));
        for (int i = 0; i < this.rows; i++) {
            String line = "";
            for (int j = 0; j < this.cols; j++)
                line += Integer.toString(this.matrix[i][j]) + " ";
            Log.i("matrix", line);
        }
    }

    public void copy(Binoxxo_Matrix bin_matrix) {
        if (this.rows == matrix.length && this.cols == matrix[0].length) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    this.matrix[i][j] = bin_matrix.matrix[i][j];
                }
            }
        } else {
            throw new RuntimeException("Matrices must have same dimensions!");
        }
    }

}