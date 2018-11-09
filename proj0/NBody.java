public class NBody {
    public static double readRadius(String fileName){
        In in  = new In(fileName);
        int N = in.readInt();
        double R = in.readDouble();
        return R;
    }
    public static Planet[] readPlanets(String fileName){
        In in  = new In(fileName);
        int N = in.readInt();
        Planet[] planets = new Planet[N];
        double R = in.readDouble();
        for(int i = 0; i < N; i += 1){
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            planets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return planets;
    }
    public static void main(String[] args){
        if(args.length < 3){
            System.out.println("please supply T, dt and filename as 3 command line arguments");
        }
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        int N = planets.length;
        double radius = readRadius(filename);
        StdDraw.setScale(- radius, radius);
        StdDraw.picture(0, 0, "images/starfield.jpg");
        for(int i = 0; i < N; i++){
            planets[i].draw();
        }
        StdDraw.enableDoubleBuffering();
        double time = 0;
        while(time < T){
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for(int i = 0; i < N; i++){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0; i < N; i++){
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for(int i = 0; i < N; i++){
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }
        StdOut.printf("%d\n", N);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < N; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
