package stores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StoresServlet
 */
@WebServlet("/StoresServlet")
public class StoresServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public static final String FORM_ITEM_ID = "ItemId";
	public static final String FORM_ITEM_NAME = "ItemName";
	public static final String FORM_ITEM_CATEGORY = "Category";
	public static final String FORM_ITEM_QUANTITY = "Quantity";
	public static final String FORM_ITEM_ISBOUGHT = "IsBought";

	public static final String ACTION_CODE_UPDATE_ITEM = "UpdateItem";
	public static final String ACTION_CODE_CREATE_ITEM = "CreateItem";
	public static final String ACTION_CODE_DELETE_ITEM = "DeleteItem";
	public static final String ACTION_CODE_ONE = "GetItemById";
	public static final String ACTION_CODE_TWO = "GetItemByName";
	public static final String ACTION_CODE_THREE = "GetItemsByStoreId";
	
	public static final String FORM_STORE_ID = "StoreId";
	public static final String FORM_STORE_NAME = "StoreName";

	public static final String ACTION_CODE_UPDATE_STORE = "UpdateStore";
	public static final String ACTION_CODE_CREATE_STORE = "CreateStore";
	public static final String ACTION_CODE_DELETE_STORE = "DeleteStore";
	public static final String ACTION_CODE_GET_STORE = "GetStore";
	
	public static final String ACTION_CODE_FOUR = "GetAllStores";
	public static final String ACTION_CODE_FIVE= "GetStoreById";
	public static final String ACTION_CODE_SIX= "GetStoreByName";
	public static final String ACTION_CODE_PARAM_NAME = "ActionName";
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StoresServlet() {
		super();

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot load the driver");
			e.printStackTrace();
		}

		this.storesDAO = new StoresDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<html><body>");
			
			
		if (isListAllStoresRequest(request)) {

			System.out.println("isGetAllStores: "
					+ isListAllStoresRequest(request));
			this.printHtmlFormForNewStore(out);

			List<Store> stores = this.getAllStores();
			this.printHtmlTableForStores(stores, out);
		}

		else if  (isGetStoreRequest(request)) {

			String storeIdParam = request.getParameter(FORM_STORE_ID);
			int storeId = Integer.parseInt(storeIdParam);

			System.out.println("isGetStoreRequest: "
					+ isGetStoreRequest(request) + " storeId param: "
					+ request.getParameter(FORM_STORE_ID));

			Store store = this.getStoreByStoreId(storeId);

			this.printHtmlUpdateFormForStore(store, out);

			List<Item> items = this.getItemsByStoreId(storeId);
			this.printHtmlTableOfItemsPerStore(items, out);

			this.printHtmlDeleteFormForStore(store, out);
			this.printHtmlFormForNewItem(storeId, out);

			//this.printHtmlLinkForStore(store, out);
			this.printHtmlLinkForAllStores(out);
		}

		else if (isGetItemRequest(request)) {

			int itemId = Integer.parseInt(request.getParameter(FORM_ITEM_ID));
			System.out.println("isGetItemRequest: " + isGetItemRequest(request)
					+ ", with itemId param ="
					+ request.getParameter(FORM_ITEM_ID));

			Item item = this.getItemByItemId(itemId);
			System.out.println("Item Id is: " + itemId);

			int storeId = item.getStoreId();
			System.out.println("store id is: " + storeId);

			this.printHtmlUpdateFormForItem(item, out);
			this.printHtmlDeleteFormForItem(item, out);

			Store store = this.getStoreByStoreId(storeId);
			this.printHtmlLinkForStore(store, out);
		}

		out.println("<br/>");
		out.println("Servlet created on: " + new Date());
		out.println("</body></html>");
	}

	private Store getStoreByStoreName(String name) {
		return this.storesDAO.getStoreByStoreName(name);
	}

	private boolean isListAllStoresRequest(HttpServletRequest request) {
	String actionCode= request.getParameter(ACTION_CODE_PARAM_NAME);
	return  (actionCode == null) || (actionCode.equals(ACTION_CODE_FOUR));
		
	}

	private boolean isGetStoreRequest(HttpServletRequest request) {
		String actionName = request.getParameter(ACTION_CODE_PARAM_NAME);
		return (actionName.equals(ACTION_CODE_FIVE));
					
	}

	private boolean isGetItemRequest(HttpServletRequest request) {
		String actionName = request.getParameter(ACTION_CODE_PARAM_NAME);
		return actionName.equals(ACTION_CODE_ONE);
	}

	private void printHtmlLinkForStore(Store store, PrintWriter out) {
		String urlForStore = "http://localhost:8080/ShoppingList/StoresServlet?ActionName=" + ACTION_CODE_FIVE + "&" + FORM_STORE_ID + "=" +
				 store.getStoreId();
		out.println();
		out.println("<h3><a href=\"" + urlForStore + "\" > " + store.getName()
				+ " </a></h3>");
		out.println("<br/>");

	}
	
	private void printHtmlTableForStores(List<Store> stores, PrintWriter out) {

		out.print("<hr/>");
		Date date = new Date();
		out.println("<h2>Shopping Lists as of: " + date + "</h2>");
		
		out.println("<table border=\"1\">");

		out.println("<tr>");
		out.println("<th>StoreId</th>");
		out.println("<th>Name</th>");
		out.println("</tr>");

		for (Store store : stores) {
			this.printHtmlTableRowForStore(store, out);

		}
		out.println("</table>");
	}

	private void printHtmlTableRowForStore(Store store, PrintWriter out) {
		out.println("<tr>");

		String store_url = "http://localhost:8080/ShoppingList/StoresServlet?ActionName="
				+ ACTION_CODE_FIVE + "&" + FORM_STORE_ID + "=" + store.getStoreId();

		out.println("<td><a href=\"" + store_url + "\" >" + store.getStoreId()
				+ "</a></td>");
		out.println("<td>" + store.getName() + "</td>");
		out.println("</tr>");

	}

	private void printHtmlUpdateFormForItem(Item item, PrintWriter out) {

		out.println("<br/>");
		String url = "http://localhost:8080/ShoppingList/StoresServlet";
		out.println("<form action =\"" + url + "\" method = \"post\" >");

		out.println("<h3>Item Id: <input type=\"text\" name=\"" + FORM_ITEM_ID
				+ "\" value=\"" + item.getItemId() + "\" readonly /></h3>");

		out.println("<h3>Item Name: <input type=\"text\" name=\""
				+ FORM_ITEM_NAME + "\" value=\"" + item.getName()
				+ "\" /></h3>");

		out.println("<h3>Category: <input type=\"text\" name=\""
				+ FORM_ITEM_CATEGORY + "\" value=\"" + item.getCategory()
				+ "\" /></h3>");

		out.println("<h3>Quantity: <input type=\"text\" name=\""
				+ FORM_ITEM_QUANTITY + "\" value=\"" + item.getQuantity()
				+ "\" /></h3>");
		out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID
				+ "\" value=\"" + item.getStoreId() + "\" />");

		out.println("<h3>Is Bought: <input type=\"text\" name=\""
				+ FORM_ITEM_ISBOUGHT + "\" value=\"" + item.getIsBought()
				+ "\" /></h3>");
		out.println("<input type=\"hidden\" name=\""
				+ ACTION_CODE_PARAM_NAME + "\" value=\""
				+ ACTION_CODE_UPDATE_ITEM + "\" />");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Update\">");
		out.println("<br/>");
		out.println("</form>");
	}

	private void printHtmlDeleteFormForItem(Item item, PrintWriter out) {
		out.println("<br/>");
		String urlForItems = "http://localhost:8080/ShoppingList/StoresServlet";
		out.println("<form action =\"" + urlForItems + "\" method = \"post\" >");
		out.println("<input type=\"hidden\" name=\"" + FORM_ITEM_ID
				+ "\" value=\"" + item.getItemId() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\"" + FORM_ITEM_CATEGORY
				+ "\" value=\"" + item.getCategory() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\"" + FORM_ITEM_QUANTITY
				+ "\" value=\"" + item.getQuantity() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID
				+ "\" value=\"" + item.getStoreId() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\"" + FORM_ITEM_ISBOUGHT
				+ "\" value=\"" + item.getIsBought() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\""
				+ ACTION_CODE_PARAM_NAME + "\" value=\""
				+ ACTION_CODE_DELETE_ITEM +  "\" />");
		out.println("<input type=\"submit\" value=\"Delete\">");
		out.println("</form>");

	}

	private void printHtmlFormForNewItem(int storeId, PrintWriter out) {

		String urlForItems = "http://localhost:8080/ShoppingList/StoresServlet";
		out.println("<br/>");
		out.println("<form action =\"" + urlForItems + "\" method = \"post\" >");
		out.println("Item Name: <input type=\"text\" name=\"" + FORM_ITEM_NAME
				+ "\" value=\"\" />");
		out.println("Category: <input type=\"text\" name=\""
				+ FORM_ITEM_CATEGORY + "\" value=\"\" />");
		out.println("Quantity: <input type=\"text\" name=\""
				+ FORM_ITEM_QUANTITY + "\" value=\"\" />");
		out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID + "\" value=\"" + storeId + "\" readonly />");
		out.println("Is Bought: <input type=\"text\" name=\""	+ FORM_ITEM_ISBOUGHT + "\" value=\"\" />");
		out.println("<input type=\"hidden\" name=\"" + ACTION_CODE_PARAM_NAME + "\" value=\"" + ACTION_CODE_CREATE_ITEM + "\" readonly />");	
		// printHtmlDropDownListOfStores(stores, out);
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");

	}

	/*
	 * private void printHtmlDropDownListOfStores(List<Store> stores,
	 * PrintWriter out) { out.println("<select name=\"" + FORM_STORE_ID +
	 * "\">"); out.println("<option value=\"3\">Trader Joe</option>");
	 * out.println("<option value=\"4\">Shop Rite</option>");
	 * out.println("<option value=\"5\">Costco</option>");
	 * out.println("<option value=\"6\">Piast</option>");
	 * out.println("</select>"); }
	 */

	private void printHtmlFormForNewStore(PrintWriter out) {
		out.println("<br/>");
		out.println("<h3>Enter new store</h3>");
		String urlForStores = "http://localhost:8080/ShoppingList/StoresServlet";
		out.println("<form action =\"" + urlForStores
				+ "\" method = \"post\" >");
		// out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID
		// + "\" value=\"" + storeId + "\" readonly />");
		out.println("Store name: <input type=\"text\" name=\""
				+ FORM_STORE_NAME + "\" value=\"\" />");
		out.println("<input type=\"hidden\" name=\"" +	ACTION_CODE_PARAM_NAME
		+ "\" value=\""+ ACTION_CODE_CREATE_STORE  + "\" readonly />");
		out.println("<br>");
		out.println("<br>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("<P/>");
	} 

	private void printHtmlTableOfItemsPerStore(List<Item> items, PrintWriter out) {
		out.println("<br/>");
		out.println("<h3>List of Items</h3>");
		out.println("<br/>");
		out.println("<table border=\"1\">");

		out.println("<tr>");
		out.println("<th>ItemId</th>");
		out.println("<th>Name</th>");
		out.println("<th>Category</th>");
		out.println("<th>Quantity</th>");
		out.println("<th>IsBought</th>");
		out.println("</tr>");

		for (Item item : items) {
			this.printHtmlTableRowForItem(item, out);
		}
		out.println("</table>");

	}

	private void printHtmlTableRowForItem(Item item, PrintWriter out) {
		out.println("<tr>");
		String item_url = "http://localhost:8080/ShoppingList/StoresServlet?" + ACTION_CODE_PARAM_NAME + "=" + ACTION_CODE_ONE + "&"+ FORM_ITEM_ID +"=" + item.getItemId();
		out.println("<td><a href=\"" + item_url + "\" >" + item.getItemId()
				+ "</a></td>");
		out.println("<td>" + item.getName() + "</td>");
		out.println("<td>" + item.getCategory() + "</td>");
		out.println("<td>" + item.getQuantity() + "</td>");
		out.println("<td>" + item.isBought() + "</td>");
		out.println("</tr>");

	}

	private void printHtmlLinkForAllStores(PrintWriter out) {
		String urlForAllStores = "http://localhost:8080/ShoppingList/StoresServlet?ActionName=GetAllStores";
		out.println("<br>");
		out.println();
		out.println("<a href=\"" + urlForAllStores + "\" >All Stores</a>");
		out.println("<br>");
	}

	private void printHtmlUpdateFormForStore(Store store, PrintWriter out) {
		out.println("<br/>");
		String urlForStores = "http://localhost:8080/ShoppingList/StoresServlet";

		Date date = new Date();
		out.println("<h2>List as of " + date + "</h2>");

		out.println("<form action =\"" + urlForStores
				+ "\" method = \"post\" >");
		out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID
				+ "\" value=\"" + store.getStoreId() + "\" readonly />");
		out.println("<br>");
		out.println("Store name: <input type=\"text\" name=\""
				+ FORM_STORE_NAME + "\" value=\"" + store.getName() + "\" />");
		out.println("<input type=\"hidden\" name=\""
				+ ACTION_CODE_PARAM_NAME + "\" value=\""
				+ ACTION_CODE_UPDATE_STORE + "\" readonly />");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Update\">");
		out.println("<br/>");
		out.println("</form>");
	}

	private void printHtmlDeleteFormForStore(Store store, PrintWriter out) {
		out.println("<br/>");
		String urlForStores = "http://localhost:8080/ShoppingList/StoresServlet";
		out.println("<form action =\"" + urlForStores
				+ "\" method = \"post\" >");
		out.println("<input type=\"hidden\" name=\"" + FORM_STORE_ID
				+ "\" value=\"" + store.getStoreId() + "\" readonly />");
		out.println("<input type=\"hidden\" name=\""
				+ ACTION_CODE_PARAM_NAME + "\" value=\""
				+ ACTION_CODE_DELETE_STORE  +  "\" />");
		out.println("<input type=\"submit\" value=\"Delete Store\">");
		out.println("<br/>");
		out.println("</form>");
	}

	
	private List<Item> getItemsByStoreId(int storeId) {
		return this.storesDAO.getItemsByStoreId(storeId);
	}
	
	private List<Store> getAllStores() {
		return this.storesDAO.getAllStores();
	}

	private Store getStoreByStoreId(int storeId) {
		return this.storesDAO.getStoreById(storeId);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String storeNameParam = request.getParameter(FORM_STORE_NAME);
		String storeIdParam = request.getParameter(FORM_STORE_ID);

		Store store = new Store();

		if (storeIdParam != null) {
			int storeId = Integer.parseInt(storeIdParam);
			store.setStoreId(storeId);
		}

		if (isCreateStoreRequest(request)) {

			store.setName(storeNameParam);

			System.out.println("Creating store: "
					+ isCreateStoreRequest(request) + " " + store);

			int createdStoreId = createNewStore(store);
			System.out.println("createdStore: StoreId=" + createdStoreId);

			store.setStoreId(createdStoreId);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?"
					+ ACTION_CODE_PARAM_NAME + "=" + ACTION_CODE_FOUR;

			System.out.println("Redirect to: " + redirectUrl);

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);

		} else if (isUpdateStoreRequest(request)) {

			store.setName(storeNameParam);

			this.storesDAO.updateStore(store);

			System.out.println("Update store:" + isUpdateStoreRequest(request)
					+ " " + store);

			updateStore(store);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?ActionName=" + ACTION_CODE_FIVE + "&" + FORM_STORE_ID + "="
					+ store.getStoreId();

			System.out.println("Redirect to: " + redirectUrl);

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);

		} else if (isDeleteStoreRequest(request)) {

			System.out.println("problem maybe here?");
			int storeId = Integer.parseInt(request.getParameter(FORM_STORE_ID));

			System.out.println("Delete store by storeId: "
					+ isDeleteStoreRequest(request) + " " + storeId);

			// int storeIdParameter = store.getStoreId();

			// storesDAO.getStoreById(storeIdParameter);
			this.storesDAO.deleteStoreById(storeId);

			System.out.println("take two: Delete store by storeId: "
					+ isDeleteStoreRequest(request) + " " + storeId);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?"
					+ ACTION_CODE_PARAM_NAME + "=" + ACTION_CODE_FOUR;

			System.out.println("Redirect to: " + redirectUrl);

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);
		}

		String itemNameParam = request.getParameter(FORM_ITEM_NAME);
		String itemIdParam = request.getParameter(FORM_ITEM_ID);
		String itemCategoryParam = request.getParameter(FORM_ITEM_CATEGORY);
		String storeIdParameter = request.getParameter(FORM_STORE_ID);
		String itemQuantityParam = request.getParameter(FORM_ITEM_QUANTITY);
		String itemIsBoughtParam = request.getParameter(FORM_ITEM_ISBOUGHT);

		Item item = new Item();
		item.setName(itemNameParam);
		item.setCategory(itemCategoryParam);

		if (storeIdParameter != null) {
			int storeId = Integer.parseInt(storeIdParameter);
			item.setStoreId(storeId);
		}

		if (itemQuantityParam != null) {
			int itemQuantity = Integer.parseInt(itemQuantityParam);
			item.setQuantity(itemQuantity);
		}

		if (itemIsBoughtParam != null) {
			boolean isBoughtParam = Boolean.parseBoolean(itemIsBoughtParam);
			item.setBought(isBoughtParam);
		}

		if (itemIdParam != null) {
			int itemId = Integer.parseInt(itemIdParam);
			item.setItemId(itemId);
		}

		if (isCreateItemRequest(request)) {

			System.out.println("createItem: " + isCreateItemRequest(request)
					+ " " + item);

			int createdItemId = createNewItemForStore(item);

			System.out.println("createdItem: ItemId=" + createdItemId);

			item.setItemId(createdItemId);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?" + ACTION_CODE_PARAM_NAME + "=" + ACTION_CODE_ONE + "&" + FORM_ITEM_ID + "=" + item.getItemId();

			System.out.println("Redirect to: " + redirectUrl);

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);

		} else if (isUpdateItemRequest(request)) {

			item.setName(itemNameParam);
			this.storesDAO.updateItem(item);

			System.out.println("updateItem: " + isUpdateItemRequest(request)
					+ " " + item);

			updateItem(item);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?ActionName=" + ACTION_CODE_ONE + "&" + FORM_ITEM_ID + "="
					+  item.getItemId();

			System.out.println("Redirect to: " + redirectUrl);

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);

		} else if (isDeleteItemRequest(request)) {
			System.out.println("or maybe problem here?");
			int itemId = Integer.parseInt(request.getParameter(FORM_ITEM_ID));
			System.out.println("isDeleteItemRequest: "
					+ isDeleteItemRequest(request) + " " + itemIdParam);

			// int itemIdParameter = item.getItemId();
			// this.storesDAO.getItemById(itemIdParameter);
			System.out.println("Finally maybe problem here?");
			storesDAO.deleteItemById(itemId);

			String redirectUrl = "http://localhost:8080/ShoppingList/StoresServlet?"
			+ ACTION_CODE_PARAM_NAME + "=" + ACTION_CODE_FIVE + "&" + FORM_STORE_ID + "=" + storeIdParam;

			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);

		}
	}

	private boolean isCreateStoreRequest(HttpServletRequest request) {

		String actionName = request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isCreateStore = actionName.equals(ACTION_CODE_CREATE_STORE);
		return isCreateStore;
	}

	private boolean isUpdateStoreRequest(HttpServletRequest request) {

		String actionName= request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isUpdateStore = actionName.equals(ACTION_CODE_UPDATE_STORE);
		return isUpdateStore;
	}

	private boolean isDeleteStoreRequest(HttpServletRequest request) {

		String actionName = request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isDeleteStore = actionName.equals(ACTION_CODE_DELETE_STORE);
		return isDeleteStore;

	}

	private boolean isCreateItemRequest(HttpServletRequest request) {

		String actionName = request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isCreateItem = actionName.equals(ACTION_CODE_CREATE_ITEM);
		return isCreateItem;

	}

	private boolean isUpdateItemRequest(HttpServletRequest request) {

		String actionName = request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isUpdateItem = (actionName.equals(ACTION_CODE_UPDATE_ITEM));

		return isUpdateItem;
	}

	private boolean isDeleteItemRequest(HttpServletRequest request) {

		String actionName= request
				.getParameter(ACTION_CODE_PARAM_NAME);

		boolean isDeleteItem = actionName.equals(ACTION_CODE_DELETE_ITEM);

		return isDeleteItem;
	}

	private void updateStore(Store store) {
		this.storesDAO.updateStore(store);
	}

	private void updateItem(Item item) {
		this.storesDAO.updateItem(item);
	}

	private int createNewItemForStore(Item item) {
		int itemId = this.storesDAO.createNewItemForStore(item);
		return itemId;
	}

	private Item getItemByItemId(int itemId) {
		return this.storesDAO.getItemById(itemId);
	}

	private int createNewStore(Store store) {
		return this.storesDAO.createNewStore(store);
	}

	private StoresDAO storesDAO;

}
