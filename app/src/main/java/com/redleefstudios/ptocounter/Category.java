package com.redleefstudios.ptocounter;

/**
 * Created by Fred on 3/29/2016.
 */
public enum Category {
    VACATION,
    SICK,
    OTHER;

    public static String[] names()
    {
        Category[] categories = values();
        String[] names = new String[categories.length];

        for(int i = 0; i < categories.length; i++)
        {
            names[i] = categories[i].name();
        }

        return names;
    }
}
