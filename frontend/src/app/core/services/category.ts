import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Category, CategoryRequest } from '../../shared/models/task.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/categories';

  // Fonte única da verdade para categorias em toda a aplicação
  private _categories = signal<Category[]>([]);
  public categories = this._categories.asReadonly();

  findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.API_URL).pipe(
      tap(res => {
        const sorted = [...res].sort((a, b) => a.name.localeCompare(b.name));
        this._categories.set(sorted);
      })
    );
  }

  // Atalho para atualizar o sinal sem precisar lidar com o Observable manualmente nos componentes
  refresh(): void {
    this.findAll().subscribe();
  }

  create(data: CategoryRequest): Observable<Category> {
    return this.http.post<Category>(this.API_URL, data).pipe(
      tap(() => this.refresh())
    );
  }

  update(id: string, data: CategoryRequest): Observable<Category> {
    return this.http.put<Category>(`${this.API_URL}/${id}`, data).pipe(
      tap(() => this.refresh())
    );
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      tap(() => this.refresh())
    );
  }
}
