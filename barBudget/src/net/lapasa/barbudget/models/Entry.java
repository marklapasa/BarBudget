package net.lapasa.barbudget.models;

import java.util.Date;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 3/31/14.
 */
public class Entry extends SugarRecord<Entry>
{

    private Date timestamp;
    private double value;
    private String memo;
    private Category category;
    
    /**
     * Empty constructor needed for SugarORM 1.3
     */
//    public Entry(){}


    public Entry(Date timestamp, double value, String memo, Category category)
    {
        this.timestamp = timestamp;
        this.value = value;
        this.memo = memo;
        this.category = category;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public double getValue()
    {
        return value;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }


    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

}
