package ru.javawebinar.basejava;

import java.util.*;
import java.util.stream.Collectors;

public class MainStream {

    public int maxValue(int[] values) {
        return Arrays.stream(values)
                .boxed()
                .distinct()
                .sorted(Collections.reverseOrder())
                .reduce(0, (accumulator, element) -> accumulator * 10 + element);
    }

    public int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (accumulator, element) -> accumulator * 10 + element);
    }

    public List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> integersByOddAndEven = integers.stream()
                .collect(Collectors.partitioningBy(number -> number % 2 == 0));
        return integersByOddAndEven.get(integersByOddAndEven.get(false).size() % 2 != 0);
    }

    public static void main(String[] args) {
        MainStream ms = new MainStream();
        int[] numbers = {9, 7, 9, 1, 5, 3};
        System.out.println("Maximum number: " + ms.maxValue(numbers));
        System.out.println("Minimum number: " + ms.minValue(numbers));

        Integer[] evenNumbersSum = {2, 3, 3, 1, 2, 3, 0, 10};
        Integer[] oddNumbersSum = {4, 4, 4, 4, 1, 3, 5, 10, 0};
        List<Integer> evenSumList = new ArrayList<>(List.of(evenNumbersSum));
        List<Integer> oddSumList = new ArrayList<>(List.of(oddNumbersSum));
        System.out.println("Result for even sum: " + ms.oddOrEven(evenSumList));
        System.out.println("Result for odd sum: " + ms.oddOrEven(oddSumList));
    }
}   
