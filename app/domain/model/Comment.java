package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

@Entity
public class Comment extends Model {

    @Id
    public long id;
    public String login;
    public String body;
    public String url;
}