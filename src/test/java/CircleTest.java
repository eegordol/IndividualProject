import app.Circle;
import misc.Vector2d;
import org.junit.Test;

public class CircleTest {
    /**
     * Circle.contains тест
     * @param expected ожижвемый результат Circle.contains
     * @param center центр окружности
     * @param radius радиус окружности
     * @param p тестируемая точка
     */
    public void circleContainsTest(boolean expected, Vector2d center, double radius, Vector2d p){
        var circle = new Circle(center, radius);
        assert expected == circle.contains(p);
    }

    /**
     * Circle.contains тест
     */
    @Test
    public void circleContainsTest(){
        circleContainsTest(true, new Vector2d(0, 0), 10, new Vector2d(0, 0));
        circleContainsTest(true, new Vector2d(0, 0), 10, new Vector2d(0, 10));
        circleContainsTest(true, new Vector2d(0, 0), 10, new Vector2d(10, 0));
        circleContainsTest(true, new Vector2d(0, 0), 10, new Vector2d(5, 5));
        circleContainsTest(false, new Vector2d(0, 0), 10, new Vector2d(10, 10));
    }
}
