import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;
    private final Point foundedPoints[][];
    private int numberOfSegments = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException ("Argument is null");
        this.points = points;
        segments = new LineSegment[points.length];
        foundedPoints = new Point[points.length][points.length];
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            foundedPoints[i][0] = points[i];
        }

        if (containsRepeatedPoint()) throw new IllegalArgumentException("Array contains repeated point!");
        findAllSegments();
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        return segments;
    }

    private void findAllSegments() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i == j) continue;
                for (int k = 0; k < points.length; k++) {
                    if (k == i || k == j) continue;
                    for (int c = 0; c < points.length; c++) {
                        if (c == i || c == j || c == k) continue;
                        boolean isSloped = pointsSloped(points[i], points[j], points[k], points[c]);
                        if (isSloped) {
                            Point first = getFirstPoint(points[i], points[j], points[k], points[c]);
                            Point last = getLastPoint(points[i], points[j], points[k], points[c]);
                            if (!arePointsFoundAlready(first, last)) {
                                this.segments[numberOfSegments++] = new LineSegment(first, last);
                                this.savePoints(first, last);
                            }
                        }
                    }

                }

            }

        }

    }

    private boolean arePointsFoundAlready(Point first, Point last) {
        for (Point[] foundedPoint : foundedPoints) {
            if (foundedPoint[0] == first)
                for (int j = 1; j < foundedPoint.length; j++) {
                    if (foundedPoint[j] == last) return true;}
        }
        return false;
    }

    private void savePoints(Point first, Point last) {
        for (int i = 0; i < foundedPoints.length; i++) {
            if (foundedPoints[i][0] == first) {
                for (int j = 1; j < foundedPoints[i].length; j++) {
                    if (foundedPoints[i][j] == null) {
                        foundedPoints[i][j] = last;
                        return;
                    }
                }
            }
        }
    }

    private Point getLastPoint(Point... localPoints) {
        Point last = localPoints[0];
        for (int i = 1; i < localPoints.length; i++) {
            if (localPoints[i].compareTo(last) > 0) {
                last = localPoints[i];
            }
        }
        if (last == null) throw new RuntimeException();
        return last;
    }

    private Point getFirstPoint(Point... localPoints) {
        Point first = localPoints[0];
        for (int i = 1; i < localPoints.length; i++) {
            if (localPoints[i].compareTo(first) < 0) {
                first = localPoints[i];
            }
        }
        if (first == null) throw new RuntimeException();
        return first;
    }

    private boolean containsRepeatedPoint() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pointsSloped(Point a, Point b, Point c, Point d) {
        double v1 = a.slopeTo(b);
        double v2 = b.slopeTo(c);
        double v3 = c.slopeTo(d);
        double v4 = d.slopeTo(a);
        return v1 == v2 && v2 == v3 && v3 == v4;
    }
}