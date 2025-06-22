public class Task {
    private final int id;
    private String name;
    private String description;
    private boolean isDone;

    public Task(int id, String name, String description, boolean isDone){
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDone = isDone;
    }

    // Gettery
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }

    // Settery
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDone(boolean done) { isDone = done; }

}
