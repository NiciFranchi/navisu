/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bzh.terrevirtuelle.navisu.architecture.impl.controller.handler;

import bzh.terrevirtuelle.navisu.domain.architecture.Component;

/**
 *
 * @author serge
 * @date Nov 3, 2017
 */
public class PrintComponentHandler extends Handler{

    @Override
    public void doIt(Component component) {
        System.out.println(component);
    }

}