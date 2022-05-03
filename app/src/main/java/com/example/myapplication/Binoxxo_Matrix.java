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

    private boolean has_unique_rowcol() {
        int[][] trans = this.transpose();
        int count = 0;
        boolean holds = false;

        //check matrix
        for (int row = 0; row < this.rows; row++) {
            //cant check uniqueness if two entries are missing
            count = 0;
            holds = false;
            for (int a = 0; a < this.cols; a++) {
                if (this.matrix[row][a] == this.init) {
                    count += 1;
                }
            }
            if (count >= 2) {
                holds = true;
            }

            for (int col = 0; col < this.cols; col++) {
                if (this.matrix[row] != this.matrix[col]) {
                    holds = true;
                }
            }
            if (!holds) {
                return holds;
            }
        }

        //check transposed
        count = 0;
        for (int col = 0; col < this.cols; col++) {
            //cant check uniqueness if two entries are missing
            count = 0;
            for (int a = 0; a < this.cols; a++) {
                if (trans[col][a] == this.init) {
                    count += 1;
                }
            }
            if (count >= 2) {
                holds = true;
            }

            for (int row = 0; row < this.rows; row++) {
                if (trans[col] != trans[row]) {
                    holds = true;
                }
            }
            if (!holds) {
                return holds;
            }
        }
        return true;
    }

    public boolean rule_unique() {
        return this.has_unique_rowcol();
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