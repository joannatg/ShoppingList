package stores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoresDAO {

	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public static final String DATABASE_NAME = "shopping_list";
	public static final String URL = "jdbc:mysql://localhost:3306/"
			+ DATABASE_NAME;
	public static final String USER = "root";
	public static final String PASSWORD = "nimda";

	public StoresDAO() {

	}

	public List<Store> getAllStores() {
		List<Store> stores = new ArrayList<Store>();

		ResultSet rs = null;
		String query = "SELECT * FROM store ORDER BY StoreId";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();)

		{
			rs = statement.executeQuery(query);
			stores = createStoresFromResultSet(rs);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return stores;
	}

	private List<Store> createStoresFromResultSet(ResultSet rs)
			throws SQLException {
		List<Store> stores = new ArrayList<Store>();

		while (rs.next()) {

			Store s = createStoreFromResultSet(rs);
			stores.add(s);
		}
		return stores;

	}

	private Store createStoreFromResultSet(ResultSet rs) throws SQLException {
		Store store = new Store();
		store.setStoreId(rs.getInt("StoreId"));
		store.setName(rs.getString("Name"));
		return store;
	}

	public int createNewStore(Store store) {

		int idForNewStore = -1;
		// ResultSet rs = null;
		String query = "INSERT INTO store (Name) VALUES ('" + store.getName() + "')";

		System.out.println("Executing query: " + query);

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Store name is: " + store.getName() );
		System.out.println("Store id is: " + store.getStoreId() );
		
		Store createdStore = this.getStoreByName(store.getName());
		idForNewStore = createdStore.getStoreId();

		return idForNewStore;
	}
	
	private Store getStoreByName(String storeName) {
		Store store = null;

		if (storeName == null) {
			return null;
		}

		String query = "SELECT * FROM store where Name = \"" + storeName  + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				store = createStoreFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return store;
	}

	/*
	 * public int getStoreById(int storeId) { // TODO Auto-generated method stub
	 * return 0; }
	 */

	public Store getStoreById(int storeId) {
		Store store = null;

		if (storeId == 0) {
			return null;
		}
		
		String query = "SELECT * FROM store where StoreId = \"" + storeId
				+ "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				store = createStoreFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return store;
	}

	public Store getStoreByStoreName(String storeName) {

		Store store = null;
		String query = "SELECT * FROM store where StoreName = \"" + storeName
				+ "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				store = createStoreFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public void deleteStoreById(int storeId) {

		String query = "DELETE from store WHERE StoreId = '" + storeId + "'";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public List<Item> getItemsByStoreId(int storeId) {
		List<Item> items = null;

		String query = "SELECT * FROM item where StoreId = '" + storeId + "'";

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);
			items = createItmesFromResultSet(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	private List<Item> createItmesFromResultSet(ResultSet rs)
			throws SQLException {

		List<Item> items = new ArrayList<Item>();
		while (rs.next()) {
			Item item = createItemFromResultSet(rs);
			items.add(item);
		}
		return items;

	}

	private Item createItemFromResultSet(ResultSet rs) throws SQLException {
		Item item = new Item();
		item.setItemId(rs.getInt("ItemId"));
		item.setName(rs.getString("Name"));
		item.setCategory(rs.getString("Category"));
		item.setQuantity(rs.getInt("Quantity"));
		item.setStoreId(rs.getInt("StoreId"));
		item.setBought(rs.getBoolean("IsBought"));

		return item;
	}

	public int createNewItemForStore(Item item) {

		int isBoughtVal = (item.getIsBought()) ? (1) : (0);

		int id = -1;
		String query = "INSERT INTO item (Name, Category, Quantity, StoreId, IsBought) VALUES ("
				+ "'"
				+ item.getName()
				+ "', '"
				+ item.getCategory()
				+ "', "
				+ item.getQuantity()
				+ ", "
				+ item.getStoreId()
				+ ", "
				+ isBoughtVal + ")";

		System.out.println("Executing query: " + query);

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Item name is: " + item.getName());
		System.out.println("Store id for new item is: " + item.getStoreId());
		Item createdItem = this.getItemByNameInStore(item.getName(),item.getStoreId());
		id = createdItem.getItemId();
		return id;
	}

	private Item getItemByNameInStore(String name, int storeId) {

		Item item = null;

		if (name == null) {
			return null;
		}
		if (storeId == 0) {
			return null;
		}

		String query = "SELECT * FROM item where Name = \"" + name
				+ "\" and StoreId = \"" + storeId + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				item = createItemFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return item;
	}

	public Item getItemById(int itemId) {

		Item item = null;

		if (itemId == 0) {
			return null;
		}
		String query = "SELECT * FROM item WHERE ItemId = \"" + itemId + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				item = createItemFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return item;
	}

	public boolean isExistingItem(int itemId) {
		return (this.getItemById(itemId) != null);
	}

	public void deleteItemById(int itemId) {

		String query = "DELETE from item WHERE ItemId = \"" + itemId + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void updateItem(Item item) {

		//int isBoughtVal = (item.getIsBought()) ? (1) : (0);

		String query = "UPDATE item SET Name= '" + item.getName()
				+ "', Category='" + item.getCategory()

				+ "', Quantity=" + item.getQuantity() + ", StoreId="
				+ item.getStoreId() + ", IsBought=" + item.getIsBought()
				+ "  WHERE ItemId = " + item.getItemId() + "";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void updateStore(Store store) {
		String query = "UPDATE store SET Name= '" + store.getName()	+ "' WHERE StoreId = " + store.getStoreId() + "";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
