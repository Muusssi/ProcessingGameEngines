package trpge;

import java.util.ArrayList;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class Room extends TRPGEObject {

  public String name;
  public String part_of;
  public int width = 1000;
  public int height = 1000;

  public ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
  public ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();


  public Room(String name) {
    this.name = name;
    TRPGE.rooms.add(this);
  }

  @Override
  public JSONObject save_object() {
    JSONObject save_object = super.save_object();
    save_object.put("type", "room");
    save_object.put("name", this.name);
    save_object.put("width", this.width);
    save_object.put("height", this.height);

    JSONArray object_array = new JSONArray();
    for (WorldObject object : objects) {
      object_array.append(object.save_object());
    }
    save_object.put("objects", objects);

    JSONArray character_array = new JSONArray();
    for (GameCharacter game_character : characters) {
      character_array.append(game_character.save_object());
    }
    save_object.put("characters", character_array);

    return save_object;
  }

  public void remove(WorldObject object) {
    objects.remove(object);
    object.location = null;
  }

  public void put(WorldObject object, int x, int y) {
    if (object.location != null) {
      object.location.remove(object);
    }
    objects.add(object);
    object.location = this;
    object.x = x;
    object.y = y;
  }

  public void remove(GameCharacter character) {
    characters.remove(character);
    character.location = null;
  }

  public void put(GameCharacter character, int x, int y) {
    if (character.location != null) {
      character.location.remove(character);
    }
    characters.add(character);
    character.location = this;
    character.x = x;
    character.y = y;
  }

  public void draw() {
    TRPGE.papplet().rect(0, 0, this.width, this.height);
    for (WorldObject object : objects) {
      object.draw();
    }
    for (GameCharacter character : characters) {
      character.draw();
    }
  }

  public void link_doors(int door_x, int door_y, Room other_room, int other_x, int other_y) {
    Door door1 = new Door("Door to " + other_room.name);
    this.put(door1, door_x, door_y);
    Door door2 = new Door("Door to " + this.name);
    other_room.put(door2, other_x, other_y);
    door1.link(door2);
  }

}
