package bzh.terrevirtuelle.navisu.geometry.isoline.triangulation.tst.gui;

import bzh.terrevirtuelle.navisu.geometry.isoline.triangulation.Contour;
import bzh.terrevirtuelle.navisu.geometry.isoline.triangulation.Delaunay_Triangulation;
import bzh.terrevirtuelle.navisu.geometry.isoline.triangulation.Point_dt;
import bzh.terrevirtuelle.navisu.geometry.isoline.triangulation.util.DT;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class FillContours_Frame {

    public static void main(String[] args) {
        int count = 50;
        final Random rnd = new Random(948731674);

        Delaunay_Triangulation dt = new Delaunay_Triangulation();

        int xMax = 700;
        int yMax = 700;
        final int zMax = 40;
        for (int i = 0; i < count; i++) {
            double x = rnd.nextInt(xMax);
            double y = rnd.nextInt(yMax);
            double z = rnd.nextInt(zMax);

            Point_dt point_dt = new Point_dt(x, y, z);
            dt.insertPoint(point_dt);
        }

        final ArrayList<Contour> contours = DT.contours(dt, 5);



        class MyPanel extends JPanel {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Contour c : contours) {
                    int gg = 0;
                    int r = 255 - c.level * (255 / zMax);
                    int B = rnd.nextInt(50);
                    g.setColor(new Color(r, gg, B));
                    ArrayList<Point_dt> point_dts = new ArrayList<Point_dt>(c.points);
                    UtGraphics.fillPolygon(g, point_dts);
                }
            }
        }


        JFrame frame = new JFrame("FillContours_Frame");

        MyPanel viewer = new MyPanel();

        frame.getContentPane().
                add(viewer, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(3);
        frame.setSize(new

                        Dimension(900, 780)

        );
        frame.setVisible(true);
    }

}