import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private Point[] copyPoints;
    private LineSegment[] segments;
    private final Point foundedPoints[][];
    private int numberOfSegments = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument is null");
        this.points = points;
        copyPoints = new Point[points.length];
        segments = new LineSegment[points.length];
        foundedPoints = new Point[points.length][points.length];
        for (int i = 0; i < points.length; i++) {
            foundedPoints[i][0] = points[i];
        }

        if (containsRepeatedPoint()) throw new IllegalArgumentException("Array contains repeated point!");
        findAllSegments();
        LineSegment[] lineSegments = new LineSegment[numberOfSegments];

        for (int i = 0; i < segments.length; i++) {
            if (segments[i] != null) {
                lineSegments[i] = segments[i];
            }
        }
        this.segments = lineSegments;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] temp = new LineSegment[numberOfSegments];
        System.arraycopy(segments, 0, temp, 0, numberOfSegments);
        return temp;
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
                    Point firstPoint = getFirstPoint(point, copyPoints[lastPointInOrderSet], copyPoints[firstPointInOrderSet]);
                    Point lastPoint = getLastPoint(point, copyPoints[lastPointInOrderSet], copyPoints[firstPointInOrderSet]);
                    if (!arePointsFoundAlready(firstPoint, lastPoint) && lastPointInOrderSet - firstPointInOrderSet >= 2) {
                        savePoints(firstPoint, lastPoint);
                        segments[numberOfSegments++] = new LineSegment(firstPoint, lastPoint);
                    }
                }
            }

        }

    }

    private int findLastPointInPointSet(int pos, Point point, double slope) {
        int lo = pos;
        int hi = copyPoints.length - 1;
        int mid;
        int lastPos = pos;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            double localSlope = copyPoints[mid].slopeTo(point);
            if (localSlope != slope) {
                hi = mid - 1;
            } else {
                lastPos = mid;
                lo = mid + 1;
            }

        }
        return lastPos;
    }

    private int findFirstPointInPointSet(int pos, Point point, double slope) {
        int hi = copyPoints.length - 1;
        int lo = 1;
        int mid;
        int first = pos;
        while (lo <= hi) {
            mid = (lo + hi) >>> 1;
            double localSlope = copyPoints[mid].slopeTo(point);
            if (localSlope == slope) {
                first = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }

        }
        return first;
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

}