package cz.muni.fi.pv168.autorental.backend;

import java.math.BigDecimal;
import java.sql.Date;

public class Rent {
    
    private Long id;
    private Car car;
    private Customer customer;
    private Date from, to;
    private BigDecimal cost;

    public Rent() {
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BigDecimal getCost() {
        return cost;
    }
    
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal calculateCost() {
        BigDecimal days = BigDecimal.valueOf(((to.getTime() - from.getTime())/86400000)+1); // 24×60×60×10×10×10
	BigDecimal fee = car.getFee();
	BigDecimal result = fee.multiply(days);
        return result;
    };
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rent other = (Rent) obj;
        if (this.car != other.car && (this.car == null || !this.car.equals(other.car))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.from != other.from && (this.from == null || !this.from.equals(other.from))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        if (this.cost != other.cost && (this.cost == null || !this.cost.equals(other.cost))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.car != null ? this.car.hashCode() : 0);
        hash = 97 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 97 * hash + (this.from != null ? this.from.hashCode() : 0);
        hash = 97 * hash + (this.to != null ? this.to.hashCode() : 0);
        hash = 97 * hash + (this.cost != null ? this.cost.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
	return "Rent{" + "id=" + id + ", car=" + car + ", customer=" + customer + ", from=" + from + ", to=" + to + ", cost=" + cost + '}';
    }

}
