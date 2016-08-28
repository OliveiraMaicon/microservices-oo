package com.br.oor.model.form;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Created by maiconoliveira on 14/03/16.
 */
public class EventSearchForm {

    private Long id;

    private String fieldSearch;

    private String address;

    private String city;

    private LocalDateTime date;

    private String nome;


    public EventSearchForm(Long id, String nome, String address, String city){

        this.id = id;
        this.nome = nome;
        this.address = address;
        this.city = city;

    }

    public EventSearchForm(String fieldSearch, LocalDateTime date){

        this.fieldSearch = fieldSearch;
        this.date = date;

    }

/*
    public Predicate buildSearch(){

        BooleanBuilder builder = new BooleanBuilder();

        QEvent qEvent = QEvent.event;

        if(StringUtils.isEmpty(getFieldSearch())){

            builder.and(qEvent.name.like(getFieldSearch()));
            builder.and(qEvent.city.like(getFieldSearch()));
            builder.and(qEvent.address.like(getFieldSearch()));
        }

        if(getDate() != null){
            builder.and(qEvent.date.eq(getDate()));
        }
        return builder;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFieldSearch() {
        return fieldSearch;
    }

    public void setFieldSearch(String fieldSearch) {
        this.fieldSearch = fieldSearch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
