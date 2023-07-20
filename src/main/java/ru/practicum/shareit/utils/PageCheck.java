package ru.practicum.shareit.utils;

import ru.practicum.shareit.exceptions.exceptions.WrongParameter;

public class PageCheck {

    private PageCheck() {
    }

    public static boolean isWithoutPage(Integer from, Integer size) {
        if (from == null || size == null) {
            return true;
        } else {
            if (from < 0 || size < 1) {
                throw new WrongParameter("Указаны неправильные параметры.");
            }
            return false;
        }
    }

}