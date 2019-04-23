package dczh.Util;

import java.util.Random;

public class MathUtil {

    /**
     * 生成max到min范围的浮点数
     * */
    public static double nextDouble(final double min, final double max) {
        return min + ((max - min) * new Random().nextFloat());
    }
    public static boolean isBetweenNormalValue(final double value) {
        if (value > 40 || value<-15){
            return false;
        }
        return true;
    }
}
