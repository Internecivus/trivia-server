package com.trivia.persistence.entity;

import com.trivia.persistence.EntityView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;



// TODO: We are mixing logic in User since it is acting as both a "normal" user and a provider. A separate provider
// table/entity should be made.
@Entity
@NamedEntityGraph(
    name = EntityView.Name.USER_DETAILS,
    attributeNodes = @NamedAttributeNode(value = "roles")
)
@Table(name = "user", schema = "Trivia")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "password")
    @NotBlank(message = "{field.required}")
    @Size(min = 6, max = 20, message = "{field.length}")
    private String password;

    @Basic
    @Column(name = "name")
    @NotBlank(message = "{field.required}")
    @Size(min = 5, max = 20, message = "{field.length}")
    private String name;

    @Basic
    @NotBlank
    @Column(name = "provider_key")
    private String providerKey;

    @Basic
    @NotBlank
    @Column(name = "provider_secret")
    private String providerSecret;

    @Basic
    @NotNull
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @OrderBy(Role_.ID + " ASC")
    @NotEmpty(message = "{collection.required}")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role_map",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Client> clients = new HashSet<>();

    public User() {}

    public User(User other) {
        this.password = other.password;
        this.name = other.name;
        this.providerKey = other.providerKey;
        this.providerSecret = other.providerSecret;
        this.dateCreated = other.dateCreated;
        this.roles = other.roles;
    }

    @PrePersist
    public void preCreate() {
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }


    public Set<Question> getQuestions() {
        return questions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = new Timestamp(dateCreated.getTime());
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getProviderSecret() {
        return providerSecret;
    }

    public void setProviderSecret(String providerSecret) {
        this.providerSecret = providerSecret;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<String> getRolesAsStrings() {
        return getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
    }

    public Set<RoleType> getRoleTypes() {
        return getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
    }

    public boolean hasRole(RoleType roleType) {
        return getRoleTypes().contains(roleType);
    }

    public boolean hasOneOfRoles(RoleType... roleTypes) {
        return getRoleTypes().stream().anyMatch(Arrays.asList(roleTypes)::contains);
    }

    public boolean isOwnerOf(Client client) {
        return client.getUser().getId().equals(this.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(password, user.password) &&
            Objects.equals(name, user.name) &&
            Objects.equals(providerKey, user.providerKey) &&
            Objects.equals(providerSecret, user.providerSecret) &&
            Objects.equals(dateCreated, user.dateCreated) &&
            Objects.equals(roles, user.roles) &&
            Objects.equals(questions, user.questions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, password, name, providerKey, providerSecret, dateCreated, roles, questions);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", providerKey='" + providerKey + '\'' +
            ", providerSecret='" + providerSecret + '\'' +
            ", dateCreated=" + dateCreated +
            ", roles=" + roles +
            ", questions=" + questions +
            '}';
    }
}