package ru.sulgik.minotated;

public class Preconditions {

    public static void notNull(Object object, String message) throws NullPointerException{
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

}
