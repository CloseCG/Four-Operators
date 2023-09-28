public class Figure {
    private String value; // 存放计算得到的值
    private String form; // 存放真分数的表达形式

    public Figure(){
    }

    public Figure(String value, String form) {
        this.value = value;
        this.form = form;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
