public class Planet{

	public double xxPos; // Its current x position
	public double yyPos; // Its current y position
	public double xxVel; // Its current velocity in the x direction
	public double yyVel; // Its current velocity in the y direction
	public double mass; // Its mass
	public String imgFileName; // the name of the file that corresponds to the image that depicts the planet 

	private static final double G = 6.67e-11; // gravitational constant

	public Planet(double xP, double yP, double xV,
              double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	// initialize an identical Planet object
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;

	}

	public double calcDistance(Planet p){

		double x1 = this.xxPos, y1 = this.yyPos;
		double x2 = p.xxPos, y2 = p.yyPos;

		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 -y2) * (y1 - y2));
	}

	public double calcForceExertedBy(Planet p){
		double m1 = this.mass, m2 = p.mass;
		double distance = calcDistance(p);

		return G * m1 * m2 / (distance * distance);
	}

	public double calcForceExertedByX(Planet p){
		double dx = p.xxPos - this.xxPos;
		double total_force = calcForceExertedBy(p);
		double distance = calcDistance(p);
		return total_force * dx / distance;
	}

	public double calcForceExertedByY(Planet p){
		double dy = p.yyPos - this.yyPos;
		double total_force = calcForceExertedBy(p);
		double distance = calcDistance(p);
		return total_force * dy / distance;
	}

	public double calcNetForceExertedByX(Planet[] all_planet){
		double net_force_x = 0;
		for (Planet p: all_planet){
			if (this.equals(p)){
				continue;
			}
			
			net_force_x += calcForceExertedByX(p);
		}
		return net_force_x;
	}

	public double calcNetForceExertedByY(Planet[] all_planet){
		double net_force_y = 0;
		for (Planet p: all_planet){
			if (this.equals(p)){
				continue;
			}
			
			net_force_y += calcForceExertedByY(p);
		}
		return net_force_y;
	}

	public void update(double dt, double fX, double fY){
		double ax = fX / mass, ay = fY / mass;

		// update velocity
		xxVel = xxVel + dt * ax;
		yyVel = yyVel + dt * ay;

		// update position
		xxPos = xxPos + dt * xxVel;
		yyPos = yyPos + dt * yyVel;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}

	

}