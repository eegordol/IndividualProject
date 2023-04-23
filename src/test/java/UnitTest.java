import app.Circle;
import app.Point;
import app.Ray;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс тестирования
 */
public class UnitTest {

    /**
     * Тест
     *
     * @param circles список окружностей
     * @param rays список лучей
     */
    private static void test(ArrayList<Circle> circles, ArrayList<Ray> rays) {
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), circles, rays);
        task.solve();

        // TODO
    }


    /**
     * Первый тест
     */
    @Test
    public void test1() {
        ArrayList<Circle> circles = new ArrayList<>();

        circles.add(new Circle(new Vector2d(0, 0), 1));
        circles.add(new Circle(new Vector2d(2, 0), 2));

        ArrayList<Ray> rays = new ArrayList<>();
        rays.add(new Ray(new Vector2d(1, 0), new Vector2d(-1, 0)));

        test(circles, rays);
    }

}
