package net.sf.anathema.lib.gui.widgets;

import net.sf.anathema.lib.UnselectingComboBoxModel;
import net.sf.anathema.lib.control.ObjectValueListener;
import org.jmock.example.announcer.Announcer;

import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ChangeableJComboBox<V> implements IChangeableJComboBox<V> {

  private final JComboBox comboBox;
  private final Announcer<ObjectValueListener> control = Announcer.to(ObjectValueListener.class);

  public ChangeableJComboBox(V[] objects) {
    this(new UnselectingComboBoxModel(objects));
  }

  private ChangeableJComboBox(UnselectingComboBoxModel model) {
    this.comboBox = new ColoredJComboBox(model);
    this.comboBox.setEditable(false);
    setSelectedObject(null);
    comboBox.addItemListener(new ItemListener() {
      @Override
      @SuppressWarnings("unchecked")
      public void itemStateChanged(ItemEvent e) {
        control.announce().valueChanged(e.getItem());
      }
    });
  }

  @Override
  public JComboBox getComponent() {
    return comboBox;
  }

  @Override
  public void setSelectedObject(Object object) {
    comboBox.setSelectedItem(object);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setObjects(V[] objects) {
    Object selectedItem = comboBox.getSelectedItem();
    UnselectingComboBoxModel model = (UnselectingComboBoxModel) comboBox.getModel();
    model.removeAllElements();
    for (Object object : objects) {
      model.addElement(object);
    }
    setSelectedObject(selectedItem);
    if (comboBox.getSelectedItem() == null) {
      control.announce().valueChanged(null);
    }
  }

  @Override
  public void addObjectSelectionChangedListener(ObjectValueListener<V> listener) {
    control.addListener(listener);
  }

  @Override
  public void removeObjectSelectionChangeListener(ObjectValueListener<V> listener) {
    control.addListener(listener);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getSelectedObject() {
    return (V) comboBox.getSelectedItem();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setRenderer(ListCellRenderer renderer) {
    comboBox.setRenderer(renderer);
  }

  @Override
  public Dimension getPreferredSize() {
    return comboBox.getPreferredSize();
  }

  @Override
  public void setPreferredSize(Dimension preferredSize) {
    comboBox.setPreferredSize(preferredSize);
  }
}