package stores;

public class Store {
	String storeName;
	int storeId;
		
	public String getName() {
		return storeName;
	}
	public void setName(String name) {
		this.storeName = name;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	@Override
	public String toString() {
		return "Store [storeName=" + storeName + ", storeId=" + storeId + "]";
	}
	
	
}
