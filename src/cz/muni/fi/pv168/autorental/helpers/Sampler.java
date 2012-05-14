/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.helpers;

import cz.muni.fi.pv168.autorental.backend.Car;
import cz.muni.fi.pv168.autorental.backend.Customer;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Random;

public class Sampler {
    private static final Random RANDOM = new Random();

    public static Car createSampleCar() {
	String[] CAR_MODELS = { "Škoda Felicia", "Škoda Octavia", "Ford Focus", "Ford Fiesta", "Renault Clio", "Porsche Carrera" };

	Car car = new Car();
	car.setModel(CAR_MODELS[RANDOM.nextInt(CAR_MODELS.length)]);
	car.setPlate("SAM " + (RANDOM.nextInt(9000) + 1000));
	car.setFee(new BigDecimal(RANDOM.nextInt(900) + 100));
	
	return car;
    }
    
    public static Customer createSampleCustomer() {
	String[] FIRSTNAMES = { "Pavel", "PEtr", "Ondra", "Josef", "Roman"};
	String[] LASTNAMES = { "Novák", "Novotný", "Veselý", "Smutný"};
        
        String[] DAYS = {"01", "02", "03"};
        String[] MONTHS = {"10", "11", "12"};
        String[] YEARS = {"1990", "1991", "1992"};

	Customer customer = new Customer();
	customer.setFirstname(FIRSTNAMES[RANDOM.nextInt(FIRSTNAMES.length)]);
	customer.setLastname(LASTNAMES[RANDOM.nextInt(LASTNAMES.length)]);
	customer.setBirth( Date.valueOf( YEARS[RANDOM.nextInt(YEARS.length)] + '-' + MONTHS[RANDOM.nextInt(MONTHS.length)] + '-' + DAYS[RANDOM.nextInt(DAYS.length)] ) );
	customer.setEmail("example@example.com");
	// http://stackoverflow.com/questions/363681/java-generating-random-number-in-a-range
	
	return customer;
    }
}
