package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.*;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;

import static app.Colors.*;

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * коэффициент колёсика мыши
     */
    private static final float WHEEL_SENSITIVE = 0.001f;
    /**
     * Порядок разделителя сетки, т.е. раз в сколько отсечек
     * будет нарисована увеличенная
     */
    private static final int DELIMITER_ORDER = 10;
    /**
     * Список точек в пересечении
     */

    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
    /**
     * Очистить задачу
     */
    public void clear() {
        circles.clear();
        rays.clear();
        solved = false;
    }
    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }
    /**
     * Решить задачу
     */
    public void solve() {
        // перебираем пары точек
        for (int i = 0; i < circles.size(); i++) {
            for (int j = i + 1; j < rays.size(); j++) {
                // TODO
            }
        }
        solved = true;
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;

    }
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            Заданы два множества: "широких лучей" и окружностей.
            Требуется построить такую пару "широкий луч"-окружность, 
            что фигура, находящаяся внутри "широкого луча" и окружности, 
            имеет максимальную площадь.""";

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * список окржностей
     */
    @Getter
    private final ArrayList<Circle> circles;

    /**
     * список лучей
     */
    @Getter
    private final ArrayList<Ray> rays;

    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;
    /**
     * Последняя СК окна
     */
    private CoordinateSystem2i lastWindowCS;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param circles массив окружностей
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("circles") ArrayList<Circle> circles,
            @JsonProperty("rays") ArrayList<Ray> rays
    ) {
        this.ownCS = ownCS;
        this.circles = circles;
         this.rays = rays;
    }

   /**    * Рисование




    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        // если левая кнопка мыши, добавляем луч
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            // TODO добавляем луч
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            // TODO добавляем окружность
        }
    }

    /**
     * Добавить случайные объекты
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomObjects(int cnt) {
        // создаем вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos1 = addGrid.getRandomCoords();
            Vector2i gridPos2 = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos1 = ownCS.getCoords(gridPos1, addGrid);
            Vector2d pos2 = ownCS.getCoords(gridPos2, addGrid);
            // сработает примерно в половине случаев
            addRay(pos1, pos2);
        }

        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos1 = addGrid.getRandomCoords();
            Vector2i gridPos2 = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos1 = ownCS.getCoords(gridPos1, addGrid);
            Vector2d pos2 = ownCS.getCoords(gridPos2, addGrid);
            // сработает примерно в половине случаев
            addCircle(pos1, pos2);
        }
    }

    /**
     * Рисование сетки
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        // сохраняем область рисования
        canvas.save();
        // получаем ширину штриха(т.е. по факту толщину линии)
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        // создаём перо соответствующей толщины
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            // перебираем все целочисленные отсчёты нашей СК по оси X
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                // рисуем вертикальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            // перебираем все целочисленные отсчёты нашей СК по оси Y
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                // рисуем горизонтальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        // восстанавливаем область рисования
        canvas.restore();
    }
    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Ray ray : rays) {
                paint.setColor(RAY_COLOR);

                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos1 = windowCS.getCoords(ray.getP1().x, ray.getP1().y, ownCS);
                // рисуем точку
                canvas.drawRect(Rect.makeXYWH(windowPos1.x - POINT_SIZE, windowPos1.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);

                Vector2i windowPos2 = windowCS.getCoords(ray.getP2().x, ray.getP2().y, ownCS);
                // рисуем точку
                canvas.drawRect(Rect.makeXYWH(windowPos2.x - POINT_SIZE, windowPos2.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }

            for (Circle circle : circles) {
                paint.setColor(CIRCLE_COLOR);

                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowCenter = windowCS.getCoords(circle.getCenter().x, circle.getCenter().y, ownCS);
                Vector2i windowPos=windowCS.getCoords(circle.getCenter().x+ circle.getRadius(), circle.getCenter().y, ownCS);
                float windowLength = (float)Vector2i.subtract(windowCenter, windowPos).length();

                // рисуем окружность
                canvas.drawCircle(windowCenter.x, windowCenter.y, windowLength, paint);
            }
        }
        canvas.restore();
    }
    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
        // рисуем координатную сетку
        renderGrid(canvas, lastWindowCS);
        // рисуем задачу
        renderTask(canvas, windowCS);
    }
    /**
     * Масштабирование области просмотра задачи
     *
     * @param delta  прокрутка колеса
     * @param center центр масштабирования
     */
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        // получаем координаты центра масштабирования в СК задачи
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        // выполняем масштабирование
        ownCS.scale(1 + delta * WHEEL_SENSITIVE, realCenter);
    }
    /**
     * Получить положение курсора мыши в СК задачи
     *
     * @param x        координата X курсора
     * @param y        координата Y курсора
     * @param windowCS СК окна
     * @return вещественный вектор положения в СК задачи
     */
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }
    /**
     * Рисование курсора мыши
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param pos      положение курсора мыши
     */
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            // сохраняем область рисования
            canvas.save();
            // рисуем перекрестие
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            // смещаемся немного для красивого вывода текста
            canvas.translate(pos.x + 3, pos.y - 5);
            // положение курсора в пространстве задачи
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            // выводим координаты
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            // восстанавливаем область рисования
            canvas.restore();
        }
    }


    public void addRay(Vector2d p1, Vector2d p2) {
        solved = false;
        Ray ray = new Ray(p1, p2);
        rays.add(ray);
        PanelLog.info("луч " + ray + " добавлена");
    }

    public void addCircle(Vector2d center, Vector2d point) {
        solved = false;
        Circle circle = new Circle(center, point);
        circles.add(circle);
        PanelLog.info("окружность " + circle + " добавлена");
    }
}
