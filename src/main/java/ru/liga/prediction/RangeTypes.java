package ru.liga.prediction;


public enum RangeTypes {
    TOMORROW(1), WEEK(7);

    private final int days;

    RangeTypes(int days) {
        this.days = days;
    }

    public static RangeTypes findByName(String s) {
        for (RangeTypes rangeTypes : values()) {
            if (rangeTypes.name().equalsIgnoreCase(s)) {
                return rangeTypes;
            }
        }
        return null;
    }

    public int getDays() {
        return days;
    }

}
