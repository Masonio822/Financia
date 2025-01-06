package gui.observer;

import java.util.ArrayList;

public class Notifier {
    private static final ArrayList<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer o) {
        observers.add(o);
    }

    public static void update() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public static void removeObserver(Observer o) {
        observers.remove(o);
    }
}
