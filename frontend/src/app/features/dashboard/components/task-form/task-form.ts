import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { CategoryService } from '../../../../core/services/category';
import { TaskService } from '../../../../core/services/task';
import { Category, Priority, Task, TaskRequest } from '../../../../shared/models/task.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-task-form',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css'
})
export class TaskFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);
  private taskService = inject(TaskService);
  private snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<TaskFormComponent>);
  public data = inject<Task>(MAT_DIALOG_DATA);

  categories: Category[] = [];
  priorities = [
    { value: Priority.LOW, label: 'Baixa' },
    { value: Priority.MEDIUM, label: 'Média' },
    { value: Priority.HIGH, label: 'Alta' }
  ];

  taskForm = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(3)]],
    description: [''],
    priority: [Priority.LOW, [Validators.required]],
    categoryId: ['', [Validators.required]],
    deadline: ['']
  });

  isEdit = false;

  ngOnInit(): void {
    this.loadCategories();
    if (this.data) {
      this.isEdit = true;
      this.taskForm.patchValue({
        title: this.data.title,
        description: this.data.description,
        priority: this.data.priority,
        categoryId: this.data.category.id,
        deadline: this.data.deadline as any
      });
    }
  }

  loadCategories(): void {
    this.categoryService.findAll().subscribe({
      next: (res) => this.categories = res,
      error: () => this.snackBar.open('Erro ao carregar categorias', 'Fechar', { duration: 3000 })
    });
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.getRawValue();
      const taskRequest: TaskRequest = {
        title: formValue.title!,
        description: formValue.description!,
        priority: formValue.priority!,
        categoryId: formValue.categoryId!,
        deadline: formValue.deadline ? new Date(formValue.deadline).toISOString() : undefined
      };

      if (this.isEdit) {
        this.taskService.update(this.data.id, taskRequest).subscribe({
          next: () => {
            this.snackBar.open('Tarefa atualizada!', 'Fechar', { duration: 2000 });
            this.dialogRef.close(true);
          },
          error: (err) => this.handleError(err)
        });
      } else {
        this.taskService.create(taskRequest).subscribe({
          next: () => {
            this.snackBar.open('Tarefa criada!', 'Fechar', { duration: 2000 });
            this.dialogRef.close(true);
          },
          error: (err) => this.handleError(err)
        });
      }
    }
  }

  private handleError(err: any): void {
    const msg = err.error?.message || 'Erro ao salvar tarefa.';
    this.snackBar.open(msg, 'Fechar', { duration: 5000 });
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}