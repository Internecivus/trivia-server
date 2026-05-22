package com.trivia.persistence.entity;

import com.trivia.persistence.EntityView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;



@Entity
@NamedEntityGraph(
    name = EntityView.Name.CLIENT_DETAILS,
    attributeNodes = @NamedAttributeNode(value = "user")
)
@Table(name = "client", schema = "Trivia")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @NotBlank
    @Column(name = "api_key")
    private String apiKey;

    @Basic
    @NotBlank
    @Column(name = "api_secret")
    private String apiSecret;

    @Basic
    @NotNull
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)// insertable nullable
    private User user;

    public Client() {}

    public Client(Client other) {
        this.apiKey = other.apiKey;
        this.apiSecret = other.apiSecret;
        this.dateCreated = other.dateCreated;
        //this.user = other.user;
    }

    @PrePersist
    public void preCreate() {
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = new Timestamp(dateCreated.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
            Objects.equals(apiKey, client.apiKey) &&
            Objects.equals(apiSecret, client.apiSecret) &&
            Objects.equals(dateCreated, client.dateCreated) &&
            Objects.equals(user, client.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, apiKey, apiSecret, dateCreated, user);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", apiKey='" + apiKey + '\'' +
            ", apiSecret='" + apiSecret + '\'' +
            ", dateCreated=" + dateCreated +
            ", user=" + user +
            '}';
    }
}