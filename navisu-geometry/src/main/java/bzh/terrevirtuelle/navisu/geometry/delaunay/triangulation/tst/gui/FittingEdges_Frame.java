package bzh.terrevirtuelle.navisu.geometry.delaunay.triangulation.tst.gui;

import bzh.terrevirtuelle.navisu.geometry.delaunay.triangulation.Contour;
import bzh.terrevirtuelle.navisu.geometry.delaunay.triangulation.Delaunay_Triangulation;
import bzh.terrevirtuelle.navisu.geometry.delaunay.triangulation.Point_dt;
import bzh.terrevirtuelle.navisu.geometry.delaunay.triangulation.util.DT;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Creates Set of point randomly
 */
public class FittingEdges_Frame {

    public static void main(String[] args) {
        int count = 50;
        final Random rnd = new Random(948731674);

        final Delaunay_Triangulation dt = new Delaunay_Triangulation();

        int xMax = 700;
        int yMax = 700;
        int zMax = 40;
        for (int i = 0; i < count; i++) {
            double x = rnd.nextInt(xMax);
            double y = rnd.nextInt(yMax);
            double z = rnd.nextInt(zMax);
            dt.insertPoint(new Point_dt(x, y, z));
        }

        final ArrayList<Contour> contours = DT.contours(dt, 5);
        class MyPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {

                for (Contour c : contours) {
                    System.out.println(c.getArea());
                    int gg = rnd.nextInt(250);
                    int r = rnd.nextInt(250);
                    int B = rnd.nextInt(250);
                    g.setColor(new Color(r, gg, B));

                    ListIterator<Point_dt> itr = c.points.listIterator();
                    Point_dt a = itr.next();//throws Exception if points size = 0

                    while (a != null && itr.hasNext()) {
                        Point_dt b = itr.next();
                        g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
                        g.fillOval((int) a.x, (int) a.y, 4, 4);
                        g.fillOval((int) b.x, (int) b.y, 4, 4);
                        a = b;
                    }
                }
            }
        }

        JFrame frame = new JFrame("FittingEdges_Frame");

        JPanel viewer = new MyPanel();

        frame.getContentPane().

                add(viewer);

        frame.setDefaultCloseOperation(3);
        frame.setSize(new

                        Dimension(900, 780)

        );
        frame.setVisible(true);
    }


}
