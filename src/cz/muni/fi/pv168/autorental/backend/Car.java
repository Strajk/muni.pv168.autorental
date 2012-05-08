package cz.muni.fi.pv168.autorental.backend;

import java.math.BigDecimal;

/**
 * This entity represents Car.
 * Car has some model name, plate and fee
 * 
 */
public class Car {
    
    private Long id;
    private String model;
    private String plate;
    private BigDecimal fee;

    public Car() {
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if ((this.model == null) ? (other.model != null) : !this.model.equals(other.model)) {
            return false;
        }
        if ((this.plate == null) ? (other.plate != null) : !this.plate.equals(other.plate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.model != null ? this.model.hashCode() : 0);
        hash = 59 * hash + (this.plate != null ? this.plate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
	return "Car{" + "id=" + id + ", model=" + model + ", plate=" + plate + ", fee=" + fee + '}';
    }
    
}
