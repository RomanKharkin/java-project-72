package hexlet.code.domain;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public final class Url extends Model {
    @Id
    long id;

    @WhenCreated
    private Date createdAt;

    String name;

    @OneToMany(mappedBy = "url")
    List<UrlCheck> urlChecks;

    public Url(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UrlCheck> getUrlChecks() {
        return urlChecks;
    }

    public void setUrlChecks(List<UrlCheck> urlChecks) {
        this.urlChecks = urlChecks;
    }

    public UrlCheck getLastCheck() {
        return urlChecks.get(urlChecks.size() - 1);
    }
}
