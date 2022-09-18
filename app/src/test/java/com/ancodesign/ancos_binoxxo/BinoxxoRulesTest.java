package com.anncodesign.ancos_binoxxo;

import org.junit.Assert;
import org.junit.Test;

public class BinoxxoRulesTest {
    @Test
    public void test_unique_rule() {
        Binoxxo_Matrix matrix = new Binoxxo_Matrix(4,4,-1);
        matrix.matrix = new int[][] {
                {0,1,0,1}, {1,0,1,0}, {0,1,0,1}, {1,0,1,0}
        };
        Assert.assertFalse("Wrongly detected as unique rows/cols",matrix.rule_unique());
        matrix.matrix = new int[][] {
                {0,1,0,1}, {1,1,1,1}, {0,0,0,0}, {0,1,0,1}
        };
        Assert.assertFalse("Wrongly detected as unique rows",matrix.rule_unique());
        matrix.matrix = new int[][] {
                {0,1,0,0}, {0,1,1,1}, {0,1,0,0}, {0,1,1,1}
        };
        Assert.assertFalse("Wrongly detected as unique cols",matrix.rule_unique());
    }

    @Test
    public void test_exactly_four_rule() {
        Binoxxo_Matrix matrix = new Binoxxo_Matrix(4,4,-1);
        matrix.matrix = new int[][] {
                {0,1,0,1}, {1,1,1,0}, {0,0,0,1}, {1,0,1,0}
        };
        Assert.assertFalse("Did not detect 3 vs O in rows",matrix.rule_four());
        matrix.matrix = new int[][] {
                {0,1,0,1}, {0,1,1,0}, {0,1,0,1}, {1,0,1,0}
        };
        Assert.assertFalse("Did not detect 3 vs 1 in a col",matrix.rule_four());
        matrix.matrix = new int[][] {
                {0,1,0,1}, {0,1,1,0}, {0,1,0,1}, {0,1,1,0}
        };
        Assert.assertFalse("Did not detect 4 equal in a col",matrix.rule_four());
    }

    @Test
    public void test_no_more_than_two_adjacent_rule() {
        Binoxxo_Matrix matrix = new Binoxxo_Matrix(6,6,-1);
        matrix.matrix = new int[][] {
                {0,1,0,1,0,1}, {1,1,1,0,0,0}, {0,0,0,1,1,1},
                {1,0,1,0,1,0}, {1,1,0,1,0,0}, {0,0,1,0,1,1}
        };
        Assert.assertFalse("Did not detect 3 adjacent equals in row/col",matrix.rule_three());
    }

    @Test
    public void test_valid_binoxxo() {
        Binoxxo_Matrix matrix = new Binoxxo_Matrix(4,4,-1);
        matrix.matrix = new int[][] {
                {0,1,0,1}, {1,1,0,0}, {0,0,1,1}, {1,0,1,0}
        };
        Assert.assertTrue("Did not accept valid Binoxxo.",matrix.is_valid());
    }
}
