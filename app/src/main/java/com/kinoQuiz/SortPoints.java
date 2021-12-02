package com.kinoQuiz;

import java.util.Comparator;

public class SortPoints implements Comparator<Point> {

    @Override
    public int compare(Point o1, Point o2) {
        if (Double.valueOf(o2.getScore()) < Double.valueOf(o1.getScore())) return -1;
        else if (Double.valueOf(o2.getScore()) == Double.valueOf(o1.getScore())) return 0;
        else return 1;
    }
}
