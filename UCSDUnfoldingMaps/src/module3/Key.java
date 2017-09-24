package module3;

import java.util.ArrayList;
import java.util.List;

import processing.core.PShape;

public class Key
{
	public static class Entry
	{
		private PShape shape;
		private String text;

		public Entry(PShape shape, String text)
		{
			this.shape = shape;
			this.text = text;
		}

		public PShape getShape()
		{
			return shape;
		}

		public String getText()
		{
			return text;
		}

	}

	String name;
	List<Entry> entries;

	public Key()
	{
		name = "Key";
		entries = new ArrayList<Entry>();
	}

	public Key(String name)
	{
		this.name = name;
		entries = new ArrayList<Entry>();
	}
	public Key(List<Entry> entries)
	{
		name = "Key";
		this.entries = entries;
	}

	public Key(String name, List<Entry> entries)
	{
		this.name = name;
		this.entries = entries;
	}

	public boolean addEntry(PShape shape, String text)
	{
		return entries.add(new Entry(shape, text));
	}
	
	public Entry removeEntry(int index)
	{
		return entries.remove(index);
	}
	
	public List<Entry> getEntries(){
		
		return entries;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}

}
