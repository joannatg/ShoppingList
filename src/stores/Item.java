package stores;

public class Item {
	int itemId;
	String name;
	String category;
	int quantity;
	int storeId;
	boolean isBought;
	
	
	
	public String getCreateItemCode() {
		return createItemCode;
	}
	public void setCreateItemCode(String createItemCode) {
		this.createItemCode = createItemCode;
	}
	public String getDeleteItemCode() {
		return deleteItemCode;
	}
	public void setDeleteItemCode(String deleteItemCode) {
		this.deleteItemCode = deleteItemCode;
	}

	String createItemCode;
	String deleteItemCode;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", name=" + name + ", category="
				+ category + ", quantity=" + quantity + ", storeId=" + storeId
				+ ", isBought=" + isBought + "]";
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public boolean isBought() {
		return isBought;
	}
	public void setBought(boolean isBought) {
		this.isBought = isBought;
	}
	
	public boolean getIsBought(){
		return isBought;
	}
}
