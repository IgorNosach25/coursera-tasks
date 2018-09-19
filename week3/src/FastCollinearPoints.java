
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final Point[] points;
    private final Point[] copyPoints;
    private final List<LineSegment> segments;
    private final List<Pair<Point, Point>> foundedPoints;
    private int numberOfSegments = 0;

    public FastCollinearPoints(final Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument is null");
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        checkPointsCorrectness();
        this.copyPoints = new Point[points.length];
        this.segments = new ArrayList<>();
        this.foundedPoints = new ArrayList<>();
        findAllSegments();
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegment = new LineSegment[segments.size()];
        return segments.toArray(lineSegment);
    }

    private void findAllSegments() {
        for (int p = 0; p < points.length; p++) {
            Point point = points[p];
            System.arraycopy(points, 0, copyPoints, 0, points.length);
            Arrays.sort(copyPoints, point.slopeOrder());
            for (int q = 0; q < points.length; q++) {
                if (q != p) {
                    double slope = point.slopeTo(points[q]);
                    int randomPointIndex = binarySearchBySlope(slope, point);
                    int firstPointIndexInOrderSet = findFirstPointIndexInPointSet(randomPointIndex, point, slope);
                    int lastPointIndexInOrderSet = findLastPointInPointSet(firstPointIndexInOrderSet, point, slope);
                    Arrays.sort(copyPoints, firstPointIndexInOrderSet, lastPointIndexInOrderSet);
                    Point smallestPoint = copyPoints[firstPointIndexInOrderSet].compareTo(point) < 0
                            ? copyPoints[firstPointIndexInOrderSet] : point;
                    Point biggestPoint = copyPoints[lastPointIndexInOrderSet].compareTo(point) > 0
                            ? copyPoints[lastPointIndexInOrderSet] : point;
                    if (!arePointsFoundAlready(smallestPoint, biggestPoint) && lastPointIndexInOrderSet - firstPointIndexInOrderSet >= 2) {
                        savePoints(smallestPoint, biggestPoint);
                        segments.add(new LineSegment(smallestPoint, biggestPoint));
                        numberOfSegments++;
                    }
                }
            }

        }

    }

    private int findFirstPointIndexInPointSet(int pos, Point point, double slope) {
        int hi = pos;
        int lo = 1;
        int mid;
        int first = pos;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            double localSlope = copyPoints[mid].slopeTo(point);
            if (Double.compare(localSlope, slope) == 0) {
                first = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }

        }
        return first;
    }

    private int findLastPointInPointSet(int pos, Point point, double slope) {
        int lo = pos;
        int hi = copyPoints.length - 1;
        int mid;
        int lastPos = pos;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            double localSlope = copyPoints[mid].slopeTo(point);
            if (Double.compare(localSlope, slope) != 0) {
                hi = mid - 1;
            } else {
                lastPos = mid;
                lo = mid + 1;
            }

        }
        return lastPos;
    }

    private int binarySearchBySlope(double slope, Point point) {
        int lo = 1;
        int hi = copyPoints.length - 1;
        int mid;
        while (lo <= hi) {
            mid = lo + hi >>> 1;
            if (copyPoints[mid].slopeTo(point) > slope) {
                hi = mid - 1;
            } else if (copyPoints[mid].slopeTo(point) < slope) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private boolean arePointsFoundAlready(Point first, Point last) {
        for (Pair<Point, Point> points : foundedPoints) {
            if (points.getKey() == first && points.getValue() == last) return true;
        }
        return false;
    }

    private void savePoints(Point first, Point last) {
        foundedPoints.add(new Pair<>(first, last));
    }

    private void checkPointsCorrectness() {
        Point previous = points[1];
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
            if (previous.compareTo(point) == 0) throw new IllegalArgumentException();
            previous = point;
        }
    }
}