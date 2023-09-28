public class Number {
    private double value; // 存放计算得到的值
    private String form; // 存放真分数的表达形式

    public Number(){

    }

    public Number(double value, String form) {
        this.value = value;
        this.form = form;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
