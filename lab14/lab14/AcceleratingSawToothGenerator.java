package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.factor = factor;
        this.period = period;
        state = 0;
    }

    public double next() {
        state = state + 1;
        return normalize(state % period);
    }

    /**
     * converts values between 0 and period - 1 to values between -1.0 and 1.0.
     */
    private double normalize(int x) {

        double value =  (double) (x * 2 - (period - 1))/ (period - 1);
        if (x == 0) {
            period = (int) (period * factor);
            state = 0;
        }
        return value;
    }
}
