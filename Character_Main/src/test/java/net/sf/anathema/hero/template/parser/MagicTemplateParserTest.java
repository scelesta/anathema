package net.sf.anathema.hero.template.parser;

import net.sf.anathema.hero.dummy.DummyCharmUtilities;
import net.sf.anathema.character.main.magic.spells.CircleType;
import net.sf.anathema.character.main.xml.magic.GenericMagicTemplate;
import net.sf.anathema.character.main.xml.magic.GenericMagicTemplateParser;
import net.sf.anathema.hero.dummy.template.DummyXmlTemplateRegistry;
import net.sf.anathema.lib.xml.DocumentUtilities;
import org.apache.commons.lang3.ArrayUtils;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MagicTemplateParserTest {

  String xml = "<magicTemplate>" + "<freePicksPredicate defaultResponse=\"true\"/>" + "<charmTemplate charmType=\"None\">" +
               "<martialArts level=\"Mortal\"/></charmTemplate>" + "<spellTemplate maximumSorceryCircle=\"None\" maximumNecromancyCircle=\"None\"/>" +
               "</magicTemplate>";
  private GenericMagicTemplateParser parser;

  @Before
  public void setUp() throws Exception {
    DummyXmlTemplateRegistry<GenericMagicTemplate> templateRegistry = new DummyXmlTemplateRegistry<>();
    parser = new GenericMagicTemplateParser(templateRegistry, null);
  }

  @Test
  public void testDefaultFreePicksPredicate() throws Exception {
    Element templateElement = DocumentUtilities.read(xml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertTrue(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("TestCharm", "Group")));
  }

  @Test
  public void testFalseFreePicksPredicate() throws Exception {
    String customXml = "<magicTemplate>" + "<freePicksPredicate defaultResponse=\"false\"/>" + "<charmTemplate charmType=\"None\">" +
                       "<martialArts level=\"Mortal\"/></charmTemplate>" +
                       "<spellTemplate maximumSorceryCircle=\"None\" maximumNecromancyCircle=\"None\"/>" + "</magicTemplate>";
    Element templateElement = DocumentUtilities.read(customXml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertFalse(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("TestCharm", "Group")));
  }

  @Test
  public void testIdExceptionInFreePicksPredicate() throws Exception {
    String customXml = "<magicTemplate>" + "<freePicksPredicate defaultResponse=\"false\"><idException id=\"ExpectedId\"/></freePicksPredicate>" +
                       "<charmTemplate charmType=\"None\">" + "<martialArts level=\"Mortal\"/></charmTemplate>" +
                       "<spellTemplate maximumSorceryCircle=\"None\" maximumNecromancyCircle=\"None\"/>" + "</magicTemplate>";
    Element templateElement = DocumentUtilities.read(customXml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertFalse(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("BadId", "Group")));
    assertTrue(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("ExpectedId", "Group")));
  }

  @Test
  public void testGroupExceptionInFreePicksPredicate() throws Exception {
    String customXml =
            "<magicTemplate>" + "<freePicksPredicate defaultResponse=\"false\"><groupException id=\"ExpectedGroup\"/></freePicksPredicate>" +
            "<charmTemplate charmType=\"None\">" + "<martialArts level=\"Mortal\"/></charmTemplate>" +
            "<spellTemplate maximumSorceryCircle=\"None\" maximumNecromancyCircle=\"None\"/>" + "</magicTemplate>";
    Element templateElement = DocumentUtilities.read(customXml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertFalse(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("ExpectedId", "UnexpectedGroup")));
    assertTrue(template.canBuyFromFreePicks(DummyCharmUtilities.createCharm("ExpectedId", "ExpectedGroup")));
  }

  @Test
  public void testMortalSpellTemplate() throws Exception {
    Element templateElement = DocumentUtilities.read(xml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertFalse(template.getSpellMagic().canLearnSpellMagic());
  }

  @Test
  public void testParsesMaximumNecromancyCircle() throws Exception {
    String celestialXml = "<magicTemplate>" +
                          "<spellTemplate maximumSorceryCircle=\"Solar\" maximumNecromancyCircle=\"Labyrinth\"/>" + "</magicTemplate>";
    Element templateElement = DocumentUtilities.read(celestialXml).getRootElement();
    GenericMagicTemplate template = parser.parseTemplate(templateElement);
    assertTrue(ArrayUtils.contains(template.getSpellMagic().getNecromancyCircles(), CircleType.Shadowlands));
    assertTrue(ArrayUtils.contains(template.getSpellMagic().getNecromancyCircles(), CircleType.Labyrinth));
    assertFalse(ArrayUtils.contains(template.getSpellMagic().getNecromancyCircles(), CircleType.Void));
  }
}