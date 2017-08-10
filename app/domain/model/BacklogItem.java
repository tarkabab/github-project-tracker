package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

import java.util.List;

import domain.types.BacklogItemType;
import domain.types.BacklogItemStatus;
import domain.types.Priority;

@Entity
public class BacklogItem extends Model {

    @Id
    public Long id;
	@Constraints.Required
	@Formats.NonEmpty
    public String name;
    public String summary;
    public BacklogItemType itemType;
    public int storyPoints;
    public Priority priority;
    public BacklogItemStatus status;
    public int tasks;
    public long projectId;

    public final static Finder<Long, BacklogItem> find = new Finder<Long, BacklogItem>(Long.class, BacklogItem.class);
        
	public static Optional<BacklogItem> forId(final Long backlogItemId) {
		return Optional.fromNullable(find.byId(backlogItemId));
	}

    public static List<BacklogItem> forProject(final Long projectId) {

		return find.where().eq("projectId", projectId).findList();
	}
}