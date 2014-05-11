package net.lapasa.barbudget.models;

import com.orm.SugarRecord;

public class Book extends SugarRecord<Book>
{
	private String title;
	private String edition;
	
	
	public Book(){}

	public Book(String title, String edition)
	{
		this.title = title;
		this.edition = edition;
	}
}
