package homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//this exercise uses a portion of the sakila database
//https://dev.mysql.com/doc/sakila/en/sakila-structure.html

public class CustomerProcessingApp {
	private List<Customer> customers;
	private List<Address> addresses;

	// do not delete this constructor, the test suite depends on it
	public CustomerProcessingApp(String...args) throws IOException {
		String customerFile = args[0];
		String addressFile = args[1];
		Stream<String> customerLines = Files.lines(Path.of(customerFile)).skip(1); // skip the header
		this.customers = customerLines.map(line -> new Customer(line)).collect(Collectors.toList());
		customerLines.close();

		Stream<String> orderLines = Files.lines(Path.of(addressFile)).skip(1); // skip the header
		this.addresses = orderLines.map(line -> new Address(line)).collect(Collectors.toList());
		orderLines.close();
	}

	// freebie, how many records are there in customers?
	@Question(1)
	public long test1() {
		return this.customers.stream().count();
	}

	// report the first five customers
	@Question(2)
	public List<Customer> test2() {
		return this.customers.stream().limit(5).collect(Collectors.toList());
	}
	
	// report the last five customers
	@Question(3)
	public List<Customer> test3() {
		int n = this.customers.size();
		return this.customers.stream().skip(n-5).collect(Collectors.toList());
	}

	// report the number of active customers
	@Question(4)
	public long test4() {
		return this.customers.stream().filter(x -> x.isActive() == true).count();
	}

	// report the percentage of customers that are active, use Utilities.round to
	// round the number
	@Question(5)
	public double test5() {
		// The reason (float) goes first: the division alone would return 0
		return Utilities.round((float) this.customers.stream()
				.filter(x -> x.isActive()==true).count() / this.customers.size());
	}

	// report ids of customers whose last names start with letter S
	@Question(6)
	public List<Integer> test6() {
		return this.customers.stream()
				.filter(x -> x.getLastName().startsWith("S"))
				.map(x -> x.getCustomerId())
				.collect(Collectors.toList());
	}

	// what is the email address of EDNA WEST?
	@Question(7)
	public String test7() {
		//return null;
		return this.customers.stream()
				.filter(x -> x.getFirstName().equals("EDNA") & x.getLastName().equals("WEST"))
				.map(x -> x.getEmail()).collect(Collectors.toList()).get(0);
	}

	// report customer records with a first name more than nine characters
	@Question(8)
	public List<Customer> test8() {
		return this.customers.stream()
				.filter(x -> x.getFirstName().length() > 9)
				.collect(Collectors.toList());
	}

	// report emails of customers with a first name starting with a vowel and a last
	// name starting with a vowel (a, e, i, o, u)
	@Question(9)
	public List<String> test9() {
		return this.customers.stream()
				.filter(x -> "AEIOU".indexOf(x.getFirstName().charAt(0)) != -1
						& "AEIOU".indexOf(x.getLastName().charAt(0)) != -1)
				.map(x -> x.getEmail())
				.collect(Collectors.toList());
	}

	// report the number of customers per storeID
	@Question(10)
	public Map<Integer, Long> test10() {
		return this.customers.stream()
				.collect(Collectors.groupingBy(Customer::getStoreId, Collectors.counting()));
	}

	// number of addresses where district is California
	@Question(11)
	public long test11() {
		return this.addresses.stream().filter(x -> x.getDistrict().equals("California")).count();
	}

	// number of unique postal codes
	@Question(12)
	public long test12() {
		return this.addresses.stream().map(x -> x.getPostalCode()).distinct().count();
	}

	// number of records without a postal code
	@Question(13)
	public long test13() {
		return this.addresses.stream().filter(x -> x.getPostalCode().equals("")).count();
	}

	// what is the street address of MARY SMITH?
	@Question(14)
	public String test14() {
		// get id of Mary Smith
		int ms = this.customers.stream()
				.filter(x -> x.getFirstName().equals("MARY") & x.getLastName().equals("SMITH"))
				.map(x -> x.getAddressId()).collect(Collectors.toList()).get(0);
				
		return this.addresses.stream().filter(x -> x.getAddressId()==ms)
				.map(x -> x.getAddress()).collect(Collectors.toList()).get(0);
	}

	// number of customers who live in addresses where district equal to California
	@Question(15)
	public long test15() {
		List<Integer> addressInCA = this.addresses.stream()
				.filter(x -> x.getDistrict().equals("California"))
				.map(x -> x.getAddressId()).collect(Collectors.toList());
		
		return this.customers.stream().filter(x -> addressInCA.contains(x.getAddressId())).count();
	}

	// what are addresses without associated customers, report the ids
	@Question(16)
	public List<Integer> test16() {
		List<Integer> IdsWithCustomers = this.customers.stream().map(x -> x.getAddressId()).distinct().collect(Collectors.toList());
		return this.addresses.stream().map(x -> x.getAddressId()).filter(x -> !IdsWithCustomers.contains(x)).collect(Collectors.toList());
	}

	// create a list of customer names and their street addresses formatted as
	// "FIRSTNAME LASTNAME @ ADDRESS"
	// show the first five records
	@Question(17)
	public List<String> test17() {
		return this.customers.stream()
				.limit(5).map(x -> x.getFirstName() + " " + x.getLastName() + " @ " 
						+ this.addresses.stream().filter(y -> y.getAddressId() == x.getAddressId()).map(y -> y.getAddress()).collect(Collectors.toList()).get(0))
				.collect(Collectors.toList());
	}

	// number of addresses where the street number (first word in address) is a
	// prime number
	// use the function Utilities.isPrime
	@Question(18)
	public long test18() {
		return this.addresses.stream()
				.filter(x -> Utilities.isPrime(Integer.parseInt(x.getAddress().split(" ", 2)[0]))).count();
	}

	// the address location is a point representing Latitude and Longitude
	// "POINT (-112.8185647 49.6999986)" Lat is -112 and Lon is 49
	// what is address of the northmost location (largest lat)
	@Question(19)
	public Address test19() {
		//return this.addresses.stream().collect(Collectors.toList()).get(0);
		return this.addresses.stream()
				.reduce((a, b) -> getLat(a.getLocation()) > getLat(b.getLocation()) ? a : b).get();
	}
	
	public double getLat(String s) {
		return Double.parseDouble(s.split("\\(")[1].split(" ")[0]);
	}

	// another freebie, who is your favorite instructor?
	@Question(20)
	public String test20() {
		return "Safadi";
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to the CustomerProcessingApp");
		if (args.length == 0)
			args = new String[] { "customer.csv", "address.csv" };

		CustomerProcessingApp app = new CustomerProcessingApp(args[0], args[1]);
		// print everything (not needed, but enable to check that data was read
		// properly)
		// app.customers.forEach(customer -> System.out.println(customer));
		// app.addresses.forEach(address -> System.out.println(address));

		//complete the homework by implementing the test methods above
		
		System.out.println(app.test1());
		System.out.println(app.test2());
		System.out.println(app.test3());
		System.out.println(app.test4());
		System.out.println(app.test5());
		System.out.println(app.test6());
		System.out.println(app.test7());
		System.out.println(app.test8());
		System.out.println(app.test9());
		System.out.println(app.test10());
		System.out.println(app.test11());
		System.out.println(app.test12());
		System.out.println(app.test13());
		System.out.println(app.test14());
		System.out.println(app.test15());
		System.out.println(app.test16());
		System.out.println(app.test17());
		System.out.println(app.test18());
		System.out.println(app.test19());
		System.out.println(app.test20());
		//System.out.println(app.getLat("POINT (-112.8185647 49.6999986)"));

	}

}
