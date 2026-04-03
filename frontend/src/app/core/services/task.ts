import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task, TaskRequest } from '../../shared/models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/tasks';

  findAll(): Observable<Task[]> {
    return this.http.get<Task[]>(this.API_URL);
  }

  findById(id: string): Observable<Task> {
    return this.http.get<Task>(`${this.API_URL}/${id}`);
  }

  create(data: TaskRequest): Observable<Task> {
    return this.http.post<Task>(this.API_URL, data);
  }

  update(id: string, data: TaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.API_URL}/${id}`, data);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  completeTask(id: string): Observable<Task> {
    return this.http.patch<Task>(`${this.API_URL}/${id}/done`, {});
  }

  startTask(id: string): Observable<Task> {
    return this.http.patch<Task>(`${this.API_URL}/${id}/start`, {});
  }
}