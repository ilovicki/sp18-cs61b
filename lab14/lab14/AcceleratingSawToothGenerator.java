package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double factor;
    private int state;
    private int sum;
    public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        sum = 0;
        this.period = period;
        this.factor = factor;
    }

    @Override
    public double next() {
        state += 1;
        if (state - sum == period) {
            sum += period;
            period *= factor;
        }
        return normalize((state - sum) % period);

    }

    private double normalize(int x) {
        return 2 * x / (double)(period - 1) - 1;
    }

}
