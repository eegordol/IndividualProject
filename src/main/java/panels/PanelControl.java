package panels;

import app.ObjectType;
import controls.Button;
import app.Task;
import controls.Input;
import controls.InputFactory;
import controls.Label;
import controls.MultiLineLabel;
import dialogs.PanelInfo;
import io.github.humbleui.jwm.*;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.ArrayList;
import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.*;

/**
 * Панель управления
 */
public class PanelControl extends GridPanel {
    /**
     * Текст задания
     */
    MultiLineLabel task;
    /**
     * Заголовки
     */
    public List<Label> labels;
    /**
     * Поля ввода
     */
    public List<Input> inputs;
    /**
     * Кнопки
     */
    public List<Button> buttons;
    /**
     * Кнопка "решить"
     */
    private final Button solve;

    /**
     *
     * @param objectType - тип добавляемого объекта
     * @param x1Field - поле ввода X координата перовой точки
     * @param y1Field - поле ввода Y координата перовой точки
     * @param x2Field- поле ввода X координата второй точки
     * @param y2Field -поле ввода Y координата второй точки
     */
    private void AddObject(ObjectType objectType, Input x1Field, Input y1Field, Input x2Field, Input y2Field)
    {
        if (!x1Field.hasValidDoubleValue()) {
            PanelLog.warning("X1 координата введена неверно");
        } else if (!y1Field.hasValidDoubleValue())
            PanelLog.warning("Y1 координата введена неверно");
        else if (!x2Field.hasValidDoubleValue()) {
            PanelLog.warning("X2 координата введена неверно");
        } else if (!y2Field.hasValidDoubleValue())
            PanelLog.warning("Y2 координата введена неверно");
        else { // если числа введены верно
            var p1 = new Vector2d(x1Field.doubleValue(), y1Field.doubleValue());
            var p2 = new Vector2d(x2Field.doubleValue(), y2Field.doubleValue());

            switch (objectType){
                case RAY -> PanelRendering.task.addRay(p1, p2);
                case CIRCLE -> PanelRendering.task.addCircle(p1, p2);
            }
        }

    }

    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        // создаём списки
        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();

        int localGridWidth = 6;
        int localGridHeight = 8;

        // задание
        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 0, 6, 2, Task.TASK_TEXT,
                false, true);
        // добавление объектов вручную
        Label x1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 2, 1, 1, "X1", true, true);
        labels.add(x1Label);
        Input x1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                localGridWidth, localGridHeight, 1, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(x1Field);
        Label y1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 2, 1, 1, "Y1", true, true);
        labels.add(y1Label);
        Input y1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                localGridWidth, localGridHeight, 4, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(y1Field);

        Label x2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 3, 1, 1, "X2", true, true);
        labels.add(x2Label);
        Input x2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                localGridWidth, localGridHeight, 1, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(x2Field);
        Label y2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 3, 1, 1, "Y2", true, true);
        labels.add(y2Label);
        Input y2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                localGridWidth, localGridHeight, 4, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(y2Field);

        Button addToFirstSet = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 4, 3, 1, "Добавить луч",
                true, true);
        addToFirstSet.setOnClick(() -> { AddObject(ObjectType.RAY, x1Field, y1Field, x2Field, y2Field); });
        buttons.add(addToFirstSet);

        Button addToSecondSet = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 4, 3, 1, "Добавить окружность",
                true, true);
        addToSecondSet.setOnClick(() -> { AddObject(ObjectType.CIRCLE, x1Field, y1Field, x2Field, y2Field); });
        buttons.add(addToSecondSet);
        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 5, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                localGridWidth, localGridHeight, 1, 5, 2, 1, "5", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addObjects = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 5, 3, 1, "Добавить\nслучайные объекты",
                true, true);
        addObjects.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во точек указано неверно");
            } else
                PanelRendering.task.addRandomObjects(cntField.intValue());
        });
        buttons.add(addObjects);
        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 6, 3, 1, "Загрузить",
                true, true);
        load.setOnClick(() -> {
            PanelRendering.load();
            cancelTask();
        });
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 6, 3, 1, "Сохранить",
                true, true);
        save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 0, 7, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                localGridWidth, localGridHeight, 3, 7, 3, 1, "Решить",
                true, true);
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
            PanelRendering.task.solve();
                // TODO
                PanelInfo.show("Нажмите Esc, чтобы вернуться");
            solve.text = "Сбросить";
        } else {
            cancelTask();
        }
            window.requestFrame();
        });
        buttons.add(solve);
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызываем обработчик предка
        super.accept(e);
        // событие движения мыши
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);

            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }
            // событие нажатия мыши
        } else if (e instanceof EventMouseButton) {
            if (!lastInside)
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);

            // пробуем кликнуть по всем кнопкам
            for (Button button : buttons) {
                button.click(relPos);
            }

            // перебираем поля ввода
            for (Input input : inputs) {
                // если клик внутри этого поля
                if (input.contains(relPos)) {
                    // переводим фокус на это поле ввода
                    input.setFocus();
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода текста
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода клавиш
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        // выводим текст задачи
        task.paint(canvas, windowCS);

        // выводим кнопки
        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }
    }
    /**
     * Сброс решения задачи
     */
    private void cancelTask() {
        PanelRendering.task.cancel();
        // Задаём новый текст кнопке решения
        solve.text = "Решить";
    }
}
