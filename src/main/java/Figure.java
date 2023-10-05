public class Figure {
    private double value; // 存放计算得到的值
    private String form; // 存放分数的表达形式
    private String recordStep; // 记录得到这个数字所经历的上一个计算步骤，格式：操作数 (运算符 操作数)

    public Figure(){
    }

    public Figure(double value, String form, String recordStep) {
        this.value = value;
        this.form = form;
        this.recordStep = recordStep;
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

    public String getRecordStep() {
        return recordStep;
    }

    public void setRecordStep(String recordStep) {
        this.recordStep = recordStep;
    }
}
