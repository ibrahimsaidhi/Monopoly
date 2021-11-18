package Model;

public class Utility extends Square{

    private String name;
    private String color;
    private int value;

    public Utility(String name, String color, int value ){
        this.name = name;
        this.color = color;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
