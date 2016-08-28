package com.br.oor.model;

import com.br.oor.util.MessageUtil;
import org.hibernate.validator.constraints.Email;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maiconoliveira on 28/10/15.
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"email"}))
public class Login implements Serializable {

    private static final long serialVersionUID = -9131260663755752336L;


    @Id
    @SequenceGenerator(sequenceName = "sequence_login", name="seq_login_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "seq_login_generator")
    @Column(nullable = false)
    private Long id;

    @NotNull(message = MessageUtil.EMAIL_REQUIRED)
    @Email(message = MessageUtil.INVALID_EMAIL)
    private String email;

    @NotNull
    private String password;

    private Date createDate;

    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateValues(Login login){
        boolean update = false;
        if(!StringUtils.isEmpty(login.getEmail())){
            setEmail(login.getEmail());
            update |= true;
        }

        if(!StringUtils.isEmpty(login.getPassword())){
            setPassword(login.getPassword());
            update |= true;

        }

        if(update){
            setUpdateDate(Calendar.getInstance().getTime());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login = (Login) o;

        if (id != null ? !id.equals(login.id) : login.id != null) return false;
        if (email != null ? !email.equals(login.email) : login.email != null) return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;
        if (createDate != null ? !createDate.equals(login.createDate) : login.createDate != null) return false;
        return !(updateDate != null ? !updateDate.equals(login.updateDate) : login.updateDate != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        return result;
    }
}
