import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TokenService } from '../../core/services/token';
import { TaskService } from '../../core/services/task';
import { Router } from '@angular/router';
import { Task, TaskStatus } from '../../shared/models/task.model';
import { TaskFormComponent } from './components/task-form/task-form';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatInputModule,
    MatFormFieldModule,
    MatBadgeModule,
    MatSnackBarModule,
    MatDialogModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private tokenService = inject(TokenService);
  private taskService = inject(TaskService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private cdr = inject(ChangeDetectorRef);

  userName: string = 'Usuário'; 
  tasks: Task[] = [];
  loading: boolean = false;

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading = true;
    this.cdr.detectChanges();
    
    this.taskService.findAll().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erro ao carregar tarefas', err);
        this.snackBar.open('Erro ao carregar tarefas. Tente novamente.', 'Fechar', { duration: 5000 });
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get stats() {
    return {
      pending: this.tasks.filter(t => t.status === TaskStatus.PENDING).length,
      doing: this.tasks.filter(t => t.status === TaskStatus.DOING).length,
      done: this.tasks.filter(t => t.status === TaskStatus.DONE).length
    };
  }

  openTaskForm(task?: Task): void {
    const dialogRef = this.dialog.open(TaskFormComponent, {
      width: 'auto',
      maxWidth: '90vw',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTasks();
      }
    });
  }

  startTask(id: string): void {
    this.taskService.startTask(id).subscribe({
      next: () => {
        this.snackBar.open('Tarefa iniciada!', 'Fechar', { duration: 2000 });
        this.loadTasks();
      },
      error: (err) => this.handleError(err)
    });
  }

  completeTask(id: string): void {
    this.taskService.completeTask(id).subscribe({
      next: () => {
        this.snackBar.open('Tarefa concluída!', 'Fechar', { duration: 2000 });
        this.loadTasks();
      },
      error: (err) => this.handleError(err)
    });
  }

  deleteTask(id: string): void {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.taskService.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Tarefa excluída!', 'Fechar', { duration: 2000 });
          this.loadTasks();
        },
        error: (err) => this.handleError(err)
      });
    }
  }

  private handleError(err: any): void {
    const msg = err.error?.message || 'Ocorreu um erro na operação.';
    this.snackBar.open(msg, 'Fechar', { duration: 5000 });
  }

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }
}