/*
 * Christos Gogos
 * University of Ioannina 
 * May - 2020
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Polygon extends JFrame {

	public final static int WIDTH = 700;
	public final static int HEIGHT = 700;
	public final static int V = 5; // # of initial vertices (5 for pentagon)
        public static int thread_counts;
	private Vector<Point> polygon_vertices = new Vector<Point>();

	public static void main(String[] args) {
            if (args.length == 1){
                thread_counts = Integer.parseInt(args[0]);
                System.out.println(+thread_counts+" threads to use!");
            }
            
            SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
                    new Polygon("Polygon");
		}
            });
	}

	public Polygon(String title) {
		super(title);
		setSize(WIDTH, HEIGHT);
                setResizable(false);
		setLayout(new BorderLayout()); //Δεν αφήνει κενά ανάμεσα στα components
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); //Κεντράρει το παράθυρο
		setVisible(true);             
		generateRegularPolygon(Math.PI / 6); // PI/6 RADS = 30 DEGREES (first vertex at 30 DEGREES)
	}
        
	@Override
	public void paint(Graphics g) { //Καλείται όποτε κρηθεί απαραίτητο
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		for (Point p : polygon_vertices) {
			int x = (int) p.x;
			int y = (int) p.y;
			g2d.fillOval(x, y, 10, 10);
			g2d.drawString(String.format("(%d,%d)", x, y), x, y);
		}
	}

	private void generateRegularPolygon(double start_angle) {
		double x, y, radius = HEIGHT / 2 - 50;
		for (int i = 0; i < V; i++) {
			x = radius * Math.cos(start_angle + i * 2 * Math.PI / V);
			y = radius * Math.sin(start_angle + i * 2 * Math.PI / V);
			x += WIDTH / 2;
			y += HEIGHT / 2;
			System.out.printf("Vertex (%d): x=%.2f y=%.2f\n", i, x, y);
			polygon_vertices.add(new Point(x, y));
		}
                    
            calc();
                
	}
  
        public void calc() {
            ExecutorService executor = Executors.newFixedThreadPool(thread_counts);
                executor.submit(() -> {
                    Point p1,p2;
                    int x1=0,y1=0;
                    for (int i=0; i<V; i++) {
                            if (i==0)
                                p1 = polygon_vertices.get(0);
                            else
                                p1=new Point(x1,y1);
                            p2 = polygon_vertices.get((i+1) % 5);
                            x1 = (int)(p2.x + p1.x) / 2;
                            y1= (int)(p2.y + p1.y) / 2;
                            polygon_vertices.add(new Point(x1,y1));
                    }
                });
        }
        
	class Point {
		public double x;
		public double y;

		public Point(double _x, double _y) {
			x = _x;
			y = _y;
		}
	}
}
