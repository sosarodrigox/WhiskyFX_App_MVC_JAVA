package utn.frp.p3.application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Whisky {
	private final IntegerProperty id;
	private final StringProperty name;
	private final StringProperty origin;

	public Whisky() {
		this.id = new SimpleIntegerProperty();
		this.name = new SimpleStringProperty();
		this.origin = new SimpleStringProperty();
	}

	public Whisky(int id, String name, String origin) {
		this();
		this.id.set(id);
		this.name.set(name);
		this.origin.set(origin);
	}

	public IntegerProperty getId() {
		return id;
	}

	public StringProperty getName() {
		return name;
	}

	public StringProperty getOrigin() {
		return origin;
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setOrigin(String origin) {
		this.origin.set(origin);
	}
}
