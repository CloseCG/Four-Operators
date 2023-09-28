import java.util.ArrayList;

public class MainClass {
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
            if(allExpression == null){
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
     * 生成表达式
     * @param num 生成数量
     * @param range 每个值的大小范围，属于[0,range)
     * @return 返回每个表达式构成的数组
     */
    public static ArrayList<MathExpression> generateAll(int num, int range){
        ArrayList<MathExpression> allExpression = new ArrayList<>();
        while(num > 0){
            // 生成单个表达式
            MathExpression oneExpression = generateOne(range, allExpression);
            if (oneExpression == null){
                return null;
            }
            allExpression.add(oneExpression);
            num--;
        }
        return allExpression;
    }

    /**
     * 生成单个表达式
     * @param range 表达式值的上限
     * @param allExpression 全部表达式
     * @return 生成的新表达式
     */
    public static MathExpression generateOne(int range, ArrayList<MathExpression> allExpression){
        // 随机产生运算符的数目，属于[1,3]

        // 产生的数的数量为运算符+1

        // 得到生成多项式的原优先级

        // 得到新的优先级

        // 进行计算，考虑到负数和1/0的情况

        // 判断是否重复，重复则重新生成

        //
        return null;
    }
}
