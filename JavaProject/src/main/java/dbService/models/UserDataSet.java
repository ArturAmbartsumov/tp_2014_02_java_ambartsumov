package dbService.models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by artur on 11.03.14.
 */
@Entity  //@Entity говорит о том что этот объект будет обрабатываться hibernate
@Table(name = "user", schema = "", catalog = "Java_db")
public class UserDataSet {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private Timestamp createTime;

    public UserDataSet() {

    }

    public UserDataSet(String username,
                       String email,
                       String password) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //как свою функцию передать для генерации
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic // @Basic allows to define that a single field is not loaded immediately but lazy when the getter is called for this field
    @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != "") {
            this.username = username;
        } else this.username = null;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != "") {
            this.email = email;
        } else this.email = null;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != "") {
            this.password = password;
        } else this.password = null;
    }

    @Basic
    @Column(name = "create_time", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataSet that = (UserDataSet) o;

        if (id != that.id) return false;

        return true;
    }

    @Override //переопределяет метод в родителе
    public int hashCode() {
        return id;
    }
}
