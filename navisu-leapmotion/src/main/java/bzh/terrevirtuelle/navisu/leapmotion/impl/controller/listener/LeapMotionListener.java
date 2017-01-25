/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.leapmotion.impl.controller.listener;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;

/**
 *
 * @author serge
 * @date Nov 16, 2016
 */
public class LeapMotionListener
        extends Listener {
    private int comptSwipe =0;
    private int comptCircle =0;
    private int comptKeyTap =0;
    private int comptScreenTap =0;

    /**
     * Called remotely when the Leap Motion controller is successfully connected
     *
     * @param controller
     */
    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    /**
     * Called remotely at each frame that the controller receives
     *
     * @param controller
     */
    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        if (frame.gestures().count() > 0) {
            //System.out.println("Nombre de gesture : " + frame.gestures().count());
            for(Gesture gesture : frame.gestures())
            {
                switch (gesture.type()) {
                    case TYPE_CIRCLE:
                        //Handle circle gestures
                        comptCircle ++;
                        if (comptCircle == 40)
                        {
                            comptSwipe =0;
                            comptCircle =0;
                            comptKeyTap =0;
                            comptScreenTap =0;
                            System.out.println("circle");
                        }
                        break;
                    case TYPE_KEY_TAP:
                        //Handle key tap gestures
                        comptKeyTap ++;
                        if (comptKeyTap == 2)
                        {
                            comptSwipe =0;
                            comptCircle =0;
                            comptKeyTap =0;
                            comptScreenTap =0;
                            System.out.println("key tap");
                        }
                        break;
                    case TYPE_SCREEN_TAP:
                        //Handle screen tap gestures
                        comptScreenTap ++;
                        if (comptScreenTap == 2)
                        {
                            comptSwipe =0;
                            comptCircle =0;
                            comptKeyTap =0;
                            comptScreenTap =0;
                            System.out.println("screen tap");
                        }
                        break;
                    case TYPE_SWIPE:
                        //Handle swipe gestures
                        comptSwipe ++;
                        if (comptSwipe == 130)
                        {
                            //System.out.println("Translation sur X : " + gesture.hands().get(0).translation(controller.frame(1)).getX());
                            comptSwipe =0;
                            comptCircle =0;
                            comptKeyTap =0;
                            comptScreenTap =0;
                            if (gesture.hands().get(0).translation(controller.frame(1)).getX() <0)
                            {
                                System.out.println("swipe left");
                            }
                            else
                            {
                                System.out.println("swipe right");
                            }
                        }
                        break;
                    default:
                        //Handle unrecognized gestures
                        System.out.println("Geste non reconnu");
                        break;
                }
            }
        }
               

        }
}
