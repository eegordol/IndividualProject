package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

public class Circle {
    /**
     * центр окружности
     */
    private Vector2d center;
    /**
     * радиус
     */
    private double radius;
    /**
     * Конструктор окружности по центру и радиусу
     *
     * @param center центр окружности
     * @param radius радиус окружности
     */
    @JsonCreator
    public Circle(@JsonProperty("center") Vector2d center, @JsonProperty("radius") double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Конструктор окружности по двум точкам
     * @param center центр окружности
     * @param point координаты какой-то точки на окружности
     */
    public Circle(Vector2d center, Vector2d point) {
        this.center = center;
        this.radius = Vector2d.subtract(center,point).length();
    }

    /**
     * Получение центра
     * @return координаты центра окружности
     */

    public Vector2d getCenter() {
        return center;
    }
    /**
     * Получение радиуса
     * @return радиус окружности
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Проверяет, что точка принадлежит кругу
     * @param p тестируемая точка
     * @return true если точка принадлежит кругу
     */
    public boolean contains(Vector2d p) {
        double dist = Vector2d.subtract(p, getCenter()).length();
        return dist < getRadius() + 1e-10;
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Circle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
