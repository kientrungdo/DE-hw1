package homework;

public class Address {
	private int addressId;
	private String address;
	private String address2;
	private String district;
	private int cityId;
	private String postalCode;
	private String phone;
	private String location;
	private String lastUpdate;

	public Address(int addressId, String address, String address2, String district, int cityId, String postalCode,
			String phone, String location, String lastUpdate) {
		this.setAll(addressId, address, address2, district, cityId, postalCode, phone, location, lastUpdate);
	}
	
	public void setAll(int addressId, String address, String address2, String district, int cityId, String postalCode,
			String phone, String location, String lastUpdate) {
		this.addressId = addressId;
		this.address = address;
		this.address2 = address2;
		this.district = district;
		this.cityId = cityId;
		this.postalCode = postalCode;
		this.phone = phone;
		this.location = location;
		this.lastUpdate = lastUpdate;
	}
	
	public Address(String csvLine) {
		//"address_id","address","address2","district","city_id","postal_code","phone","location","last_update"
		String[] comps = csvLine.split(",");
		int addressId = (int) Double.parseDouble(comps[0]);
		String address = comps[1].substring(1, comps[1].length()-1);
		String address2 = comps[2];
		String district = comps[3].substring(1, comps[3].length()-1);
		int cityId = (int) Double.parseDouble(comps[4]);
		String postalCode = comps[5].replace('"', ' ').trim();
		String phone = comps[6];
		String location = comps[7];
		String lastUpdate = comps[8];
		this.setAll(addressId, address, address2, district, cityId, postalCode, phone, location, lastUpdate);
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Address{" + "addressId=" + addressId + ", address='" + address + '\'' + ", address2='" + address2 + '\''
				+ ", district='" + district + '\'' + ", cityId=" + cityId + ", postalCode='" + postalCode + '\''
				+ ", phone='" + phone + '\'' + ", location='" + location + '\'' + ", lastUpdate='" + lastUpdate + '\''
				+ '}';
	}
}