package net.sf.anathema.framework.repository.tree;

import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.environment.Environment;
import net.sf.anathema.framework.messaging.IMessaging;
import net.sf.anathema.framework.module.ItemTypePresentationFactory;
import net.sf.anathema.framework.presenter.view.IItemTypeViewProperties;
import net.sf.anathema.initialization.ForItemType;
import net.sf.anathema.initialization.ItemTypeCollection;
import net.sf.anathema.lib.gui.dialog.userdialog.page.AbstractDialogPage;
import net.sf.anathema.lib.message.BasicMessage;
import net.sf.anathema.lib.message.IBasicMessage;

import javax.swing.JComponent;
import java.util.Collection;

public class RepositoryBrowserDialogPage extends AbstractDialogPage {

  private final Environment environment;
  private final IApplicationModel model;

  public RepositoryBrowserDialogPage(Environment environment, IApplicationModel model) {
    super(environment.getString("AnathemaCore.Tools.RepositoryView.DialogMessage"));
    this.environment = environment;
    this.model = model;
  }

  @Override
  public JComponent createContent() {
    RepositoryTreeView treeView = new RepositoryTreeView();
    ItemTypeCollection itemTypeCollection = new ItemTypeCollection(environment);
    ItemTypePropertiesMap propertiesMap = registerItemTypePresentations(itemTypeCollection);
    ObjectUiTreeCellRenderer renderer = new ObjectUiTreeCellRenderer(new ItemTypeTreeUi(environment, propertiesMap));
    RepositoryTreeModel repositoryTreeModel = new RepositoryTreeModel(model.getRepository(), itemTypeCollection);
    new RepositoryTreePresenter(environment, repositoryTreeModel, treeView, renderer, "AnathemaCore.Tools.RepositoryView.TreeRoot")
            .initPresentation();
    IMessaging messaging = model.getMessaging();
    AmountMessaging fileCountMessaging = new AmountMessaging(messaging, environment);
    new RepositoryItemDeletionPresenter(environment, repositoryTreeModel, treeView, fileCountMessaging).initPresentation();
    new RepositoryItemExportPresenter(environment, repositoryTreeModel, treeView, fileCountMessaging).initPresentation();
    new RepositoryItemImportPresenter(environment, repositoryTreeModel, treeView, fileCountMessaging).initPresentation();
    new RepositoryItemDuplicationPresenter(environment, repositoryTreeModel, treeView, messaging).initPresentation();
    new RepositoryMessagingPresenter(repositoryTreeModel, messaging).initPresentation();
    return treeView.getComponent();
  }


  private ItemTypePropertiesMap registerItemTypePresentations(ItemTypeCollection itemTypeCollection) {
    Collection<ItemTypePresentationFactory> presentationFactories = environment.instantiateAllImplementers(ItemTypePresentationFactory.class);
    ItemTypePropertiesMap map = new ItemTypePropertiesMap();
    for (ItemTypePresentationFactory factory : presentationFactories) {
      IItemTypeViewProperties properties = factory.createItemTypeCreationProperties(model, environment);
      String itemType = factory.getClass().getAnnotation(ForItemType.class).value();
      map.put(itemTypeCollection.getById(itemType), properties);
    }
    return map;
  }

  @Override
  public IBasicMessage createCurrentMessage() {
    return new BasicMessage(environment.getString("AnathemaCore.Tools.RepositoryView.DialogMessage"));
  }

  @Override
  public String getTitle() {
    return environment.getString("AnathemaCore.Tools.RepositoryView.DialogTitle");
  }
}