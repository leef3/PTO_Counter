package com.redleefstudios.ptocounter;

/**
 * Created by Fred on 3/29/2016.
 */
public class PTOItem
{
    private String event;
    private Category type;
    private int days;


    public PTOItem(String event, Category type, int days)
    {
        this.event = event;
        this.type = type;
        this.days = days;
    }

    public String GetEvent() { return event; }
    public Category GetType() { return type; }
    public int GetDays() { return days; }

    public void SetEvent(String toSet) { event = toSet; }
    public void SetType(Category toSet) { type = toSet; }
    public void SetDays(int toSet) { days = toSet ; }
}

