package bzh.terrevirtuelle.navisu.app.guiagent.layertree.impl;

import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTree;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.core.view.display.Display;
import bzh.terrevirtuelle.navisu.core.view.geoview.GeoView;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.GeoLayer;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.LayerManager;
import bzh.terrevirtuelle.navisu.core.view.geoview.layer.worldwind.impl.WorldWindGeoLayer;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.GeoWorldWindView;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.widgets.slider.SliderController;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;

/**
 * NaVisu
 *
 * @author Jordan Modified Serge Morvan
 * @date 23/02/2014 17:46
 */
public class LayerCheckTreeImpl
        implements LayerTree, LayerTreeServices, ComponentState {

    @UsedService
    GeoViewServices geoViewServices;
    @UsedService
    GuiAgentServices guiAgentServices;
    private TreeView<GeoLayer> treeView;
    protected GeoWorldWindView geoView;
    protected LayerManager<Layer> layerManager;
    private CheckBoxTreeItem<GeoLayer> rootItem0;
    private List<CheckBoxTreeItem<GeoLayer>> rootItems;
    private GeoLayer layer;
    private String value;
    private List<String> groupNames;
    protected WorldWindow wwd = GeoWorldWindViewImpl.getWW();
    protected SliderController opacitySliderController;

    @Override
    public void componentInitiated() {
        this.rootItem0 = new CheckBoxTreeItem<>();
        this.treeView = new TreeView<>(rootItem0);
        this.rootItems = new ArrayList<>();
        this.groupNames = new ArrayList<>();
        this.geoView = GeoView.factory.newWorldWindGeo3DView();
        this.layerManager = this.geoView.getLayerManager();
        this.rootItem0.setExpanded(true);
        this.treeView.setShowRoot(false);
        this.treeView.setCellFactory(CheckBoxTreeCell.<GeoLayer>forTreeView((TreeItem<GeoLayer> objectTreeItem) -> new SimpleBooleanProperty(objectTreeItem.getValue().isVisible()), new StringConverter<TreeItem<GeoLayer>>() {
            @Override
            public String toString(TreeItem<GeoLayer> geoLayerTreeItem) {
                return geoLayerTreeItem.getValue().getName();
            }

            @Override
            public TreeItem<GeoLayer> fromString(String s) {
                return null;
            }
        }));
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        rootItems.add(createNode(rootItem0, "On-earth layers", "16x16-icon-earth.png"));
        rootItems.add(createNode(rootItem0, "On-screen layers", "boussole.png"));
        CheckBoxTreeItem<GeoLayer> charts = createNode(rootItem0, "Charts", "charts-16x16.png");
        rootItems.add(charts);
        CheckBoxTreeItem<GeoLayer> buildings = createNode(rootItem0, "Buildings", "charts-16x16.png");
        rootItems.add(buildings);
        CheckBoxTreeItem<GeoLayer> raster = createNode(charts, "Raster charts", null);
        rootItems.add(raster);
        CheckBoxTreeItem<GeoLayer> tmp = createNode(raster, "BSB/KAP charts", null);
        rootItems.add(tmp);
        tmp = createNode(raster, "GeoTiff charts", null);
        rootItems.add(tmp);
        CheckBoxTreeItem<GeoLayer> vector = createNode(charts, "Vector charts", null);
        rootItems.add(vector);
        tmp = createNode(vector, "S57 charts", null);
        rootItems.add(tmp);
        tmp = createNode(vector, "S57 catalog", null);
        rootItems.add(tmp);
        CheckBoxTreeItem<GeoLayer> devices = createNode(rootItem0, "Navigation", null);
        rootItems.add(devices);
        CheckBoxTreeItem<GeoLayer> meteo = createNode(rootItem0, "Meteo", null);
        rootItems.add(meteo);
        CheckBoxTreeItem<GeoLayer> currents = createNode(rootItem0, "Currents", null);
        rootItems.add(currents);
        CheckBoxTreeItem<GeoLayer> bathymetry = createNode(rootItem0, "Bathymetry", null);
        rootItems.add(bathymetry);
        tmp = createNode(bathymetry, "Bathymetry catalog", null);
        rootItems.add(tmp);
        tmp = createNode(bathymetry, "Bathymetry data", null);
        rootItems.add(tmp);
        CheckBoxTreeItem<GeoLayer> kml = createNode(rootItem0, "KML files", null);
        rootItems.add(kml);
        CheckBoxTreeItem<GeoLayer> gpx = createNode(rootItem0, "GPX files", null);
        rootItems.add(gpx);
        CheckBoxTreeItem<GeoLayer> shp = createNode(rootItem0, "Shape files", null);
        rootItems.add(shp);
        CheckBoxTreeItem<GeoLayer> wms = createNode(rootItem0, "WMS Layers", null);
        rootItems.add(wms);
        CheckBoxTreeItem<GeoLayer> targetDisplay = createNode(rootItem0, "Target display", null);
        rootItems.add(targetDisplay);
        tmp = createNode(targetDisplay, "Target", null);
        rootItems.add(tmp);
        tmp = createNode(targetDisplay, "Path", null);
        rootItems.add(tmp);
        tmp = createNode(targetDisplay, "GpsTrack", null);
        rootItems.add(tmp);
        tmp = createNode(targetDisplay, "Watch polygons", null);
        rootItems.add(tmp);
        tmp = createNode(targetDisplay, "Rules", null);
        rootItems.add(tmp);
        tmp = createNode(targetDisplay, "Buffer", null);
        rootItems.add(tmp);
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {

    }

    @Override
    public void createGroup(String groupName, GeoLayer<?>... layers) {
        groupNames.add(groupName);
        for (GeoLayer geoLayer : layers) {
            CheckBoxTreeItem<GeoLayer> treeItem;
            treeItem = new CheckBoxTreeItem<>(geoLayer);
            treeItem.setSelected(geoLayer.isVisible());
            treeItem.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) -> {
                geoLayer.setVisible(newValue);

                if (geoLayer instanceof Layer) {
                    ((Layer) geoLayer.getDisplayLayer()).setEnabled(newValue);
                }
            });
            search(groupName).getChildren().add(treeItem);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CheckBoxTreeItem<GeoLayer> createNode(CheckBoxTreeItem<GeoLayer> root, String nodeName, String iconName) {
        GeoLayer geoLayer = new WorldWindGeoLayer(new RenderableLayer());
        geoLayer.setName(nodeName);
        CheckBoxTreeItem<GeoLayer> treeItem = null;
        if (iconName != null) {
            treeItem = new CheckBoxTreeItem<>(geoLayer, getIcon(iconName));
        } else {
            treeItem = new CheckBoxTreeItem<>(geoLayer);
        }
        treeItem.setSelected(geoLayer.isVisible());

        treeItem.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) -> {
            geoLayer.setVisible(newValue);
            if (geoLayer instanceof Layer) {
                ((Layer) geoLayer.getDisplayLayer()).setEnabled(newValue);
            }
        });

        layerManager.createGroup(nodeName, geoLayer);
        root.getChildren().add(treeItem);
        return treeItem;
    }

    @Override
    public void addGeoLayer(String groupName, GeoLayer<?>... layers) {
        createGroup(groupName, layers);
    }

    @Override
    public Display<Node> getDisplayService() {
        return Display.factory.newDisplayNode(treeView);
    }

    private Node getIcon(String name) {
        return new ImageView(new Image(LayerCheckTreeImpl.class.getResourceAsStream(name)));
    }

    @Override
    public CheckBoxTreeItem<GeoLayer> search(String name) {
        CheckBoxTreeItem<GeoLayer> result = null;
        for (CheckBoxTreeItem<GeoLayer> cb : rootItems) {
            if (cb.getValue().getName().equals(name)) {
                result = cb;
            }
        }
        return result;
    }

    @Override
    public CheckBoxTreeItem<GeoLayer> search(GeoLayer layer) {
        CheckBoxTreeItem<GeoLayer> result = null;
        for (CheckBoxTreeItem<GeoLayer> cb : rootItems) {
            if (cb.getValue() == layer) {
                result = cb;
            }
        }
        return result;
    }

    @Override
    public List<CheckBoxTreeItem<GeoLayer>> getCheckBoxTreeItems() {
        return rootItems;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeoLayer getGeoLayer(String name) {

        CheckBoxTreeItem<GeoLayer> i = (CheckBoxTreeItem) this.search(name);
        List<TreeItem<GeoLayer>> childrens = i.getChildren();
        childrens.stream().forEach((t) -> {
            if (t.getValue().getName().trim().equals(name)) {
                layer = t.getValue();
            }
        });
        return layer;
    }

    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Text) {
           Layer l = getLayer(((Text) node).getText());
            if (l != null) {
                Platform.runLater(() -> {
                  //  createOpacityController(l);
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Layer getLayer(String layerName) {
        LayerList ls = wwd.getModel().getLayers();
        Layer l = null;
        for (Layer ll : ls) {
            if (ll.getName().equals(layerName)) {
                l =  ll;
            }
        }
        return l;
    }

    public RenderableLayer getLayer(List<String> groupNames, String layerName) {
        List<String> groups = groupNames;

        boolean layerExist = false;
        RenderableLayer layer = null;
        for (String groupName : groupNames) {
            List<GeoLayer<Layer>> layers = geoViewServices.getLayerManager().getGroup(groupName);

            for (GeoLayer<Layer> g : layers) {
                if (g.getName().contains(layerName)) {
                    layer = (RenderableLayer) g.getDisplayLayer();
                    layerExist = true;
                }
            }
        }
        return layer;

    }

    @Override
    public List<String> getGroupNames() {
        return groupNames;
    }

    @Override
    public void removeLayer(String... names) {
        List<TreeItem<GeoLayer>> l = new ArrayList<>();
        for (String name : names) {
            rootItems.stream().map((cTI) -> cTI.getChildren()).forEachOrdered((childrens) -> {
                childrens.stream().forEach((t) -> {
                    if (t.getValue().getName().trim().equals(name)) {
                        layer = t.getValue();
                        l.add(t);
                        t.setExpanded(false);
                    }
                });
                childrens.removeAll(l);
            });
        }

    }

    protected void createOpacityController(Layer layer) {
        if (opacitySliderController != null) {
            if (guiAgentServices.getRoot().getChildren().contains(opacitySliderController)) {
                guiAgentServices.getScene().removeEventFilter(KeyEvent.KEY_RELEASED, opacitySliderController);
                guiAgentServices.getRoot().getChildren().remove(opacitySliderController);
            }
        }
        opacitySliderController = new SliderController();
        opacitySliderController.setTranslateY(150.0);
        opacitySliderController.setTranslateX(480.0);
        opacitySliderController.setRotate(-90);
        opacitySliderController.setVisible(true);
        opacitySliderController.getSlider().setMin(0.0);
        opacitySliderController.getSlider().setMax(1.0);
        opacitySliderController.getSlider().setValue(1.0);
        // opacitySliderController.getSlider().setTooltip(new Tooltip(name + " opacity"));

        opacitySliderController.getSlider().valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            java.awt.EventQueue.invokeLater(() -> {
             //   System.out.println("layer : " + layer.getName());
             //   layer.setOpacity(opacitySliderController.getSlider().getValue());
                System.out.println(layer.getOpacity());
                
                layer.setOpacity(0);
                wwd.redrawNow();
            });
        });
        guiAgentServices.getScene().addEventFilter(KeyEvent.KEY_RELEASED, opacitySliderController);
        guiAgentServices.getRoot().getChildren().add(opacitySliderController);
    }
}
