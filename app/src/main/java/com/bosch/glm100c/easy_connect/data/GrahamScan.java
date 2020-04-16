package com.bosch.glm100c.easy_connect.data;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public final class GrahamScan {
    protected  enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }
    protected boolean areAllCollinear(List<Point> points) {
        if(points.size() < 2) {
            return true;
        }
        Point a = points.get(0);
        Point b = points.get(1);
        for(int i = 2; i < points.size(); i++) {
            Point c = points.get(i);
            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }
        return true;
    }
    public List<Point> getConvexHull(int[] xs, int[] ys){
        List<Point> points = new ArrayList<Point>();
        if(xs.length != ys.length) {
            return points;
        }
        for(int i = 0; i < xs.length; i++) {
            points.add(new Point(xs[i], ys[i]));
        }
        return getConvexHull(points);
    }
    public List<Point> getConvexHull(List<Point> points){
        List<Point> sorted = new ArrayList<Point>(getSortedPointSet(points));
        if(sorted.size() < 3) {
            return points;
        }
        if(areAllCollinear(sorted)) {
            return points;
        }
        Stack<Point> stack = new Stack<Point>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));
        for (int i = 2; i < sorted.size(); i++) {
            Point head = sorted.get(i);
            Point middle = stack.pop();
            Point tail = stack.peek();
            Turn turn = getTurn(tail, middle, head);
            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }
        stack.push(sorted.get(0));
        return new ArrayList<Point>(stack);
    }
    protected static Point getLowestPoint(List<Point> points) {
        Point lowest = points.get(0);
        for(int i = 1; i < points.size(); i++) {
            Point temp = points.get(i);
            if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;
            }
        }
        return lowest;
    }

    
    protected Set<Point> getSortedPointSet(List<Point> points) {
        final Point lowest = getLowestPoint(points);
        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {
                if(a == b || a.equals(b)) {
                    return 0;
                }
                double thetaA = Math.atan2((long)a.y - lowest.y, (long)a.x - lowest.x);
                double thetaB = Math.atan2((long)b.y - lowest.y, (long)b.x - lowest.x);
                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {           
                    double distanceA = Math.sqrt((((long)lowest.x - a.x) * ((long)lowest.x - a.x)) +
                                                (((long)lowest.y - a.y) * ((long)lowest.y - a.y)));
                    double distanceB = Math.sqrt((((long)lowest.x - b.x) * ((long)lowest.x - b.x)) +
                                                (((long)lowest.y - b.y) * ((long)lowest.y - b.y)));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });
        set.addAll(points);
        return set;
    }
    protected  Turn getTurn(Point a, Point b, Point c) {
        long crossProduct = (((long)b.x - a.x) * ((long)c.y - a.y)) -
                            (((long)b.y - a.y) * ((long)c.x - a.x));
        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
    public void testGrahamScan(){
        List<Point> points = Arrays.asList(
        new Point(3, 9),
        new Point(5, 2),
        new Point(-1, -4),
        new Point(8, 3),
        new Point(-6, 90),
        new Point(23, 3),
        new Point(4, -11));
        GrahamScan grahamScan = new GrahamScan();
        List<Point> convexHull = grahamScan.getConvexHull(points);
        for(Point p : convexHull) {
            System.out.println(p);
        }
    }
}