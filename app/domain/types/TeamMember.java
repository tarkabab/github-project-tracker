package domain.types;

public class TeamMember {
    public String identity;
    public String email;
    public String name;

    public TeamMember(String identity, String email, String name) {
        this.identity = identity;
        this.email = email;
        this.name = name;
    }
}