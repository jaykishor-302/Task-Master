package hella;


import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

class Task implements Serializable {
    String title;
    String description;
    Date dueDate;
    int priority; // 1 for low, 2 for medium, 3 for high
    boolean completed; // Indicates if the task is completed

    public Task(String title, String description, Date dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Task " +
                "Title:'" + title + '\'' +
                "\nDescription:'" + description + '\'' +
                "\nDue Date:" + dueDate +
                "\nPriority:" + priority +
                "\nCompleted:" + completed + "\n";
    }
}

class TaskNode implements Serializable {
    Task task;
    List<TaskNode> children;

    public TaskNode() {
        this.children = new ArrayList<>();
    }

    public TaskNode(Task task) {
        this.task = task;
        this.children = new ArrayList<>();
    }

    public void addChild(TaskNode child) {
        children.add(child);
    }

    public void removeChild(TaskNode child) {
        children.remove(child);
    }

    public TaskNode findTaskNodeByTitle(String title) {
        if (this.task != null && this.task.title.equals(title)) {
            return this;
        }
        for (TaskNode child : children) {
            TaskNode found = child.findTaskNodeByTitle(title);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public List<Task> searchTasks(String keyword) {
        List<Task> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
        if (this.task != null && pattern.matcher(this.task.title).find()) {
            results.add(this.task);
        }
        for (TaskNode child : children) {
            results.addAll(child.searchTasks(keyword));
        }
        return results;
    }

    public void getAllTasks(List<Task> tasks) {
        if (this.task != null) {
            tasks.add(this.task);
        }
        for (TaskNode child : children) {
            child.getAllTasks(tasks);
        }
    }
}

class TaskSystem {
    private TaskNode root;
    private static final String FILENAME = "tasks.ser";

    public TaskSystem() {
        root = new TaskNode();
        loadTasksFromFile(); // Load tasks from file on startup
    }

    public void createTask(String title, String description, Date dueDate, int priority) {
        Task task = new Task(title, description, dueDate, priority);
        TaskNode taskNode = new TaskNode(task);
        root.addChild(taskNode);
        saveTasksToFile(); // Save to file after adding
    }

    public void addSubtask(String parentTitle, String title, String description, Date dueDate, int priority) {
        TaskNode parentNode = root.findTaskNodeByTitle(parentTitle);
        if (parentNode != null) {
            Task subtask = new Task(title, description, dueDate, priority);
            parentNode.addChild(new TaskNode(subtask));
            saveTasksToFile(); // Save to file after adding subtask
        } else {
            System.out.println("Parent task not found.");
        }
    }

    public void deleteTask(String title) {
        TaskNode taskNodeToRemove = root.findTaskNodeByTitle(title);
        if (taskNodeToRemove != null && taskNodeToRemove != root) {
            TaskNode parentNode = findParentNode(root, taskNodeToRemove);
            if (parentNode != null) {
                parentNode.removeChild(taskNodeToRemove);
                saveTasksToFile(); // Save to file after deleting
            }
        }
    }

    public void deleteSubtask(String parentTitle, String subtaskTitle) {
        TaskNode parentNode = root.findTaskNodeByTitle(parentTitle);
        if (parentNode != null) {
            TaskNode subtaskNode = parentNode.findTaskNodeByTitle(subtaskTitle);
            if (subtaskNode != null) {
                parentNode.removeChild(subtaskNode);
                saveTasksToFile(); // Save to file after deleting subtask
            }
        }
    }

    public void markTaskAsCompleted(String title) {
        TaskNode taskNode = root.findTaskNodeByTitle(title);
        if (taskNode != null) {
            taskNode.task.completed = true;
            saveTasksToFile(); // Save to file after marking as completed
        }
    }

    public void markSubtaskAsCompleted(String parentTitle, String subtaskTitle) {
        TaskNode parentNode = root.findTaskNodeByTitle(parentTitle);
        if (parentNode != null) {
            TaskNode subtaskNode = parentNode.findTaskNodeByTitle(subtaskTitle);
            if (subtaskNode != null) {
                subtaskNode.task.completed = true;
                saveTasksToFile(); // Save to file after marking subtask as completed
            }
        }
    }

    public PriorityQueue<Task> getTasksByPriority() {
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>((t1, t2) -> t2.priority - t1.priority);
        collectTasksByPriority(root, priorityQueue);
        return priorityQueue;
    }

    private void collectTasksByPriority(TaskNode node, PriorityQueue<Task> priorityQueue) {
        if (node.task != null) {
            priorityQueue.add(node.task);
        }
        for (TaskNode child : node.children) {
            collectTasksByPriority(child, priorityQueue);
        }
    }

    public List<TaskNode> getAllTasks() {
        return root.children;
    }

    public List<Task> getAllTasksFlat() {
        List<Task> tasks = new ArrayList<>();
        root.getAllTasks(tasks);
        return tasks;
    }

    public Task getTaskByTitle(String title) {
        TaskNode taskNode = root.findTaskNodeByTitle(title);
        return taskNode != null ? taskNode.task : null;
    }

    public List<Task> searchTasks(String keyword) {
        return root.searchTasks(keyword);
    }

    private TaskNode findParentNode(TaskNode current, TaskNode target) {
        for (TaskNode child : current.children) {
            if (child == target) {
                return current;
            }
            TaskNode found = findParentNode(child, target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        File file = new File(FILENAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
                root = (TaskNode) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
