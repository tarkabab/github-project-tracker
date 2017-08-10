package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

import java.util.List;
import java.util.ArrayList;

import domain.model.Comment;

@Entity
public class Task extends Model {

    @Id
    public long id;
    
    @Constraints.Required
	@Formats.NonEmpty
    public String name;
    
    public String description;
    
    @Constraints.Required
	@Formats.NonEmpty
    public long backlogItemId;
    
    public TaskStatus githubStatus = TaskStatus.valueOf("OPEN");
    
    public String githubUrl = "";
    
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Comment> githubComments = new ArrayList();

    public final static Finder<Long, Task> find = new Finder<Long, Task>(Long.class, Task.class);

	public static Optional<Task> forId(final Long taskId) {
		return Optional.fromNullable(find.byId(taskId));
	}

    public static List<Task> forBacklogItem(final Long backlogItemId) {

		return find.where().eq("backlogItemId", backlogItemId).findList();
	}

}