package Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    protected List<Observer> attached;

    protected Subject() {
        attached = new ArrayList<>();
    }

    public void attach(Observer obs) {
        if (!attached.contains(obs)) {
            attached.add(obs);
        }
    }

    public void detach(Observer obs) {
        if (attached.contains(obs)) {
            attached.remove(obs);
        }
    }

    protected void notifyObservers() {
        for (Observer o : attached) {
            o.update(this);
        }
    }

    protected void notifyObservers(Object data) {
        for (Observer o : attached) {
            o.update(this, data);
        }
    }

}
