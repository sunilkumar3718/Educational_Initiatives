import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Astronaut_Daily_Schedule {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScheduleManager manager = ScheduleManager.getInstance();
        manager.addObserver(new ConflictNotifier());

        System.out.println("Astronaut Daily Schedule Organizer. Type 'help' ");
        String command = "";

        while (!"exit".equalsIgnoreCase(command)) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            command = scanner.nextLine().trim();
            if (command.isEmpty()) continue;

            String action;
            String params;
            if (command.contains("|")) {
                action = command.substring(0, command.indexOf("|")).toLowerCase();
                params = command.substring(command.indexOf("|") + 1);
            } else {
                String[] parts = command.split("\\s+", 2);
                action = parts[0].toLowerCase();
                params = parts.length > 1 ? parts[1] : "";
            }

            try {
                switch (action) {
                    case "help" -> printHelp();
                    case "add" -> {
                        String[] fields = params.split("\\|");
                        if (fields.length < 4) {
                            System.out.println("Usage: add|description|HH:MM|HH:MM|priority");
                            break;
                        }
                        Task task = TaskFactory.create(fields[0], fields[1], fields[2], fields[3]);
                        if (manager.addTask(task)) System.out.println("Task added successfully. No conflicts.");
                    }
                    case "remove" -> {
                        if (params.isEmpty()) {
                            System.out.println("Usage: remove|description");
                            break;
                        }
                        boolean removed = manager.removeTask(params);
                        System.out.println(removed ? "Task removed successfully." : "Error: Task not found.");
                    }
                    case "view" -> {
                        List<Task> list = manager.listTasks();
                        if (list.isEmpty()) System.out.println("No tasks scheduled for the day.");
                        else list.forEach(System.out::println);
                    }
                    case "exit" -> {  }
                    default -> System.out.println("Unknown command. Type 'help' to see commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Exiting. Goodbye.");
        scanner.close();
    }

    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println(" add|description|HH:MM|HH:MM|priority  — add a task");
        System.out.println(" remove|description  — remove a task by description");
        System.out.println(" view    — view all tasks sorted by start time");
        System.out.println(" help    — show this help");
        System.out.println(" exit    — exit app");
    }
}

class ConflictNotifier implements TaskObserver {
    @Override
    public void onConflict(Task newTask, Task existingTask) {
        System.out.println("Error: Task conflicts with existing task \"" + existingTask.getDescription() + "\".");
    }
}

class ScheduleManager {
    private static ScheduleManager instance;
    private final List<Task> tasks = new ArrayList<>();
    private final List<TaskObserver> observers = new ArrayList<>();

    private ScheduleManager() {}

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) instance = new ScheduleManager();
        return instance;
    }

    public synchronized void addObserver(TaskObserver obs) { observers.add(obs); }

    public synchronized boolean addTask(Task task) {
        for (Task existing : tasks) {
            if (overlaps(existing, task)) {
                observers.forEach(o -> o.onConflict(task, existing));
                return false;
            }
        }
        tasks.add(task);
        return true;
    }

    public synchronized boolean removeTask(String description) {
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            if (it.next().getDescription().equalsIgnoreCase(description.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public synchronized List<Task> listTasks() {
        List<Task> copy = new ArrayList<>(tasks);
        copy.sort(Comparator.comparing(Task::getStart));
        return copy;
    }

    private boolean overlaps(Task a, Task b) {
        return a.getStart().isBefore(b.getEnd()) && b.getStart().isBefore(a.getEnd());
    }
}

class Task {
    private final String description;
    private final LocalTime start;
    private final LocalTime end;
    private final Priority priority;

    public enum Priority { HIGH, MEDIUM, LOW }

    public Task(String description, LocalTime start, LocalTime end, Priority priority) {
        this.description = description;
        this.start = start;
        this.end = end;
        this.priority = priority;
    }

    public String getDescription() { return description; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public Priority getPriority() { return priority; }

    public static Task fromStrings(String desc, String startStr, String endStr, String priorityStr) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime s = LocalTime.parse(startStr, f);
        LocalTime e = LocalTime.parse(endStr, f);
        if (!e.isAfter(s)) throw new IllegalArgumentException("End time must be after start time");

        Priority p;
        try {
            p = Priority.valueOf(priorityStr.trim().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid priority. Use High / Medium / Low");
        }
        return new Task(desc.trim(), s, e, p);
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(f) + " - " + end.format(f) + ": " + description + " [" + priority + "]";
    }
}

class TaskFactory {
    public static Task create(String description, String start, String end, String priority) {
        return Task.fromStrings(description, start, end, priority);
    }
}

interface TaskObserver {
    void onConflict(Task newTask, Task existingTask);
}
