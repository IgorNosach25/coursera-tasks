
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final Point[] points;
    private final Point[] copyPoints;
    private final List<LineSegment> segments;
    private final Point[][] foundedPoints;
    private int numberOfSegments = 0;

    public FastCollinearPoints(final Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument is null");
        this.points = Arrays.copyOf(points, points.length);
        checkCorrectnessOfPoints();
        this.copyPoints = new Point[points.length];
        this.segments = new ArrayList<>();
        this.foundedPoints = new Point[points.length][points.length];
        for (int i = 0; i < points.length; i++) {
            foundedPoints[i][0] = points[i];
        }
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
                    int index = binarySearchBySlope(slope, point);
                    int firstPointInOrderSet = findFirstPointInPointSet(index, point, slope);
                    int lastPointInOrderSet = findLastPointInPointSet(firstPointInOrderSet, point, slope);
                    Point firstPoint = getFirstPoint(firstPointInOrderSet, lastPointInOrderSet, point);
                    Point lastPoint = getLastPoint(firstPointInOrderSet, lastPointInOrderSet, point);
                    if (!arePointsFoundAlready(firstPoint, lastPoint) && lastPointInOrderSet - firstPointInOrderSet >= 2) {
                        savePoints(firstPoint, lastPoint);
                        segments.add(new LineSegment(firstPoint, lastPoint));
                        numberOfSegments++;
                    }
                }
            }

        }

    }

    private int findFirstPointInPointSet(int pos, Point point, double slope) {
        int first = pos;
        while (first > 1 && Double.compare(copyPoints[first - 1].slopeTo(point), slope) == 0) {
            first--;
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
        for (Point[] foundedPoint : foundedPoints) {
            if (foundedPoint[0] == first)
                for (int j = 1; j < foundedPoint.length; j++) {
                    if (foundedPoint[j] == last) return true;
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
                        break;
                    }
                }
            }
        }
    }

    private Point getFirstPoint(int from, int to, Point point) {
        Point first = point;
        for (int i = from; i <= to; i++) {
            if (copyPoints[i].compareTo(first) < 0) {
                first = copyPoints[i];
            }
        }
        if (first == null) throw new RuntimeException();
        return first;
    }

    private Point getLastPoint(int from, int to, Point point) {
        Point last = point;
        for (int i = from; i <= to; i++) {
            if (copyPoints[i].compareTo(last) > 0) {
                last = copyPoints[i];
            }
        }
        if (last == null) throw new RuntimeException();
        return last;
    }



    private boolean checkCorrectnessOfPoints() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    if (points[j] == null) throw new IllegalArgumentException();
                    else if (points[i].compareTo(points[j]) == 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        return false;
    }
}