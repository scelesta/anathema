package net.sf.anathema.hero.attributes.sheet.content;

import net.sf.anathema.character.main.template.ITraitTemplateCollection;
import net.sf.anathema.character.main.template.abilities.GroupedTraitType;
import net.sf.anathema.character.main.traits.ITraitTemplate;
import net.sf.anathema.character.main.traits.TraitType;
import net.sf.anathema.character.main.traits.types.AttributeType;
import net.sf.anathema.hero.attributes.model.AttributesModelFetcher;
import net.sf.anathema.hero.model.Hero;
import net.sf.anathema.lib.util.Identifier;
import net.sf.anathema.lib.util.SimpleIdentifier;

import java.util.ArrayList;
import java.util.List;

public class AttributesPrintModel implements AttributesList {

  private Hero hero;

  public AttributesPrintModel(Hero hero) {
    this.hero = hero;
  }

  public int getCurrentValue(Identifier traitId) {
    AttributeType type = AttributeType.valueOf(traitId.getId());
    return AttributesModelFetcher.fetch(hero).getTrait(type).getCurrentValue();
  }

  public int getTraitMaximum() {
    TraitType traitType = getGroupedAttributeTypes()[0].getTraitType();
    ITraitTemplate template = getTraitTemplateCollection().getTraitTemplate(traitType);
    return template.getLimitation().getAbsoluteLimit(hero);
  }

  @Override
  public void iterate(AttributesIterator iterator) {
    for (Identifier groupId : getGroupedIds()) {
      iterator.nextGroup(groupId);
      iterateGroup(iterator, groupId);
    }
  }

  private void iterateGroup(AttributesIterator iterator, Identifier groupId) {
    for (TraitType traitType : getTraitTypes(groupId)) {
      iterateTrait(iterator, traitType);
    }
  }

  private void iterateTrait(AttributesIterator iterator, TraitType traitType) {
     iterator.nextTrait(traitType);
  }

  private ITraitTemplateCollection getTraitTemplateCollection() {
    return hero.getTemplate().getTraitTemplateCollection();
  }

  private List<TraitType> getTraitTypes(Identifier groupId) {
    List<TraitType> attributes = new ArrayList<>();
    for (GroupedTraitType groupedType : getGroupedAttributeTypes()) {
      if (groupedType.getGroupId().equals(groupId.getId())) {
        attributes.add(groupedType.getTraitType());
      }
    }
    return attributes;
  }

  private List<Identifier> getGroupedIds() {
    List<Identifier> groupIdList = new ArrayList<>();
    for (GroupedTraitType type : getGroupedAttributeTypes()) {
      SimpleIdentifier groupId = new SimpleIdentifier(type.getGroupId());
      if (!groupIdList.contains(groupId)) {
        groupIdList.add(groupId);
      }
    }
    return groupIdList;
  }

  private GroupedTraitType[] getGroupedAttributeTypes() {
    return hero.getTemplate().getAttributeGroups();
  }
}
