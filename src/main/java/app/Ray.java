package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

/**
 * Широкий луч - это прямоугольник с одной из сторон, находящейся в бесконечности.
 * Он задается двумя точками, являющимися вершинами одной из сторон прямоугольника. Направление "луча" получается поворотом на
 * 90 градусов
 *  вектора, проведенного из первой точки во вторую.
 */
public class Ray {

    /**
     * первая точка
     */
    private Vector2d p1;
    /**
     * вторая точка
     */
    private Vector2d p2;

    /**
     *
     * @param p1 положение 1 точки
     * @param p2 положение 2 точки 
     */
    @JsonCreator
    public Ray(@JsonProperty("p1") Vector2d p1, @JsonProperty("p2") Vector2d p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * получение 1 точки
     * @return 1 точка
     */
    public Vector2d getP1() {
        return p1;
    }

    /**
     * получение 2 точки
     * @return 2 точка
     */
    public Vector2d getP2() {
        return p2;
    }

    /**
     * Проверяет, что точка принадлежит лучу
     * @param p тестируемая точка
     * @return true если точка принадлежит лучу
     */
    public boolean contains(Vector2d p) {
        Vector2d ort = Vector2d.subtract(p2,p1).rotate(Math.PI/2); // направление луча
        Vector2d p3 = Vector2d.sum(p2, ort); // точка на второй прямой
        Vector2d p0 = Vector2d.sum(p1, ort); // точка на первой прямой

        return Misc.halfPlaneContainsPoint(p0, p1, p) &&
               Misc.halfPlaneContainsPoint(p1, p2, p) &&
               Misc.halfPlaneContainsPoint(p2, p3, p);
    }

    public boolean intersects(Circle circle){
        Vector2d ort = Vector2d.subtract(p2,p1).rotate(Math.PI/2); // направление луча
        Vector2d p3 = Vector2d.sum(p2, ort); // точка на второй прямой
        Vector2d p0 = Vector2d.sum(p1, ort); // точка на первой прямой

        double testDistance = -circle.getRadius() - 1e-10;

        return Misc.signedDistanceToLine(p0, p1, circle.getCenter()) > testDistance &&
               Misc.signedDistanceToLine(p1, p2, circle.getCenter()) > testDistance &&
               Misc.signedDistanceToLine(p2, p3, circle.getCenter()) > testDistance;
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }
}
