
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final Point[] points;
    private final List<LineSegment> segments;
    private final Point[][] foundedPoints;
    private int numberOfSegments = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument is null");
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        checkPointsCorrectness();
        this.segments = new ArrayList<>();
        this.foundedPoints = new Point[points.length][points.length];
        initPointsStorage();
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
                                this.segments.add(new LineSegment(first, last));
                                this.savePoints(first, last);
                                this.numberOfSegments++;
                            }
                        }
                    }

                }

            }

        }

    }

    private void initPointsStorage() {
        for (int i = 0; i < points.length; i++) {
            this.foundedPoints[i][0] = points[i];
        }
    }

    private boolean arePointsFoundAlready(Point first, Point last) {
        for (Point[] foundedPoint : foundedPoints) {
            if (foundedPoint[0] == first) {
                for (int j = 1; j < foundedPoint.length; j++) {
                    if (foundedPoint[j] == last) return true;
                }
            }
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
        if (last == null) throw new NullPointerException();
        return last;
    }

    private Point getFirstPoint(Point... localPoints) {
        Point first = localPoints[0];
        for (int i = 1; i < localPoints.length; i++) {
            if (localPoints[i].compareTo(first) < 0) {
                first = localPoints[i];
            }
        }
        if (first == null) throw new NullPointerException();
        return first;
    }

    private void checkPointsCorrectness() {
        Point previous = points[1];
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
            if (previous.compareTo(point) == 0) throw new IllegalArgumentException();
            previous = point;
        }
    }

    private boolean pointsSloped(Point a, Point b, Point c, Point d) {
        return Double.compare(a.slopeTo(b), b.slopeTo(c)) == 0
                && Double.compare(b.slopeTo(c), c.slopeTo(d)) == 0;
    }
}