import java.util.Random;

public class Util {
    public static boolean checkArgument(int arg){
        return arg > 0;
    }

    public static String generateFraction(int range){
        Random random = new Random();
        // 创建分子
        int numerator;
        // 创建分母
        int denominator;
        // 校对是否小于区间
        do{
            numerator = random.nextInt(range);
            denominator = random.nextInt(range - 1) + 1; // 避免0
        }while (numerator / denominator >= range);
        // 返回字符型
        return numerator == 0? "0" : numerator + "/" + denominator;
    }
}
