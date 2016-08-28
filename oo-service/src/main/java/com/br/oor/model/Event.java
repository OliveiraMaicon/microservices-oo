package com.br.oor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by agomes on 15/01/16.
 */
@Entity
public class Event implements Serializable{
	
    private static final long serialVersionUID = 6216642371794326049L;

    @Id
    @SequenceGenerator(sequenceName = "sequence_event", name="seq_event_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "seq_event_generator")
    @Column(nullable = false)
    private Long id;

    @NotEmpty(message = "Nome obrigatório")
    @Column
    private String name;

    //@Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column
    private LocalDateTime date;

    @NotNull(message="Endereço obrigatório para um evento.")
    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    private Address address;
    
    @Version 
    private Long version;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDateTime localDateTime) {
        this.date = localDateTime;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Address getAddress() {
        return address;
    }
    public void setVersion(Long version) {
		this.version = version;
	}
    public Long getVersion() {
		return version;
	}

    @Override
    public String toString() {
    	try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
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
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}
}
