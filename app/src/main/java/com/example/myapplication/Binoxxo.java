package com.example.myapplication;

import java.util.ArrayList;

public class Binoxxo
{
    int rows;
    int cols;

    Binoxxo_Matrix matrix;
    Binoxxo_Matrix original;
    Binoxxo_Matrix solution;

    ArrayList<Integer> written;

    int[] rows_zero;
    int[] rows_ones;
    int[] cols_zero;
    int[] cols_ones;


    public Binoxxo(int rows,int cols)
    {
        if (rows % 2 != 0 || rows != cols)
        {
            throw new RuntimeException("Need even number of rows and columns and they must be equal.");
        }
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Binoxxo_Matrix(rows,cols,Helper.INIT);
        this.original = new Binoxxo_Matrix(rows,cols,Helper.INIT);
        this.solution = new Binoxxo_Matrix(rows,cols,Helper.INIT);
        this.written = new ArrayList<Integer>();
        this.rows_zero = new int[this.rows];
        this.rows_ones = new int[this.rows];
        this.cols_zero = new int[this.cols];
        this.cols_ones = new int[this.cols];

        for(int i = 0; i < this.rows; i++)
        {
            rows_zero[i] = this.rows/2;
            rows_ones[i] = this.rows/2;
            cols_zero[i] = this.rows/2;
            cols_ones[i] = this.rows/2;
        }
    }


    //operations after creation
    public void set_entry(int row,int col,int entry)
    {
        if (entry == 1)
        {
            this.matrix.set_entry(row,col,1);
            written.add(col);
            this.rows_ones[row] -= 1;
            this.cols_ones[col] -= 1;
        }
        else if (entry == 0)
        {
            this.matrix.set_entry(row,col,0);
            written.add(col);
            this.rows_zero[row] -= 1;
            this.cols_zero[col] -= 1;
        }
    }

    public void delete_entry(int row,int col,String content)
    {
        this.matrix.delete_entry(row,col);
        if (content.equals("X"))
        {
            this.rows_ones[row] += 1;
            this.cols_ones[col] += 1;
        }
        else if (content.equals("O"))
        {
            this.rows_zero[row] += 1;
            this.cols_zero[col] += 1;
        }
    }


    public void add_entry(int row,int col,String content)
    {
        if (content.equals("X"))
        {
            this.matrix.set_entry(row,col,1);
            this.rows_ones[row] -= 1;
            this.cols_ones[col] -= 1;
        }
        else if (content.equals("O"))
        {
            this.matrix.set_entry(row,col,0);
            this.rows_zero[row] -= 1;
            this.cols_zero[col] -= 1;
        }
        else
        {
            throw new RuntimeException("The entries should be X or O, nothing else.");
        }
    }

    public void reload_grid()
    {
        this.matrix.copy(this.original);
    }

    private void rule1_col(int row)
    {
        int[][] matrix = this.matrix.get_matrix();
        if (row > 1)
        {
            for(int col = 0; col < this.cols; col++)
            {
                if (!this.written.contains(col))
                {
                    if (matrix[row-1][col] == matrix[row-2][col] && matrix[row-1][col] != Helper.INIT)
                    {
                        if (matrix[row-1][col] == 0) { this.set_entry(row,col,1); }
                        else if (matrix[row-1][col] == 1) { this.set_entry(row,col,0); }
                    }
                    else if (row < this.rows-1 && matrix[row-1][col] == matrix[row+1][col] && matrix[row-1][col] != Helper.INIT)
                    {
                        if (matrix[row-1][col] == 0) { this.set_entry(row,col,1); }
                        else if (matrix[row-1][col] == 1) {this.set_entry(row,col,0); }
                    }
                }
            }
        }
    }

    private void rule2_col(int row)
    {
        if (row == this.rows/2)
        {
            for(int col = 0; col < this.cols; col++)
            {
                if (!this.written.contains(col))
                {
                    if (this.cols_zero[col] < 2) { this.set_entry(row,col,1); }
                    else if (this.cols_ones[col] < 2) { this.set_entry(row,col,0); }
                }
            }
        }
    }

    private void rule1_row(int row,int col)
    {
        int[][] matrix = this.matrix.get_matrix();
        if (col > 1)
        {
            if (matrix[row][col-1] == matrix[row][col-2] && matrix[row][col-1] != Helper.INIT)
            {
                if (matrix[row][col-1] == 0) { this.set_entry(row,col,1); }
                else if (matrix[row][col-1] == 1) { this.set_entry(row,col,0); }
            }
            else if (col < this.cols-1 && matrix[row][col-1] == matrix[row][col+1] && matrix[row][col-1] != Helper.INIT)
            {
                if (matrix[row][col-1] == 0) { this.set_entry(row,col,1); }
                else if (matrix[row][col-1] == 1) {this.set_entry(row,col,0); }
            }
        }
    }

    private void rule2_row(int row,int col)
    {
        if (col == this.cols/2 && !this.written.contains(col))
        {
            if (this.rows_zero[row] < 2) { this.set_entry(row,col,1); }
            else if (this.rows_ones[row] < 2) { this.set_entry(row,col,0); }
        }
    }

    private void fill_rest(int row,int col)
    {
        if (!this.written.contains(col)) {
            if (this.rows_zero[row] == 0 || this.cols_zero[col] == 0)
            {
                this.set_entry(row, col, 1);
            }
            else if (this.rows_ones[row] == 0 || this.cols_ones[col] == 0)
            {
                this.set_entry(row, col, 0);
            }
            else
            {
                boolean e = Helper.DECISION(Helper.FILL_PROB);
                if (e)
                {
                    this.set_entry(row, col, 1);
                }
                else if (!e)
                {
                    this.set_entry(row, col, 0);
                }
            }
        }
    }

    private void create_row(int row)
    {
        this.written = new ArrayList<Integer>();
        if (row < this.rows-1)
        {
            this.rule1_col(row);
            this.rule2_col(row);
            if (this.written.size() < this.cols)
            {
                for(int col = 0; col < this.cols; col++)
                {
                    if (!this.written.contains(col))
                    {
                        this.rule1_row(row,col);
                        this.rule2_row(row,col);
                        this.fill_rest(row,col);
                    }
                }
            }
        }
        else if (row == this.rows-1)
        {
            for(int col = 0; col < this.cols; col++)
            {
                this.fill_rest(row,col);
            }
        }
    }

    private void set_shown()
    {
        for(int row = 0; row < this.rows; row++)
        {
            for(int col = 0; col < this.cols; col++)
            {
                boolean show = Helper.DECISION(Helper.SHOW_PROB);
                if (!show) { this.matrix.set_entry(row,col,Helper.INIT); }
            }
        }
    }

    public void create()
    {
        for(int row = 0; row < this.rows; row++)
        {
            this.create_row(row);
        }
        this.solution.copy(this.matrix);
        this.set_shown();
        this.original.copy(this.matrix);
    }

    public boolean binoxxo_is_valid()
    {
        return this.solution.is_valid();
    }
}