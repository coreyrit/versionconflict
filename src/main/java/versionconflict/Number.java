package versionconflict;

import java.util.ArrayList;
import java.util.List;

public enum Number {
    Zero("zero.png"),
    One("one.png"),
    Two("two.png"),
    Three("three.png"),
    Four("four.png"),
    Five("five.png"),
    Six("six.png"),
    Seven("seven.png"),
    Eight("eight.png"),
    Nine("nine.png");

    public String image;

    Number(String img) {
        this.image = img;
    }

    public static Number fromDigit(int digit) {
        switch (digit) {
            case 0:
                return Zero;
            case 1:
                return One;
            case 2:
                return Two;
            case 3:
                return Three;
            case 4:
                return Four;
            case 5:
                return Five;
            case 6:
                return Six;
            case 7:
                return Seven;
            case 8:
                return Eight;
            case 9:
                return Nine;
        }
        throw new RuntimeException(digit + " is not a single digit.");
    }

    public static List<Number> fromInt(int number) {
        List<Number> numbers = new ArrayList<Number>();
        String s = Integer.toString(number);
        for(int i = 0; i < s.length(); i++) {
            int digit = Integer.parseInt(Character.toString(s.charAt(i)));
            numbers.add(fromDigit(digit));
        }
        return numbers;
    }
}
