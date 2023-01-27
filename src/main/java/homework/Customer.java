package homework;

public class Customer {
	private int customerId;
	private int storeId;
	private String firstName;
	private String lastName;
	private String email;
	private int addressId;
	private boolean active;
	private String createDate;
	private String lastUpdate;

	public Customer(int customerId, int storeId, String firstName, String lastName, String email,
			int addressId, boolean active, String createDate, String lastUpdate) {
		this.setAll(customerId, storeId, firstName, lastName, email, addressId, active, createDate, lastUpdate);
	}
	
	public void setAll(int customerId, int storeId, String firstName, String lastName, String email,
			int addressId, boolean active, String createDate, String lastUpdate) {
		this.customerId = customerId;
		this.storeId = storeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.addressId = addressId;
		this.active = active;
		this.createDate = createDate;
		this.lastUpdate = lastUpdate;
	}
	public Customer(String csvLine) {
		//"customer_id","store_id","first_name","last_name","email","address_id","active","create_date","last_update"
		String[] comps = csvLine.split(",");
		int customerId = (int) Double.parseDouble(comps[0]);
		int storeId = (int) Double.parseDouble(comps[1]);
		String firstName = comps[2].substring(1, comps[2].length()-1);
		String lastName = comps[3].substring(1, comps[3].length()-1);
		String email = comps[4].substring(1, comps[4].length()-1);
		int addressId = (int) Double.parseDouble(comps[5]);
		boolean active = Double.parseDouble(comps[6]) == 1.0;
		String createDate = comps[7];
		String lastUpdate = comps[8];
		
		this.setAll(customerId, storeId, firstName, lastName, email, addressId, active, createDate, lastUpdate);

	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Customer{" + "customerId=" + customerId + ", storeId=" + storeId + ", firstName='" + firstName + '\''
				+ ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", addressId=" + addressId
				+ ", active=" + active + ", createDate=" + createDate + ", lastUpdate=" + lastUpdate + '}';
	}
}