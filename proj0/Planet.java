public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double G = 6.67e-11;
    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;

    }
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p){
        double dx = p.xxPos - xxPos;
        double dy = p.yyPos - yyPos;
        double squaredR = dx * dx + dy * dy;
        double r = Math.sqrt(squaredR);
        return r;
    }
    public double calcForceExertedBy(Planet p){
        double r = calcDistance(p);
        double f = G * mass * p.mass / (r * r);
        return f;
    }
    public double calcForceExertedByX(Planet p){
        double r = calcDistance(p);
        double f = calcForceExertedBy(p);
        double dx = p.xxPos - xxPos;
        double fx = f * dx / r;
        return fx;
    }
    public double calcForceExertedByY(Planet p){
        double r = calcDistance(p);
        double f = calcForceExertedBy(p);
        double dy = p.yyPos - yyPos;
        double fy = f * dy / r;
        return fy;
    }
    public boolean equals(Planet p){
        if (xxPos == p.xxPos && yyPos == p.yyPos){
            return true;
        }
        return false;
    }
    public double calcNetForceExertedByX(Planet[] P){
        double netX = 0.0;
        for(int i = 0; i < P.length; i += 1){
            if (equals(P[i])){
                continue;
            }
            double x = calcForceExertedByX(P[i]);
            netX += x;
        }
        return netX;
    }
    public double calcNetForceExertedByY(Planet[] P){
        double netY = 0.0;
        for(int i = 0; i < P.length; i += 1){
            if (equals(P[i])){
                continue;
            }
            double y = calcForceExertedByY(P[i]);
            netY += y;
        }
        return netY;
    }
    public void update(double dt, double fX, double fY){
        double aX = fX / mass;
        double aY = fY / mass;
        xxVel += dt * aX;
        yyVel += dt * aY;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
    }

}
