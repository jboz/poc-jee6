package com.boz.poc.presentation.tests;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import com.boz.poc.domain.Car;

@Named
@RequestScoped
public class CarBean {

	List<Car> cars = new ArrayList<Car>();
	List<Car> selected;

	@PostConstruct
	public void init() {
		selected = new ArrayList<Car>();
		final String[] name = { "Toyota", "Maruti", "Indica", "Centro", "AAAAA", "BBBBBB", "CCCCCC", "DDDDD", "EEEEEE", "FFFFFF",
				"GGGGGG" };
		for (int i = 0; i < name.length; i++) {
			cars.add(new Car(i, name[i]));
		}
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(final List<Car> cars) {
		this.cars = cars;
	}

	public List<Car> getSelected() {
		return selected;
	}

	public void setSelected(final List<String> newSelectedIds) {
		selected.clear();
		for (final String carId : newSelectedIds) {
			if (selected.add(getCarById(Long.valueOf(carId)))) {
				System.out.println(getCarById(Long.valueOf(carId)));
			}
		}
	}

	private Car getCarById(final long id) {
		for (final Car car : cars) {
			if (car.getId() == id) {
				return car;
			}
		}
		return null;
	}

	public String onSave() {
		System.out.println("Inside onSave..");
		for (final Car c : selected) {
			System.out.println(c);
		}
		return null;
	}
}