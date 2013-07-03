package org.tai.cops.occi.cords.concepts;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.annotation.Nonnull;

import org.tai.cops.occi.annotations.Attribute;
import org.tai.cops.occi.annotations.Transformations;
import org.tai.cops.occi.client.Kind;
import org.tai.cops.occi.client.Resource;
import org.tai.cops.occi.client.TypeIdentifier;

public class Publication extends Resource {
	
	@Attribute(name = "occi.publication.remote")
	private String remote;
	@Attribute(name = "occi.publication.what")
	private String what;
	@Attribute(name = "occi.publication.where")
	private String where;
	@Attribute(name = "occi.publication.why", trans = Transformations.StringToUrl.class)
	private URL why;
	@Attribute(name = "occi.publication.when", trans = Transformations.StringToInteger.class)
	private Integer when;
	@Attribute(name = "occi.publication.uptime", trans = Transformations.StringToInteger.class)
	private Integer uptime;
	@Attribute(name = "occi.publication.who")
	private String who;
	@Attribute(name = "occi.publication.pass")
	private String pass;
	@Attribute(name = "occi.publication.identity")
	private String identity;
	@Attribute(name = "occi.publication.zone")
	private String zone;
	//<instance name='price' category='http://scheme.compatibleone.fr/scheme/compatible#price' multiplicity='[0..1]'/>
	@Attribute(name = "occi.publication.rating")
	private String rating;
	//<instance name='operator' category='http://scheme.compatibleone.fr/scheme/compatible#operator' multiplicity='[0..1]'/>
	@Attribute(name = "occi.publication.pid", trans = Transformations.StringToInteger.class)
	private Integer pid;
	@Attribute(name = "occi.publication.state", trans = Transformations.StringToInteger.class)
	private Integer state;


	protected Publication(@Nonnull TypeIdentifier kind, @Nonnull URI id, String title, String summary) {
		super(kind, id, title, summary);
	}
	
	public Publication(@Nonnull TypeIdentifier kind, Map<String, String> attributes) throws URISyntaxException {
		super(kind, attributes);
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public URL getWhy() {
		return why;
	}

	public void setWhy(URL why) {
		this.why = why;
	}

	public Integer getWhen() {
		return when;
	}

	public void setWhen(Integer when) {
		this.when = when;
	}

	public Integer getUptime() {
		return uptime;
	}

	public void setUptime(Integer uptime) {
		this.uptime = uptime;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
