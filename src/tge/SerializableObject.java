package tge;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import processing.data.JSONObject;

public abstract class SerializableObject {

    public SerializableObject() {}


    public JSONObject save_object() {
        JSONObject json = new JSONObject();
        get_constructor(getClass().getCanonicalName());
        json.put("canonicalName", this.getClass().getCanonicalName());
        return json;
    }

    public void update(JSONObject json) {}

    @SuppressWarnings("unchecked")
    public static Constructor<SerializableObject> get_constructor(String canonical_name) {
        Constructor<SerializableObject> constructor = null;
        Class<SerializableObject> entityClass = null;
        try {
            entityClass = (Class<SerializableObject>)Class.forName(canonical_name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        @SuppressWarnings("rawtypes")
        Class[] cArg = {JSONObject.class};
        try {
            constructor = entityClass.getDeclaredConstructor(cArg);
        } catch (NoSuchMethodException | SecurityException e) {
            System.err.println("Error: Unsafe save file! Corresponding loading constructor for a class is missing:");
            System.err.println("  public " + entityClass.getSimpleName() + "(JSONObject json) {");
            System.err.println("    super(json);");
            System.err.println("  }");
        }
        return constructor;
    }

    public static SerializableObject load_instance(JSONObject json) {
        SerializableObject instance = null;
        Constructor<SerializableObject> constructor = get_constructor(json.getString("canonicalName"));
        try {
            instance = constructor.newInstance(json);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            System.err.println("Error loading " + json.getString("canonicalName"));
            System.exit(1);
        }
        return instance;
    }

}
