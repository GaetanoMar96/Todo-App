package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @jakarta.persistence.Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq")
    @SequenceGenerator(name = "tasks_seq", sequenceName = "tasks_seq", allocationSize = 1)
    private Long taskId;

    @Column(name = "task_name")
    private String name;
    @Column(name = "task_description")
    private String description;

    private LocalDateTime deadline;

    @Column(name = "category_id")
    private Long categoryId;
}
