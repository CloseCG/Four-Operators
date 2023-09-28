public class MathExpression {
    private Number value; // 对应表达式计算得到的值
    private String formOfFormula; // 表达式的形式

    public MathExpression() {
    }

    public MathExpression(Number value, String formOfFormula) {
        this.value = value;
        this.formOfFormula = formOfFormula;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getFormOfFormula() {
        return formOfFormula;
    }

    public void setFormOfFormula(String formOfFormula) {
        this.formOfFormula = formOfFormula;
    }
}
