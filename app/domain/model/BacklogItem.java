package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

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
        
    public final void add() throws TeamNameAlreadyTakenException {
        // TODO: validation project
        save();
	}

	public static Optional<BacklogItem> forId(final Long backlogItemId) {
		return Optional.fromNullable(find.byId(backlogItemId));
	}}