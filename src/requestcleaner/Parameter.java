package requestcleaner;

import burp.IParameter;

public class Parameter implements IParameter {

    private byte type;
    private String name;
    private String value;
    private int nameStart;
    private int nameEnd;
    private int valueStart;
    private int valueEnd;

    public Parameter(IParameter parameter) {
        this.type = parameter.getType();
        this.name = parameter.getName();
        this.value = parameter.getValue();
        this.nameStart = parameter.getNameStart();
        this.nameEnd = parameter.getNameEnd();
        this.valueStart = parameter.getValueStart();
        this.valueEnd = parameter.getValueEnd();
    }

    @Override
    public byte getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getNameStart() {
        return this.nameStart;
    }

    @Override
    public int getNameEnd() {
        return this.nameEnd;
    }

    @Override
    public int getValueStart() {
        return this.valueStart;
    }

    @Override
    public int getValueEnd() {
        return this.valueEnd;
    }
}
