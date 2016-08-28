package com.br.oor.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.br.oor.util.MessageError;
import com.br.oor.util.MessageUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by agomes on 15/01/16.
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"latitude", "longitude"}))
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = MessageUtil.LATITUDE_REQUIRED)
    private Long latitude;

    @Column(nullable = false)
    @NotNull(message = MessageUtil.LONGITUDE_REQUIRED)
    private Long longitude;

    @Column(nullable = false)
    @NotNull(message = MessageUtil.ADDRESS_REQUIRED)
    private String name;

    @Column(nullable = false)
    @NotNull
    private String uf;

    @Column(nullable = false)
    @NotNull(message = MessageUtil.CITY_REQUIRED)
    private String city;
    
    @Version 
    private Long version;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, mappedBy="address")
    private List<Event> events;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public Long getLatitude() {
        return latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setVersion(Long version) {
		this.version = version;
	}
    public Long getVersion() {
		return version;
	}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
	public String toString() {
		return "Address [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", name=" + name
				+ ", version=" + version + ",uf= "+uf+", city="+city+", events=" + events + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Address other = (Address) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    
}
