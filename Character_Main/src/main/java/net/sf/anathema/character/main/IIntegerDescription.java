package net.sf.anathema.character.main;

import net.sf.anathema.lib.control.ChangeListener;

public interface IIntegerDescription {

  int getValue();

  void setValue(int value);

  void addChangeListener(ChangeListener listener);
}