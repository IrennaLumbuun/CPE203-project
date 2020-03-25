import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler {
    private PriorityQueue<Event> eventQueue;
    private Map<Entity, List<Event>> pendingEvents;
    private double timeScale;

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public void scheduleEvent(Entity entity, Action action, long afterPeriod) {
        long time = System.currentTimeMillis() +
                (long) (afterPeriod * timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

    public void unscheduleAllEvents(Entity entity) {
        List<Event> pending = this.pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                this.eventQueue.remove(event);
            }
        }
    }

    public void removePendingEvent(Event event) {
        List<Event> pending = this.pendingEvents.get(event.entity);

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void executeAction(Action action) {
        if (action instanceof ActivityAction) {
            action.execute(this);
        } else if (action instanceof AnimationAction) {
            action.execute(this);
        }
    }

    public void updateOnTime(long time) {
        while (!getEventQueue().isEmpty() &&
                getEventQueue().peek().time < time) {
            Event next = getEventQueue().poll();

            removePendingEvent(next);

            executeAction(next.getAction());
        }
    }
}
