package com.boz.poc.domain;

import java.io.Serializable;

public class Car implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;

	public Car() {
	}

	public Car(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Car other = (Car) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null ? other.name != null : !name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (int) (id ^ id >>> 32);
		hash = 23 * hash + (name != null ? name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Car{" + "id=" + id + ", name=" + name + "}";
	}
}