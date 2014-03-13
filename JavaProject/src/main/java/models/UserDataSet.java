package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by artur on 11.03.14.
 */
@Entity
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
                       String password) {
        setUsername(username);
        setPassword(password);
    }

    public UserDataSet(String username,
                       String email,
                       String password) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public UserDataSet(Integer id,
                       String username) {
        setId(id);
        setUsername(username);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
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
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result.intValue();
    }
}
