public class NBody{

	public static double readRadius(String file_name){
		In in = new In(file_name);

		int num = in.readInt();
		double radius = in.readDouble();

		return radius;
	} 

	public static Planet[] readPlanets(String file_name){
		
		In in = new In(file_name);
		int num = in.readInt();
		double radius = in.readDouble();

		Planet[] return_array = new Planet[num];

		for(int i = 0; i < num; i++){

			double xP = in.readDouble();
			double yP = in.readDouble();
			double xV = in.readDouble();
			double yV = in.readDouble();
			double m = in.readDouble();
			String img = in.readString();

			return_array[i] = new Planet(xP, yP, xV, yV, m, img);
		}
		return return_array;
	}

	public static void main(String[] args){

		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];

		double r = readRadius(filename);
		Planet[] planets = readPlanets(filename);

		StdDraw.setScale(-r, r);
		StdDraw.enableDoubleBuffering();

		/* Clears the drawing window. */
		StdDraw.clear();

		int num = planets.length;

		double time = 0;
		double[] xForces = new double[num], yForces = new double[num];
		

		while(time < T){

			for(int i = 0; i < num; i++){
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);	
			}

			for(int i = 0; i < num; i++){
				planets[i].update(dt, xForces[i], yForces[i]);
			}

			/* set background */
			StdDraw.picture(0, 0, "images/starfield.jpg");

			for(Planet p: planets){
				p.draw();
			}

			StdDraw.show();
			StdDraw.pause(10);

			time += dt;
		}

		/* Printing the Universe */
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
		}

		
		
	}

}