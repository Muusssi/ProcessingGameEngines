package trpge;

public abstract class Event implements Comparable<Event> {

    public int time, interval;

    public Event (int time) {
      this.time = time;
      TRPGE.events.add(this);
    }

    public Event (int time, int interval) {
      this.time = time;
      this.interval = interval;
      TRPGE.events.add(this);
    }

    public void trigger() {
      this.effect();
      if (this.interval > 0) {
        this.time += this.interval;
        TRPGE.events.add(this);
      }
    }

    public abstract void effect();

    @Override
    public int compareTo(Event other) {
      return Integer.compare(this.time, other.time);
    }
}
