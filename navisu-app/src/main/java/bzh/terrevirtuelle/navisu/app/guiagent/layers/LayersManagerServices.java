/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.app.guiagent.layers;

import gov.nasa.worldwind.layers.RenderableLayer;
import org.capcaval.c3.component.ComponentService;

/**
 * NaVisu
 *
 * @date 26 août 2015
 * @author Serge Morvan
 */
public interface LayersManagerServices
        extends ComponentService {

    RenderableLayer getLayer(String groupName, String layerName);

    RenderableLayer getLayer(String layerName);

    RenderableLayer getlayer(String groupName, String layerName, String onLayerName);

    boolean removeLayer(RenderableLayer... layer);
}
