package bzh.terrevirtuelle.navisu.weather.impl;

import bzh.terrevirtuelle.navisu.app.drivers.driver.Driver;
import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.geoview.GeoViewServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layers.LayersManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.layertree.LayerTreeServices;
import bzh.terrevirtuelle.navisu.gazetteer.GazetteerComponentServices;
import bzh.terrevirtuelle.navisu.weather.WeatherComponent;
import bzh.terrevirtuelle.navisu.weather.WeatherComponentServices;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.DarkSkyComponentController;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.DataPoint;

import java.util.List;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;

import java.util.logging.Logger;

/**
 * User: serge Date: 23/11/2013
 */
public class WeatherComponentImpl
        implements WeatherComponent, WeatherComponentServices,
        InstrumentDriver, ComponentState {

    @UsedService
    GeoViewServices geoViewServices;
    @UsedService
    LayersManagerServices layersManagerServices;
    @UsedService
    LayerTreeServices layerTreeServices;
    @UsedService
    GuiAgentServices guiAgentServices;
    @UsedService
    GazetteerComponentServices gazetteerComponentServices;

    protected static final Logger LOGGER = Logger.getLogger(WeatherComponentImpl.class.getName());
    protected static int LAYER_INDEX = 0;
    protected Driver driver;
    protected DarkSkyComponentController weatherController;
    protected String category;
    private static final String NAME = "Weather";
    private static final String TYPE_0 = "DarkSky";
    private List<DataPoint> dataPoints;

    @Override
    public void componentInitiated() {

    }

    @Override
    public boolean canOpen(String type) {
        boolean canOpen = false;
        if (!type.equals(TYPE_0)) {
        } else {
            canOpen = true;
        }
        return canOpen;
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }

    @Override
    public InstrumentDriver getDriver() {
        return this;
    }

    @Override
    public void on(String... category) {
        weatherController = new DarkSkyComponentController(gazetteerComponentServices, guiAgentServices);
        //   if (weatherController.update() == true) {
        //      dataPoints = weatherController.getForecast();
        //  }

        guiAgentServices.getRoot().getChildren().add(weatherController.getDarkSkyController()); //Par defaut le widget n'est pas visible Ctrl-A
        weatherController.getDarkSkyController().setVisible(true);
    }

    @Override
    public boolean update() {
        if (weatherController != null) {
            return weatherController.update();
        } else {
            return false;
        }
    }

    @Override
    public List<DataPoint> getForecast() {
        if (weatherController != null) {
            return weatherController.getForecast();
        } else {
            return null;
        }
    }
}