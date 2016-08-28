package com.br.oor.enuns;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by maiconoliveira on 14/03/16.
 */
public enum State {

    ACRE("Acre", "AC"),

    ALAGOAS("Alagoas", "AL"),

    AMAPA("Amapá", "AP"),

    AMAZONAS("Amazonas", "AM"),

    BAHIA("Bahia", "BA"),

    CEARA("Ceará", "CE"),

    DISTRITOFEDERAL("Distrito Federal", "DF"),

    ESPIRITOSANTO("Espírito Santo", "ES"),

    GOIAS("Goiás", "GO"),

    MARANHAO("Maranhão", "MA"),

    MATOGROSSO("Mato Grosso", "MT"),

    MATOGROSSODOSUL("Mato Grosso do Sul", "MS"),

    MINASGERAIS("Minas Gerais", "MG"),

    PARA("Pará", "PA"),

    PARAIBA("Paraíba", "PB"),


    PARANA("Paraná", "PR"),

    PERNAMBUCO("Pernambuco", "PE"),

    PIAUI("Piauí", "PI"),

    RIODEJANEIRO("Rio de Janeiro", "RJ"),

    RIOGRANDEDONORTE("Rio Grande do Norte", "RN"),

    RIOGRANDEDOSUL("Rio Grande do Sul","RS"),

    RONDONIA("Rondônia", "RO"),

    RORAIMA("Roraima", "RR"),

    SANTACATARINA("Santa Catarina", "SC"),

    SAOPAULO("São Paulo", "SP"),

    SERGIPE("Sergipe", "SE"),

    TOCANTINS("Tocantins", "TO");

    private String nome;
    private String sigla;

    private State(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    public static Map<String,String> getValuesMap(){

        Map<String,String> cities = Arrays.stream(State.values()).collect(Collectors.toMap(State::getSigla,State::getNome));

        Comparator<Map.Entry<String, String>> comparator = (entry1, entry2) -> entry1.getKey().compareTo(
                entry2.getKey());

        return cities.entrySet().stream()
                .sorted(comparator).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry:: getValue, (e1,e2) -> e1, LinkedHashMap::new));
    }

}
