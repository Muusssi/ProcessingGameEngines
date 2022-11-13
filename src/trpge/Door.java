package trpge;

public class Door extends WorldObject {

  public Door linked_door;

  public Door(String name) {
    super(name, "Door", "Door", null);
  }

  public void link(Door other_door) {
    if (other_door != null) {
      this.linked_door = other_door;
      other_door.linked_door = this;
    }
  }

  @Override
  public void default_interaction(GameCharacter character) {
    if (this.linked_door != null) {
      this.linked_door.location.put(character, this.linked_door.x, this.linked_door.y);
      if (character == TRPGE.active_player) {
        TRPGE.focus_camera();
      }
    }
  }

}
