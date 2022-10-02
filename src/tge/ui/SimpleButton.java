package tge.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import tge.TGE;

public class SimpleButton extends Button {

  public Method press_action_method;
  public Method release_action_method;

  public SimpleButton(String text, int x, int y, String method_name) {
    super(text, x, y);
    set_action_method(method_name);
  }

  public void set_action_method(String method_name) {
    press_action_method = get_method(method_name);
  }

  public void set_release_action_method(String method_name) {
    release_action_method = get_method(method_name);
  }

  protected Method get_method(String method_name) {
    if (method_name == null || method_name.equals("")) {
      return null;
    }
    try {
      return TGE.papplet().getClass().getMethod(method_name);
    }
    catch (SecurityException e) {
      System.err.println("Error: SecurityException when initializing button: " + this.text);
    }
    catch (NoSuchMethodException e) {
      System.err.println("Error: NoSuchMethodException when initializing button: " + this.text);
    }
    return null;
  }

  protected void invoke(Method method) {
    try {
      method.invoke(TGE.papplet());
    }
    catch (IllegalAccessException e) {
      System.err.println("IllegalAccessException when invoking button action: " + this.text);
    }
    catch (InvocationTargetException e) {
      System.err.println("InvocationTargetException when invoking button action: " + this.text);
    }
    catch (IllegalArgumentException e) {
      System.err.println("IllegalArgumentException when invoking button action: " + this.text);
    }
  }

  @Override
  public void action() {
    if (this.press_action_method == null) {
      System.err.println("Button action missing: " + this.text);
      return;
    }
    this.invoke(this.press_action_method);
  }

  @Override
  public void release_action() {
    if (this.release_action_method == null) {
      System.err.println("Button action missing: " + this.text);
      return;
    }
    this.invoke(this.release_action_method);
  }

}
