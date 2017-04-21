package entities.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import entities.users.Encrypting;
import entities.users.User;

@Entity
public class Ticket {

    @Id
    private long id;

    private Calendar created;

    @Column(unique = true, nullable = false)
    private String reference;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Shopping> shoppingList;

    @ManyToOne
    @JoinColumn
    private User user;

    public Ticket() {
        created = Calendar.getInstance();
        shoppingList = new ArrayList<>();
    }

    public Ticket(long id) {
        this();
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        updateReference();
    }
    
    private void updateReference() {
        reference = new Encrypting().encryptInBase64UrlSafe("" + this.getId() + Long.toString(new Date().getTime()));
    }

    public void addShopping(Shopping shopping) {
        shoppingList.add(shopping);
    }

    public List<Shopping> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Shopping> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getCreated() {
        return created;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return id == ((Ticket) obj).id;
    }

    @Override
    public String toString() {
        String createTime = new SimpleDateFormat("HH:mm dd-MMM-yyyy ").format(created.getTime());
        return "Ticket[" + id + ": created=" + createTime + ", shoppingList=" + shoppingList + ", userId="
                + user.getId() + "]";
    }

}
