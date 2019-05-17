package fm.doe.national.app_support.utils;

public class IntRange {

    private int start;
    private int end;

    public IntRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean containsValue(int value) {
        return containsValue((float) value);
    }

    public boolean containsValue(float value) {
        return start <= value && value <= end;
    }
}
