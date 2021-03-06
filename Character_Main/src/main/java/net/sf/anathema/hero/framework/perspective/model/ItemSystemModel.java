package net.sf.anathema.hero.framework.perspective.model;

import net.sf.anathema.character.main.framework.item.Item;

import java.util.Collection;

public interface ItemSystemModel extends ItemSelectionModel {

  Collection<CharacterItemModel> collectAllExistingCharacters();

  Item loadItem(CharacterIdentifier identifier);

  void setCurrentCharacter(CharacterIdentifier identifier);
}