/*
Action: ideally what our various entities might do in our virtual world
 */

public interface Action {
    void execute(EventScheduler scheduler);
}
