package net.sf.anathema.hero.equipment.sheet.rendering.possessions;

import net.sf.anathema.framework.environment.Resources;
import net.sf.anathema.hero.sheet.pdf.content.BasicContent;
import net.sf.anathema.hero.sheet.pdf.encoder.EncoderIds;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.ContentEncoder;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.GlobalEncoderFactory;

@SuppressWarnings("UnusedDeclaration")
public class PossessionsEncoderFactory extends GlobalEncoderFactory {

  public PossessionsEncoderFactory() {
    super(EncoderIds.POSSESSIONS);
  }

  @Override
  public ContentEncoder create(Resources resources, BasicContent content) {
    return new PossessionsEncoder();
  }
}
