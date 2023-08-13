package model;

public class Book {

	private int id;
	private String name;
	private String author;
	private int genreId;
	private String genreName;
	private int stock;
	private int price;
	public Book(int id, String name, String author, int genreId, String genreName, int stock, int price) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.genreId = genreId;
		this.genreName = genreName;
		this.stock = stock;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getGenreId() {
		return genreId;
	}
	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	

}
