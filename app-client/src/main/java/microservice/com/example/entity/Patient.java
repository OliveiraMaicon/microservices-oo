package microservice.com.example.entity;

/**
 * Created by maiconoliveira on 13/08/16.
 */
public class Patient {

    private Long id;

    private String name;

    public Patient() {
    }

    public Patient(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nome='" + name + '\'' +
                '}';
    }
}
