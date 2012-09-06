package com.boz.poc.presentation.tests.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DynamicColumnsBean implements Serializable {
	private static final long serialVersionUID = 1L;

	List<Task> data = new ArrayList<Task>() {
		private static final long serialVersionUID = 1L;
		{
			int i = 0;
			while (i != 10) {
				add(new Task(i, "Topic " + i, "Action " + i, "Notes " + i++));
			}
		}
	};

	List<ColumnModel> columns = new ArrayList<ColumnModel>() {
		private static final long serialVersionUID = 1L;
		{
			add(new ColumnModel("id", "ID"));
			add(new ColumnModel("topic", "Topic"));
			add(new ColumnModel("action", "Action"));
			add(new ColumnModel("notes", "Notes"));
		}
	};

	public List<Task> getData() {
		return data;
	}

	public void setData(final ArrayList<Task> data) {
		this.data = data;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(final ArrayList<ColumnModel> columns) {
		this.columns = columns;
	}
}