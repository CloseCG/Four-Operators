import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainClass {
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MULTIPLE = 3;
    public static final int DEVICE = 4;

    public static void main(String[] args) {
        // 接受命令行输入
        int num = 0;
        int range = 0;
        String exerciseFilePath = null;
        String answerFilePath = null;
        label:
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    // 此处如果收到了1.1，自动将其转型为1
                    num = Integer.parseInt(args[i]);
                    if (num <= 0){
                        ArgumentsException RException = new ArgumentsException("请保证-n后的值为自然数！");
                        RException.printStackTrace();//打印异常栈追踪信息
                        System.exit(1); // 退出程序
                    }
                    break;
                case "-r":
                    // 检测到 -r，获取后面的元素作为值
                    if (i + 1 < args.length) {
                        // 此处如果收到了1.1，自动将其转型为1
                        range = Integer.parseInt(args[i + 1]);
                        if (!Util.checkArgument(range)) {
                            // 表面输入的值不为自然数
                            ArgumentsException RException = new ArgumentsException("请保证-r后的值为自然数！");
                            RException.printStackTrace();//打印异常栈追踪信息
                            System.exit(1); // 退出程序
                        }
                    } else {
                        // 如果 -r 后没有元素，则创建异常对象
                        ArgumentsException RException = new ArgumentsException("请输入-r后的值！");
                        RException.printStackTrace();//打印异常栈追踪信息
                        System.exit(1); // 退出程序
                    }
                    break label;
                case "-e":
                    exerciseFilePath = args[i];
                    break;
                case "-a":
                    answerFilePath = args[i];
                    break;
            }
        }
        // 先判断是生成还是检测
        if (num != 0 && range != 0 && exerciseFilePath == null && answerFilePath == null){
            // 生成
            ArrayList<MathExpression> allExpression = generateAll(num, range);
            if(allExpression.size() == 0){
                System.out.println("生成表达式的过程中出错！");
                System.exit(1);
            }
            System.out.println("生成表达式成功！");

            // 写入文件


        } else if (num == 0 && range == 0 && exerciseFilePath != null && answerFilePath != null){
            // 检测
            System.out.println();
        } else {
            ArgumentsException RException = new ArgumentsException("输入有误！");
            RException.printStackTrace();//打印异常栈追踪信息
            System.exit(1); // 退出程序
        }
    }

    /**
     * 生成num个表达式
     * @param num 生成数量
     * @param range 每个值的大小范围，属于[0,range)
     * @return 返回每个表达式构成的数组
     */
    public static ArrayList<MathExpression> generateAll(int num, int range){
        ArrayList<MathExpression> allExpression = new ArrayList<>();
        ArrayList<ArrayList<MathExpression>> allExpressionStep = new ArrayList<>();
        while(num > 0){
            // 生成单个表达式
            MathExpression oneExpression;
            // 反复生成，直到不出现错误的表达式
            do{
                oneExpression = generateOne(range, allExpressionStep);
            }while (oneExpression == null);
            allExpression.add(oneExpression);
            num--;
        }
        return allExpression;
    }

    /**
     * 生成单个表达式
     * @param range 表达式值的上限
     * @param allExpressionStep 全部表达式
     * @return 生成的新表达式
     */
    public static MathExpression generateOne(int range, ArrayList<ArrayList<MathExpression>> allExpressionStep){
        // 随机产生运算符的数目，属于[1,3]
        Random random = new Random();
        int operatorNum = random.nextInt(3) + 1;
        ArrayList<Operator> operators = new ArrayList<>();
        for(int i = 1; i <= operatorNum; i++){
            Operator operator = new Operator(random.nextInt(4) + 1);
            operators.add(operator);
        }
        // 产生的数的数量为运算符+1
        int numberNum = operatorNum + 1;
        ArrayList<MathExpression> figuresExpression = new ArrayList<>();
        for(int i = 1; i <= numberNum; i++){
            // 创建一个随机数，封装到expression中去
            String valueStr = Util.generateFraction(range);
            // 计算生成数的值
            double value = Util.calculateFraction(valueStr);
            Figure figure = new Figure(value, valueStr);
            figuresExpression.add(new MathExpression(figure, valueStr));
        }
        // 得到生成多项式的初始优先级
        ArrayList<Integer> rawPriority = getPriority(operators);
        // 得到新的优先级，即随机生成括号
        ArrayList<Integer> newPriority = generatePriority(rawPriority.size());
        // 检测那些运算符优先级改变，并改变其负责括号的属性
        updateBracketAttribute(operators, rawPriority, newPriority);

        // 进行计算，考虑到负数和1/0的情况
        ArrayList<MathExpression> newExpressionStep = calculateResult(figuresExpression, operators,
                newPriority);
        if (newExpressionStep == null) return null;
        MathExpression result = newExpressionStep.get(newExpressionStep.size() - 1); // 得到最后一个元素
        // 判断是否重复，重复则重新生成
        if (judgeRepetitive(newExpressionStep, allExpressionStep)){
            return null;
        }
        allExpressionStep.add(newExpressionStep);
        // 最后一个元素即为最终整合结果
        return result;
    }

    /**
     * 得到运算符的初始优先级
     * @param operators 运算符数组
     * @return 优先级数组，其中值越小代表优先级越高
     */
    public static ArrayList<Integer> getPriority(ArrayList<Operator> operators){
        // 规则很简单，分为+-和*/，分别从左自右排优先级
        int operatorsSize = operators.size();
        ArrayList<Integer> priority = new ArrayList<>(operatorsSize);
        int p = 1; // 表明优先级
        // 先考虑*/的优先级
        for (int i = 0; i < operatorsSize; i++) {
            int value = operators.get(i).getValue();
            if(value == MULTIPLE || value == DEVICE){
                priority.set(i, p);
                p++;
            }
        }
        // 先考虑+-的优先级
        for (int i = 0; i < operatorsSize; i++) {
            int value = operators.get(i).getValue();
            if(value == PLUS || value == MINUS){
                priority.set(i, p);
                p++;
            }
        }
        return priority;
    }

    /**
     * 生成新的优先级
     * @param size 旧优先级的数量
     * @return 新优先级数组
     */
    public static ArrayList<Integer> generatePriority(int size){
        ArrayList<Integer> numbers = new ArrayList<>(size);
        for(int i = 1; i <= size; i++){
            numbers.set(i, i);
        }
        // 使用洗牌算法打乱数组顺序
        Random rand = new Random();
        for (int i = size - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // 交换位置
            int temp = numbers.get(i);
            numbers.set(i, numbers.get(j));
            numbers.set(j, temp);
        }
        return numbers;
    }

    /**
     * 修改运算符的是否需要括号属性，如果优先级改变则修改，反之则不修改
     * @param operators 运算符数组
     * @param rawPriority 原始优先级
     * @param newPriority 新优先级
     */
    public static void updateBracketAttribute(ArrayList<Operator> operators, ArrayList<Integer> rawPriority,
                                              ArrayList<Integer> newPriority){
        // contrast新旧优先级数组，如果有对应的优先级比初始小，表明其需要括号
        for(int i = 1; i <= rawPriority.size(); i++){
            if(newPriority.get(i) < rawPriority.get(i)){
                operators.get(i).setNeedBracket(true);
            }
        }
    }

    /**
     * 计算最终的结果
     * @param figuresExpression 每个封装为MathExpression的数值
     * @param operators 运算符数组
     * @param newPriority 新的优先级
     * @return 每一步计算的表达式，包含表达式的值和字符串形式，按照先后顺序组成了数组
     */
    public static ArrayList<MathExpression> calculateResult(ArrayList<MathExpression> figuresExpression,
                                          ArrayList<Operator> operators, ArrayList<Integer> newPriority){

        // 创建新数组，复制figuresExpression
        ArrayList<MathExpression> step = new ArrayList<>();
        // 从优先级高到低计算
        for(int i = 1; i <= newPriority.size(); i++){
            // 选取当前最小的元素，即优先级最高。它的下标也是表达式左值的下标
            int indexOfMinElement = newPriority.indexOf(Collections.max(newPriority));
            int indexOfLatterFigure = indexOfMinElement + 1; // 下标在运算符加1
            // 计算两个封装后的Figure
            MathExpression afterIntegration = calculateFigure(figuresExpression.get(indexOfMinElement),
                    figuresExpression.get(indexOfLatterFigure), operators.get(indexOfMinElement));
            if (afterIntegration == null) return null;
            // 去除最小值
            figuresExpression.remove(indexOfMinElement);
            // 去除后者，并将新的值赋给前者
            figuresExpression.set(indexOfMinElement, afterIntegration);
            figuresExpression.remove(indexOfLatterFigure);
            step.add(afterIntegration);
        }
        return step;
    }

    /**
     * 计算单个运算符表达式
     * @param formerFigure 左值
     * @param latterFigure 右值
     * @param operator 运算符
     * @return 得到的结果
     */
    public static MathExpression calculateFigure(MathExpression formerFigure, MathExpression latterFigure,
                                                 Operator operator){
        // 计算将得到一个新的表达式
        Figure former = formerFigure.getValue();
        Figure latter = latterFigure.getValue();
        // 计算值
        Figure newFigure = Util.calculateFractionExpression(former, latter, operator);
        if (newFigure == null) return null;
        formerFigure.setValue(newFigure);
        // 将表达式合并
        // 检测是否要加括号
        boolean isNeedBracket = operator.isNeedBracket();
        String formerForm = former.getForm();
        String latterForm = latter.getForm();
        // 如果哪个分母为1，则让其变为整数形式
        if (formerForm.contains("/1")) {
            formerForm = formerForm.replace("/1", "");
        }
        if (latterForm.contains("/1")) {
            latterForm = latterForm.replace("/1", "");
        }
        if (isNeedBracket){
            formerFigure.setFormOfFormula("(" + formerForm + operator.getValue() + latterForm + ")");
        } else {
            formerFigure.setFormOfFormula(formerForm + operator.getValue() + latterForm);
        }
        return formerFigure;
    }

    /**
     * 检测是否存在重复
     * @param newExpressionStep 生成新的表达式的步骤
     * @param allExpressionStep 以往生成的全部表达式的步骤
     * @return true表明重复，false表明不重复
     */
    public static boolean judgeRepetitive(ArrayList<MathExpression> newExpressionStep,
                                          ArrayList<ArrayList<MathExpression>> allExpressionStep){
        // 将newStep的步骤按照字符串数组读出
        ArrayList<String> newStep = new ArrayList<>();
        for (MathExpression me :
                newExpressionStep) {
            newStep.add(me.getFormOfFormula());
        }
        for (ArrayList<MathExpression> me :
                allExpressionStep) {
            ArrayList<String> pastStep = new ArrayList<>();
            for (MathExpression m :
                    me) {
                pastStep.add(m.getFormOfFormula());
            }
            // 将两字符串元素进行比较，若完全一样(不包含顺序)，则表明重复
            if (newStep.containsAll(pastStep) && pastStep.containsAll(newStep)){
                return true;
            }
        }
        return false;
    }
}
