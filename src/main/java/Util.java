import org.apache.commons.lang.math.Fraction;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {
    public static boolean checkArgument(int arg) {
        return arg <= 0;
    }

    public static String generateFraction(int range) {
        Random random = new Random();
        // 创建分子
        int numerator;
        // 创建分母
        int denominator;
        // 校对是否小于区间
        do {
            numerator = random.nextInt(range);
            denominator = random.nextInt(range - 1) + 1; // 避免0
        } while (numerator / denominator >= range);
        // 返回字符型
        return numerator == 0 ? "0/1" : numerator + "/" + denominator;
    }

    /**
     * 计算化为“x/y”的字符串的值
     *
     * @param valueStr “x/y”
     * @return 得到的结果
     */
    public static double calculateFraction(String valueStr) {
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
     *
     * @return 以"x/y"的字符串形式返回
     */
    public static Figure calculateFractionExpression(Figure f1, Figure f2, Operator operator) {
        // 先做1/0和负数的检测
        double v1 = f1.getValue();
        double v2 = f2.getValue();
        double v = 0;
        int op = operator.getValue();
        if (op == MainClass.DEVICE) {
            if (v2 == 0) return null;
        } else if (op == MainClass.MINUS) {
            if (v1 - v2 < 0) return null;
        }
        // 将字符串拆分为分子和分母
        String[] partsF1 = f1.getForm().split("/");
        int numeratorF1 = Integer.parseInt(partsF1[0]);
        int denominatorF1 = Integer.parseInt(partsF1[1]);
        String[] partsF2 = f2.getForm().split("/");
        int numeratorF2 = Integer.parseInt(partsF2[0]);
        int denominatorF2 = Integer.parseInt(partsF2[1]);
        int numerator = 0;
        int denominator = 0;

        switch (op) {
            case MainClass.PLUS:
                v = v1 + v2;
                // 分数相加
                numerator = numeratorF1 * denominatorF2 + denominatorF1 * numeratorF2;
                denominator = denominatorF1 * denominatorF2;
                break;
            case MainClass.MINUS:
                v = v1 - v2;
                // 分数相减
                numerator = numeratorF1 * denominatorF2 - denominatorF1 * numeratorF2;
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
                // 分数相除
                numerator = numeratorF1 * denominatorF2;
                denominator = denominatorF1 * numeratorF2;
                break;
        }
        return new Figure(v, numerator + "/" + denominator);
    }

    public static String simplifyFraction(String fraction) {
        // 使用正则表达式提取分子和分母
        Pattern pattern = Pattern.compile("(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(fraction);

        if (matcher.matches()) {
            int numerator = Integer.parseInt(matcher.group(1));
            int denominator = Integer.parseInt(matcher.group(2));

            // 如果相同直接返回"1"即可
            if (numerator == denominator) {
                return "1";
            }

            // 如果分母为0，直接返回零即可
            if (numerator == 0) {
                return "0";
            }

            // 如果分母为1，直接返回分子即可
            if (denominator == 1) {
                return numerator + "";
            }

            // 计算最大公约数
            int gcd = gcd(numerator, denominator);

            // 化简分数
            numerator /= gcd;
            denominator /= gcd;

            if (numerator / denominator >= 1 && numerator != denominator) {
                return fractionToMixedNumber(numerator + "/" + denominator);
            } else if (numerator / denominator == 0) {
                // 构造最简分数字符串
                return numerator + "/" + denominator;
            }
        }
        // 未发生改变会返回原始字符串
        return fraction;
    }

    // 计算最大公约数的辗转相除法
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static String fractionToMixedNumber(String fraction) {
        // 使用正则表达式提取分子和分母
        String[] parts = fraction.split("/");
        if (parts.length != 2) {
            return fraction; // 不是有效的分数格式，直接返回原始字符串
        }

        int numerator = Integer.parseInt(parts[0]);
        int denominator = Integer.parseInt(parts[1]);

        // 计算整数部分和余数
        int wholePart = numerator / denominator;
        int remainder = numerator % denominator;

        if (remainder == 0) {
            return String.valueOf(wholePart); // 没有余数，直接返回整数部分
        } else if (numerator == denominator) {
            return "1"; // 分子等于分母，等于整数1
        } else {
            return wholePart + "'" + remainder + "/" + denominator; // 带分数形式
        }
    }

    // 将表达式中包含的真分数转换为普通的分数形式
    public static String convertExpression(String expression) {
        StringBuilder result = new StringBuilder();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '/' || c == '\'') {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    result.append(convertFraction(number.toString()));
                    number.setLength(0);
                }
                result.append(c);
            }
        }

        if (number.length() > 0) {
            result.append(convertFraction(number.toString()));
        }

        return result.toString();
    }

    private static String convertFraction(String fraction) {
        if (fraction.contains("'")) {
            String[] parts = fraction.split("'");
            int integerPart = Integer.parseInt(parts[0]);
            int numerator = Integer.parseInt(parts[1].split("/")[0]);
            int denominator = Integer.parseInt(parts[1].split("/")[1]);
            numerator += integerPart * denominator;
            return numerator + "/" + denominator;
        } else {
            return fraction;
        }
    }

    public static void saveToFile(String fileName, ArrayList<String> strings) {
        try {
            // 创建文件写入对象
            FileWriter writer = new FileWriter(fileName);

            // 遍历链表并将数据写入文件
            for (String item : strings) {
                writer.write(item + "\n");
            }

            // 关闭文件写入对象
            writer.close();

            System.out.println("数据已成功写入文件 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAndCalculate(String exercisesFilePath, String answersFilePath, String gradeFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(exercisesFilePath))) {
            String line;
            ArrayList<Double> exercises = new ArrayList<>();
            ArrayList<Double> answers;
            ArrayList<Integer> correct = new ArrayList<>();
            ArrayList<Integer> wrong = new ArrayList<>();
            ArrayList<String> content = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String expression = line.substring(line.indexOf(".") + 1).trim();
                // 将真分数转换为普通分数格式
                String newExpression = convertExpression(expression);
                exercises.add(calculate(newExpression));
            }

            // 与答案文件做对比
            answers = readAnswerFile(answersFilePath);
            judgeCorrectAndWrong(exercises, answers, correct, wrong);

            // 生成成绩报告
            generateGradeReport(correct, wrong, content);

            // 保存成绩
            saveToFile(gradeFilePath, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double calculate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isDigit(c)) {
                int num = c - '0';
                while (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    num = num * 10 + (expression.charAt(i + 1) - '0');
                    i++;
                }
                if (i + 1 < expression.length() && expression.charAt(i + 1) == '/') {
                    i++;
                    int den = expression.charAt(i + 1) - '0';
                    while (i + 2 < expression.length() && Character.isDigit(expression.charAt(i + 2))) {
                        den = den * 10 + (expression.charAt(i + 2) - '0');
                        i++;
                    }
                    i++; // 跳过分母
                    numbers.push((double) num / den);
                } else {
                    numbers.push((double) num);
                }
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(c)) {
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.empty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }


    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '×' && op1 != '÷') || (op2 != '+' && op2 != '-');
    }

    private static double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '×':
                return a * b;
            case '÷':
                if (b == 0) {
                    throw new UnsupportedOperationException("除数不能为零");
                }
                return a / b;
        }
        return 0;
    }

    public static boolean judgeNotAllNumber(String s){
        // 使用正则表达式匹配数字以外的字符
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    // 读取答案文件并返回答案数组
    public static ArrayList<Double> readAnswerFile(String filePath){
        List<String> lines = new ArrayList<>();
        ArrayList<Double> answers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // 去除行首的数字和点号，以及空格
                line = line.replaceAll("^\\d+\\.\\s*", "");
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s :
                lines) {
            // 将真分数转换为普通分数格式
            String newExpression = convertExpression(s);
            // 进行计算
            answers.add(calculate(newExpression));
        }

        return answers;
    }

    // 判断答案的正确和错误
    public static void judgeCorrectAndWrong(ArrayList<Double> exercises, ArrayList<Double> answers,
                                            ArrayList<Integer> correct, ArrayList<Integer> wrong){
        if (exercises.size() != answers.size()) {
            throw new IllegalArgumentException("Input ArrayLists must have the same size.");
        }

        for (int i = 0; i < exercises.size(); i++) {
            double exerciseValue = exercises.get(i);
            double answerValue = answers.get(i);
            int index = i + 1;
            if (Math.abs(exerciseValue - answerValue) < 0.001) {
                correct.add(index);
            } else {
                wrong.add(index);
            }
        }
    }

    // 生成成绩报告
    public static void generateGradeReport(ArrayList<Integer> correct, ArrayList<Integer> wrong,
                                           ArrayList<String> content){
        content.add("Correct: " + correct.size() + " (" + correct.stream().map(Object::toString).
                collect(Collectors.joining(", ")) + ")");
        content.add("Wrong: " + wrong.size() + " (" + wrong.stream().map(Object::toString).
                collect(Collectors.joining(", ")) + ")");
    }

    // 判断是否出现步骤重复
    public static boolean judgeRepeatElement(ArrayList<String> newStep, ArrayList<String> passedStep){
        // 如果新step是旧step的子集，那么它们就算重复
        for (String expr1 : newStep) {
            boolean found = false;
            for (String expr2 : passedStep) {
                if (areExpressionsEquivalent(expr1, expr2)) {
                    found = true;
                    break;
                }
            }
            // 有一个步骤不重复就返回false
            if (!found) {
                return false;
            }
        }
        // 只有当步骤全部重复时才会返回true
        return true;
    }

    private static boolean areExpressionsEquivalent(String expr1, String expr2) {
        if (expr1.length() != expr2.length()) {
            return false;
        }

        // 使用正则表达式将表达式中的加号(+)、减号(-)、乘号(*)和除号(/)作为分隔符进行分割
        // 判断符号是否一致

        // 判断数值是否一致
        String[] parts1 = expr1.split("\\+|-|\\*|/");
        String[] parts2 = expr2.split("\\+|-|\\*|/");
        Arrays.sort(parts1);
        Arrays.sort(parts2);

        return Arrays.equals(parts1, parts2) && expr1.replaceAll("[0-9]", "").
                equals(expr2.replaceAll("[0-9]", ""));
    }

}
