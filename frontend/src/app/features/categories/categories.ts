import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CategoryService } from '../../core/services/category';
import { Category } from '../../shared/models/task.model';
import { CategoryFormComponent } from './components/category-form/category-form';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  templateUrl: './categories.html',
  styleUrl: './categories.css'
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  categories = signal<Category[]>([]);
  displayedColumns: string[] = ['color', 'name', 'taskCount', 'actions'];

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.findAll().subscribe({
      next: (res) => this.categories.set(res),
      error: () => this.snackBar.open('Erro ao carregar categorias', 'Fechar', { duration: 3000 })
    });
  }

  openForm(category?: Category): void {
    const dialogRef = this.dialog.open(CategoryFormComponent, {
      width: '400px',
      data: category
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCategories();
      }
    });
  }

  deleteCategory(category: Category): void {
    if (confirm(`Tem certeza que deseja excluir a categoria "${category.name}"? As tarefas associadas ficarão sem categoria.`)) {
      this.categoryService.delete(category.id).subscribe({
        next: () => {
          this.snackBar.open('Categoria excluída com sucesso', 'Sucesso', { duration: 3000 });
          this.loadCategories();
        },
        error: () => this.snackBar.open('Erro ao excluir categoria', 'Fechar', { duration: 3000 })
      });
    }
  }
}
