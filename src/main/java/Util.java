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

    /**
     * 计算化为“x/y”的字符串的值
     * @param valueStr “x/y”
     * @return 得到的结果
     */
    public static double calculateFraction(String valueStr){
        String[] parts = valueStr.split("/");
        double numerator = Double.parseDouble(parts[0]);
        double denominator = Double.parseDouble(parts[1]);
        if (denominator == 0) {
            System.out.println("除数不能为零");
            return -1;
        } else {
            return numerator / denominator;
        }
    }

    /**
     * 计算输入的两个分数的值
     * @return 以"x/y"的字符串形式返回
     */
    public static Figure calculateFractionExpression(Figure f1, Figure f2, Operator operator){
        // 先做1/0和负数的检测
        double v1 = f1.getValue();
        double v2 = f2.getValue();
        double v = 0;
        int op = operator.getValue();
        if(op == MainClass.DEVICE){
            if(v2 == 0) return null;
        } else if(op == MainClass.MINUS) {
            if (v1 - v2 < 0) return null;
        }
        // 将字符串拆分为分子和分母
        String[] partsF1 = f1.getForm().split("/");
        int numeratorF1 = Integer.parseInt(partsF1[0]);
        int denominatorF1 = Integer.parseInt(partsF1[1]);
        String[] partsF2 = f1.getForm().split("/");
        int numeratorF2 = Integer.parseInt(partsF2[0]);
        int denominatorF2 = Integer.parseInt(partsF2[1]);
        int numerator = 0;
        int denominator = 0;

        switch (op){
            case MainClass.PLUS:
                v = v1 + v2;
                // 分数相加
                numerator = numeratorF1*denominatorF2 + denominatorF1*numeratorF2;
                denominator = denominatorF1 * denominatorF2;
                break;
            case MainClass.MINUS:
                v = v1 - v2;
                // 分数相减
                numerator = numeratorF1*denominatorF2 - denominatorF1*numeratorF2;
                denominator = denominatorF1 * denominatorF2;
                break;
            case MainClass.MULTIPLE:
                v = v1 * v2;
                // 分数相乘
                numerator = numeratorF1 * numeratorF2;
                denominator = denominatorF1 * denominatorF2;
                break;
            case MainClass.DEVICE:
                v = v1 / v2;
                // 分数相除，其实就是乘以倒数
                numerator = numeratorF1 * denominatorF2;
                denominator = denominatorF1 * numeratorF2;
                break;
        }
        return new Figure(v, numerator + "/" + denominator);
    }
}
