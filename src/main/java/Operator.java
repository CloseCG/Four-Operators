public class Operator {
    private int value; // 表面运算符号的类型。1:2:3:4 = +:-:×:÷
    private boolean isNeedBracket; // 标记该运算符两个操作数两侧是否需要括号，true表明需要，false表明不需要

    public Operator() {
    }

    public Operator(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isNeedBracket() {
        return isNeedBracket;
    }

    public void setNeedBracket(boolean needBracket) {
        isNeedBracket = needBracket;
    }
}
