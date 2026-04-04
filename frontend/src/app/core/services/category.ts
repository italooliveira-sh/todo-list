import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category, CategoryRequest } from '../../shared/models/task.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/categories';

  findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.API_URL);
  }

  create(data: CategoryRequest): Observable<Category> {
    return this.http.post<Category>(this.API_URL, data);
  }

  update(id: string, data: CategoryRequest): Observable<Category> {
    return this.http.put<Category>(`${this.API_URL}/${id}`, data);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}