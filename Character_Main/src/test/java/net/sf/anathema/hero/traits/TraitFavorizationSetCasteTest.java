package net.sf.anathema.hero.traits;

import net.sf.anathema.character.main.library.trait.Trait;
import net.sf.anathema.character.main.library.trait.favorable.FavorableState;
import net.sf.anathema.character.main.library.trait.favorable.FriendlyIncrementChecker;
import net.sf.anathema.character.main.library.trait.favorable.IncrementChecker;
import net.sf.anathema.character.main.library.trait.favorable.TraitFavorization;
import net.sf.anathema.character.main.traits.types.AbilityType;
import net.sf.anathema.hero.concept.CasteType;
import net.sf.anathema.hero.dummy.DummyCasteType;
import net.sf.anathema.hero.dummy.DummyHero;
import net.sf.anathema.hero.dummy.trait.DummyTrait;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TraitFavorizationSetCasteTest {

  private Trait archeryTrait = new DummyTrait(AbilityType.Archery);

  private TraitFavorization createFriendlyTraitFavorization() {
    return createTraitFavorization(new FriendlyIncrementChecker());
  }

  private TraitFavorization createTraitFavorization(IncrementChecker incrementChecker) {
    DummyHero dummyHero = new DummyHero();
    return new TraitFavorization(dummyHero, new CasteType[]{new DummyCasteType()}, incrementChecker, archeryTrait, false);
  }

  @Test
  public void testCasteAfterSettingCasteOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
  }

  @Test
  public void testCasteAfterSettingCasteOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    assertSame(FavorableState.Default, favorization.getFavorableState());
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
  }

  @Test
  public void testCasteAfterSettingCasteOnFavored() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setFavored(true);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
  }

  @Test
  public void testDefaultAfterSettingNotCasteOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.setCaste(false);
    assertSame(FavorableState.Default, favorization.getFavorableState());
  }

  @Test
  public void testDefaultAfterSettingNotCasteOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    assertSame(FavorableState.Default, favorization.getFavorableState());
    favorization.setCaste(false);
    assertSame(FavorableState.Default, favorization.getFavorableState());
  }

  @Test
  public void testFavoredAfterSettingNotCasteOnFavored() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setFavored(true);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
    favorization.setCaste(false);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
  }
}