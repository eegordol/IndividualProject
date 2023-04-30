import app.Circle;
import app.Ray;
import misc.Misc;
import misc.Vector2d;
import org.junit.Test;

public class RayTest {

    /**
     * Ray.contains test
     * @param expected ожижвемый результат Ray.contains
     * @param r1 первая точка луча
     * @param r2 вторая точка луча
     * @param p тестируемая точка
     */
    private void rayContainsTest(boolean expected, Vector2d r1, Vector2d r2, Vector2d p) {
        var ray = new Ray(r1, r2);
        assert expected == ray.contains(p);
    }

    /**
     * Ray.contains test
     */
    @Test
    public void rayContainsTest() {
        rayContainsTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, -1));
        rayContainsTest(false, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, 1));

        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(0, 0));
        rayContainsTest( false, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(1, 2));

        // граничный тест
        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(1, 1.5));
        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(3, 1));
        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(-1, 2));
        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(-1.5, 0));
        rayContainsTest( true, new Vector2d(3, 1), new Vector2d(-1, 2), new Vector2d(2, -3));
    }


    /**
     * Ray.intersects(Circle) test
     * @param expected ожижвемый результат Ray.intersects(Circle)
     * @param r1 первая точка луча
     * @param r2 вторая точка луча
     * @param center центр окружности
     * @param radius радиус окружности
     */
    private void rayIntersectsCircleTest(boolean expected, Vector2d r1, Vector2d r2, Vector2d center, double radius) {
        var ray = new Ray(r1, r2);
        var circle = new Circle(center, radius);
        assert expected == ray.intersects(circle);
    }

    /**
     * Ray.intersects(Circle) test
     */
    @Test
    public void rayIntersectsCircleTest() {
        rayIntersectsCircleTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, -1), 0);
        rayIntersectsCircleTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, -1), 1);
        rayIntersectsCircleTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, -1), 10);
        rayIntersectsCircleTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(1, 0), 3);
        rayIntersectsCircleTest( true, new Vector2d(1, 0), new Vector2d(-1, 0), new Vector2d(0, 2), 3);
    }

    /**
     * Тестирует Misc.signedDistanceToLine в обеих полуплоскостях
     * @param expectedDistance ожидаемое расстояние (со знаком) от прямой p1 -> p2 до точки x
     * @param p1 первая точка примой
     * @param p2 вторая точка прямой
     * @param x тестируемая точка
     */
    private static void distanceToLineTest(double expectedDistance, Vector2d p1, Vector2d p2, Vector2d x) {
        double dist = Misc.signedDistanceToLine(p1, p2, x);
        assert Math.abs( expectedDistance - dist) < 1e-8;

        dist = Misc.signedDistanceToLine(p2, p1, x);
        assert Math.abs( -expectedDistance - dist) < 1e-8;

    }

    /**
     * Misc.signedDistanceToLine тест
     */
    @Test
    public void distanceToLineTest(){
        distanceToLineTest( 4, new Vector2d(0, 0), new Vector2d(0, 10), new Vector2d(-4, 8));
        distanceToLineTest( 8, new Vector2d(0, 0), new Vector2d(10, 0), new Vector2d(-4, 8));
        distanceToLineTest( 0.5 * Math.sqrt(2), new Vector2d(1, 0), new Vector2d(0, 1), new Vector2d(0, 0));
        distanceToLineTest(-4.5 * Math.sqrt(2), new Vector2d(1, 0), new Vector2d(0, 1), new Vector2d(5, 5));
        distanceToLineTest(0, new Vector2d(3, 2), new Vector2d(0, 7), new Vector2d(6, -3));

        distanceToLineTest(-6 * Math.cos(Math.atan2(3, 5)), new Vector2d(3, 2), new Vector2d(-3, 12), new Vector2d(6, 7));
    }

    /**
     * Тестирует Vector2.rotate
     * @param expected ожидаемый вектор
     * @param v исходный вектор
     * @param angleDeg угол поворота в градусах
     */
    private void vectorRotateTest(Vector2d expected, Vector2d v, double angleDeg){
        double angle = angleDeg / 180.0 * Math.PI;
        Vector2d res = v.rotate(angle);
        Vector2d diff = Vector2d.subtract(res, expected);
        assert diff.length() < 1e-8;
    }

    /**
     * Тестирует Vector2.rotate
     */
    @Test
    public void vectorRotateTest(){
        vectorRotateTest(new Vector2d(0, 10), new Vector2d(10, 0), 90);
        vectorRotateTest(new Vector2d(0, -10), new Vector2d(10, 0), -90);
        vectorRotateTest(new Vector2d(5 * Math.sqrt(2), 5 * Math.sqrt(2)), new Vector2d(10, 0), 45);
    }


}