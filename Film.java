package edu.csuft.liuyi.spider;

/**
 * ���ע���ֶ� 
 */
public class Film 
{
	int id;//����
	String title;//Ƭ��
	String poster;//����
	String info;
	String director;//����
	String mainactor;//����
	String country;//����
	double star;//����
	int rating;//��������
	String quote;//��Ҫ
	int year;//��ӳ���
	
	
	public Film() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getStar() {
		return star;
	}

	public void setStar(double d) {
		this.star = d;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int i) {
		this.rating = i;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director){
		this.director = director;
	}
	public String getMainActor() {
		return mainactor;
	}
	
	public void setMainActor(String mainactor){
		this.mainactor = mainactor;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int i){
		this.year = i;
	}
	
	@Override
	public String toString() 
	{
		return "Film [id="          + id + 
				", title="          + title + 
				", director="       + director +
				", mainactor="      + mainactor + 
				", year="           + year +
				", country="        + country +
				", star="           + star + 
				", rating="         + rating+ 
				", poster="         + poster + 
				", quote="          + quote + 
				"]";
	}		
}
