package org.tai.cops.concepts;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.tai.cops.occi.annotations.Attribute;
import org.tai.cops.occi.annotations.Transformations;
import org.tai.cops.occi.client.Resource;
import org.tai.cops.occi.client.TypeIdentifier;



@javax.persistence.Entity
public class Placement extends Resource {
	
	public static final TypeIdentifier identifyBy =
			new TypeIdentifier(URI.create("http://scheme.compatibleone.fr/scheme/compatible#placement")); 
	
	
	@Column
	@Attribute(name = "occi.placement.name")
	private String name;
	@Column
	@Attribute(name = "occi.placement.account", trans = Transformations.StringToUrl.class)
	private URL account;
	@Column
	@Attribute(name = "occi.placement.algorithm", trans = Transformations.StringToUrl.class)
	private URL algorithm;
	@Column
	@Attribute(name = "occi.placement.node", trans = Transformations.StringToUrl.class)
	private URL node;
	@Column
	@Attribute(name = "occi.placement.provider", trans = Transformations.StringToUrl.class)
	private URL provider;
	@Column
	@Attribute(name = "occi.placement.price", trans = Transformations.StringToUrl.class)
	private URL price;
	@Column
	@Attribute(name = "occi.placement.opinion")
	private String opinion;
	@Column
	@Attribute(name = "occi.placement.zone")
	private String zone;
	@Column
	@Attribute(name = "occi.placement.security", trans = Transformations.StringToUrl.class)
	private URL security;
	@Column
	@Attribute(name = "occi.placement.operator", trans = Transformations.StringToUrl.class)
	private URL operator;
	@Column
	@Attribute(name = "occi.placement.solution")
	private String solution;
	@Column
	@Attribute(name = "occi.placement.energy")
	private String energy;
	@Column
	@Attribute(name = "occi.placement.state", trans = Transformations.StringToInteger.class)
	private Integer state;
	
	@Transient
	private Set<URL> quantities;
	

	public Placement(@Nonnull TypeIdentifier kind, @Nonnull URI id, String title, String summary, String name) {
		super(kind, id, title, summary);
		this.name = name;
	}
	
	public Placement(@Nonnull TypeIdentifier kind, Map<String, String> attributes) throws URISyntaxException {
		super(kind, attributes);
	}



	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public URL getAccount() {
		return account;
	}


	public void setAccount(URL account) {
		this.account = account;
	}


	public URL getAlgorithm() {
		return algorithm;
	}


	public void setAlgorithm(URL algorithm) {
		this.algorithm = algorithm;
	}


	public URL getNode() {
		return node;
	}


	public void setNode(URL node) {
		this.node = node;
	}


	public URL getProvider() {
		return provider;
	}


	public void setProvider(URL provider) {
		this.provider = provider;
	}


	public URL getPrice() {
		return price;
	}


	public void setPrice(URL price) {
		this.price = price;
	}


	public String getOpinion() {
		return opinion;
	}


	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}


	public String getZone() {
		return zone;
	}


	public void setZone(String zone) {
		this.zone = zone;
	}


	public URL getSecurity() {
		return security;
	}


	public void setSecurity(URL security) {
		this.security = security;
	}


	public URL getOperator() {
		return operator;
	}


	public void setOperator(URL operator) {
		this.operator = operator;
	}


	public String getSolution() {
		return solution;
	}


	public void setSolution(String solution) {
		this.solution = solution;
	}


	public String getEnergy() {
		return energy;
	}


	public void setEnergy(String energy) {
		this.energy = energy;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public Set<URL> getQuantities() {
		return quantities;
	}


	public void setQuantities(Set<URL> quantities) {
		this.quantities = quantities;
	}

}
