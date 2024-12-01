
import java.io.*;
import java.util.*;

public class TaskManager {

    private static final String TASKS_FILE = "tasks.txt";

    public static void main(String[] args) {
        List<Task> tasks = loadTasks();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Gerenciador de Tarefas ---");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Concluir Tarefa");
            System.out.println("4. Remover Tarefa");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTask(tasks, scanner);
                    break;
                case "2":
                    listTasks(tasks);
                    break;
                case "3":
                    completeTask(tasks, scanner);
                    break;
                case "4":
                    removeTask(tasks, scanner);
                    break;
                case "5":
                    saveTasks(tasks);
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void addTask(List<Task> tasks, Scanner scanner) {
        System.out.print("Digite a nova tarefa: ");
        String taskDescription = scanner.nextLine();
        tasks.add(new Task(taskDescription, false));
        System.out.println("Tarefa adicionada com sucesso!");
    }

    private static void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String status = task.isCompleted() ? "✔️" : "❌";
                System.out.printf("%d. %s - %s%n", i + 1, task.getDescription(), status);
            }
        }
    }

    private static void completeTask(List<Task> tasks, Scanner scanner) {
        listTasks(tasks);
        System.out.print("Digite o número da tarefa para marcar como concluída: ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine());
            tasks.get(taskNumber - 1).setCompleted(true);
            System.out.println("Tarefa concluída com sucesso!");
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Número inválido.");
        }
    }

    private static void removeTask(List<Task> tasks, Scanner scanner) {
        listTasks(tasks);
        System.out.print("Digite o número da tarefa para remover: ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine());
            Task removedTask = tasks.remove(taskNumber - 1);
            System.out.println("Tarefa '" + removedTask.getDescription() + "' removida com sucesso!");
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Número inválido.");
        }
    }

    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                tasks.add(new Task(parts[0], Boolean.parseBoolean(parts[1])));
            }
        } catch (IOException e) {
            // Arquivo não encontrado, inicializando lista vazia
        }
        return tasks;
    }

    private static void saveTasks(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + ";" + task.isCompleted());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas.");
        }
    }
}

class Task {
    private String description;
    private boolean completed;

    public Task(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
